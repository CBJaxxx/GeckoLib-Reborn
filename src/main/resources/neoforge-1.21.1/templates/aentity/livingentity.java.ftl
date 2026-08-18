<#-- @formatter:off -->
<#include "../mcitems.ftl">
<#include "../procedures.java.ftl">

<#--
 # Water AI detection:
 # RandomSwimmingGoal ("swim" block) needs WaterBoundPathNavigation + fish MoveControl.
 # "swim_in_water" is FloatGoal (land mobs float in water) — NOT aquatic AI.
 # Enabling WaterBoundPathNavigation for FloatGoal breaks land pathing so melee/wander
 # never get paths and only RandomLookAround appears to work.
 # Full water stack only when waterMob is set OR true "swim" AI is present.
-->
<#assign needsWaterAI = data.waterMob || (aiblocks?? && aiblocks?seq_contains("swim"))>

package ${package}.entity;

import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import javax.annotation.Nullable;

import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;

<#assign extendsClass = "PathfinderMob">

<#if data.aiBase != "(none)" >
	<#assign extendsClass = data.aiBase?replace("Enderman", "EnderMan")>
<#else>
	<#assign extendsClass = data.mobBehaviourType?replace("Mob", "Monster")?replace("Creature", "PathfinderMob")>
</#if>

<#if data.breedable>
	<#assign extendsClass = "Animal">
</#if>

<#if (data.tameable && data.breedable)>
	<#assign extendsClass = "TamableAnimal">
</#if>

public class ${name}Entity extends ${extendsClass} <#if data.ranged>implements RangedAttackMob, GeoEntity</#if><#if !data.ranged>implements GeoEntity</#if> {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(
      ${name}Entity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(
      ${name}Entity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(
      ${name}Entity.class, EntityDataSerializers.STRING);
	<#-- One synched slot per custom controller, so procedures can drive each
	     controller independently and the value reaches the rendering client. -->
	<#list data.getValidControllers() as ctrl>
	public static final EntityDataAccessor<String> ANIMATION_${ctrl.name?upper_case} = SynchedEntityData.defineId(
	  ${name}Entity.class, EntityDataSerializers.STRING);
	</#list>

	<#if data.mobBehaviourType == "Raider">
	public static final EnumProxy<Raid.RaiderType> RAIDER_TYPE = new EnumProxy<>(Raid.RaiderType.class,
		${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}, new int[] {0, ${data.raidSpawnsCount[0]}, ${data.raidSpawnsCount[1]}, ${data.raidSpawnsCount[2]}, ${data.raidSpawnsCount[3]}, ${data.raidSpawnsCount[4]}, ${data.raidSpawnsCount[5]}, ${data.raidSpawnsCount[6]}}
	);
	</#if>

	<#list data.entityDataEntries as entry>
		<#if entry.value().getClass().getSimpleName() == "Integer">
			public static final EntityDataAccessor<Integer> DATA_${entry.property().getName()} = SynchedEntityData.defineId(${name}Entity.class, EntityDataSerializers.INT);
		<#elseif entry.value().getClass().getSimpleName() == "Boolean">
			public static final EntityDataAccessor<Boolean> DATA_${entry.property().getName()} = SynchedEntityData.defineId(${name}Entity.class, EntityDataSerializers.BOOLEAN);
		<#elseif entry.value().getClass().getSimpleName() == "String">
			public static final EntityDataAccessor<String> DATA_${entry.property().getName()} = SynchedEntityData.defineId(${name}Entity.class, EntityDataSerializers.STRING);
		</#if>
	</#list>

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean swinging;
	private boolean lastloop;
	private long lastSwing;
        public String animationprocedure = "empty";
	<#list data.getValidControllers() as ctrl>
	public String animation_${ctrl.name} = "empty";
	private String prevAnim_${ctrl.name} = "empty";
	</#list>
	<#if data.getValidControllers()?has_content>
	/** Separates the animation name from the request token; see tagAnimationRequest. */
	private static final String ANIMATION_REQUEST_SEPARATOR = "#";
	private int animationRequestCounter;
	</#if>
	<#if data.isBoss>
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(),
		ServerBossEvent.BossBarColor.${data.bossBarColor}, ServerBossEvent.BossBarOverlay.${data.bossBarType});
	</#if>

	public ${name}Entity(EntityType<${name}Entity> type, Level world) {
    	super(type, world);
		xpReward = ${data.xpAmount};
		setNoAi(${(!data.hasAI)});

		<#if data.mobLabel?has_content >
        	setCustomName(Component.literal("${data.mobLabel}"));
        	setCustomNameVisible(true);
        </#if>

		<#if !data.doesDespawnWhenIdle>
			setPersistenceRequired();
        </#if>

		<#if !data.equipmentMainHand.isEmpty()>
        this.setItemSlot(EquipmentSlot.MAINHAND, ${mappedMCItemToItemStackCode(data.equipmentMainHand, 1)});
        </#if>
        <#if !data.equipmentOffHand.isEmpty()>
        this.setItemSlot(EquipmentSlot.OFFHAND, ${mappedMCItemToItemStackCode(data.equipmentOffHand, 1)});
        </#if>
        <#if !data.equipmentHelmet.isEmpty()>
        this.setItemSlot(EquipmentSlot.HEAD, ${mappedMCItemToItemStackCode(data.equipmentHelmet, 1)});
        </#if>
        <#if !data.equipmentBody.isEmpty()>
        this.setItemSlot(EquipmentSlot.CHEST, ${mappedMCItemToItemStackCode(data.equipmentBody, 1)});
        </#if>
        <#if !data.equipmentLeggings.isEmpty()>
        this.setItemSlot(EquipmentSlot.LEGS, ${mappedMCItemToItemStackCode(data.equipmentLeggings, 1)});
        </#if>
        <#if !data.equipmentBoots.isEmpty()>
        this.setItemSlot(EquipmentSlot.FEET, ${mappedMCItemToItemStackCode(data.equipmentBoots, 1)});
        </#if>

		<#if data.flyingMob>
		this.moveControl = new FlyingMoveControl(this, 10, true);
		<#elseif needsWaterAI>
		// Aquatic stack modeled on AbstractFish (FishMoveControl + custom travel):
		// LivingEntity.travel() ignores getSpeed() in water unless WATER_MOVEMENT_EFFICIENCY > 0,
		// so AI goal speed factors (3 vs 50) had no visible effect with SmoothSwimming alone.
		this.setPathfindingMalus(PathType.WATER, 0);
		this.moveControl = new MoveControl(this) {
			@Override public void tick() {
				Mob mob = ${name}Entity.this;
				if (this.operation == MoveControl.Operation.MOVE_TO && !mob.getNavigation().isDone()) {
					// Buoyancy only while actively pathing (vanilla FishMoveControl always
					// adds +0.005, but pairs it with -0.005 in travel when idle). Without that
					// cancel, idle fish slowly float to the surface.
					if (mob.isInWater()) {
						mob.setDeltaMovement(mob.getDeltaMovement().add(0.0, 0.005, 0.0));
					}
					double dx = this.wantedX - mob.getX();
					double dy = this.wantedY - mob.getY();
					double dz = this.wantedZ - mob.getZ();
					double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
					if (dist < 1.0E-5) {
						mob.setSpeed(0.0F);
						mob.setZza(0.0F);
						return;
					}
					// speedModifier is the AI goal speed field (Melee/Swim/Panic)
					float speed = (float) (this.speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
					mob.setSpeed(Mth.lerp(0.125F, mob.getSpeed(), speed));
					// vertical component toward path target (prevents endless sinking)
					mob.setDeltaMovement(mob.getDeltaMovement().add(0.0, mob.getSpeed() * (dy / dist) * 0.1, 0.0));
					if (dx != 0.0 || dz != 0.0) {
						float yaw = (float) (Mth.atan2(dz, dx) * (180.0F / (float) Math.PI)) - 90.0F;
						mob.setYRot(this.rotlerp(mob.getYRot(), yaw, 90.0F));
						mob.yBodyRot = mob.getYRot();
						mob.yHeadRot = mob.getYRot();
					}
					// forward thrust used by travel()
					mob.setZza(mob.getSpeed());
				} else {
					mob.setSpeed(0.0F);
					mob.setZza(0.0F);
				}
			}
		};
		</#if>
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SHOOT, false);
	    builder.define(ANIMATION, "undefined");
		builder.define(TEXTURE, "${data.mobModelTexture?replace(".png", "")}");
		<#list data.getValidControllers() as ctrl>
		builder.define(ANIMATION_${ctrl.name?upper_case}, "undefined");
		</#list>
		<#if data.entityDataEntries?has_content>
		    <#list data.entityDataEntries as entry>
			    builder.define(DATA_${entry.property().getName()}, ${entry.value()?is_string?then("\"" + entry.value() + "\"", entry.value())});
		    </#list>
		</#if>
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	<#if hasProcedure(data.solidBoundingBox)>
	@Override
	public boolean canCollideWith(Entity entity) {
			return true;
	}

	@Override
	public boolean canBeCollidedWith() {
			Entity entity = this;
			Level world = entity.level();
			double x = entity.getX();
			double y = entity.getY();
			double z = entity.getZ();
			return <@procedureOBJToConditionCode data.solidBoundingBox/>;
	}
	</#if>

	<#if data.flyingMob>
	@Override protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}
	<#elseif needsWaterAI>
	@Override protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}
	</#if>

	<#if data.hasAI>
	@Override protected void registerGoals() {
		super.registerGoals();

		<#-- Panic must outrank Swim (vanilla fish put Panic at 0). Blockly order swim→panic puts panic too low.
		     Vanilla PanicGoal uses DefaultRandomPos.getPos (random), so fish often flee toward/past the attacker.
		     Override findRandomPosition to prefer getPosAway from last attacker / damage source. -->
		<#if aiblocks?? && aiblocks?seq_contains("panic_when_attacked")>
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.5) {
			@Override protected boolean findRandomPosition() {
				Vec3 avoid = null;
				LivingEntity attacker = this.mob.getLastHurtByMob();
				if (attacker != null) {
					avoid = attacker.position();
				} else if (this.mob.getLastDamageSource() != null) {
					Entity src = this.mob.getLastDamageSource().getEntity();
					if (src != null) {
						avoid = src.position();
					} else if (this.mob.getLastDamageSource().getSourcePosition() != null) {
						avoid = this.mob.getLastDamageSource().getSourcePosition();
					}
				}
				Vec3 pos = null;
				if (avoid != null) {
					pos = net.minecraft.world.entity.ai.util.DefaultRandomPos.getPosAway(this.mob, 10, 4, avoid);
				}
				if (pos == null) {
					pos = net.minecraft.world.entity.ai.util.DefaultRandomPos.getPos(this.mob, 10, 4);
				}
				if (pos == null) {
					return false;
				}
				this.posX = pos.x;
				this.posY = pos.y;
				this.posZ = pos.z;
				return true;
			}
		});
		</#if>
		<#if needsWaterAI>
		// Low priority: only matters on land; must not outrank panic
		this.goalSelector.addGoal(8, new TryFindWaterGoal(this));
		</#if>

		<#if aicode??>
			<#if aiblocks?? && (aiblocks?seq_contains("doors_open") || aiblocks?seq_contains("doors_close"))>
				this.getNavigation().getNodeEvaluator().setCanOpenDoors(true);
			</#if>
            ${aicode}
        </#if>

        <#if data.ranged>
            this.goalSelector.addGoal(1, new ${name}Entity.RangedAttackGoal(this, 1.25, ${data.rangedAttackInterval}, ${data.rangedAttackRadius}f) {
				@Override public boolean canContinueToUse() {
					return this.canUse();
				}
			});
        </#if>
	}
	</#if>

        <#if data.ranged>
	public class RangedAttackGoal extends Goal {
		private final Mob mob;
		private final RangedAttackMob rangedAttackMob;
		@Nullable
		private LivingEntity target;
		private int attackTime = -1;
		private final double speedModifier;
		private int seeTime;
		private final int attackIntervalMin;
		private final int attackIntervalMax;
		private final float attackRadius;
		private final float attackRadiusSqr;

		public RangedAttackGoal(RangedAttackMob p_25768_, double p_25769_, int p_25770_, float p_25771_) {
			this(p_25768_, p_25769_, p_25770_, p_25770_, p_25771_);
		}

		public RangedAttackGoal(RangedAttackMob p_25773_, double p_25774_, int p_25775_, int p_25776_, float p_25777_) {
			if (!(p_25773_ instanceof LivingEntity)) {
				throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
			} else {
				this.rangedAttackMob = p_25773_;
				this.mob = (Mob) p_25773_;
				this.speedModifier = p_25774_;
				this.attackIntervalMin = p_25775_;
				this.attackIntervalMax = p_25776_;
				this.attackRadius = p_25777_;
				this.attackRadiusSqr = p_25777_ * p_25777_;
				this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
			}
		}

		public boolean canUse() {
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity != null && livingentity.isAlive()) {
				this.target = livingentity;
				return true;
			} else {
				return false;
			}
		}

		public boolean canContinueToUse() {
			return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
		}

		public void stop() {
			this.target = null;
			this.seeTime = 0;
			this.attackTime = -1;
			((${name}Entity) rangedAttackMob).entityData.set(SHOOT, false);
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
			boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
			if (flag) {
				++this.seeTime;
			} else {
				this.seeTime = 0;
			}
			if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
				this.mob.getNavigation().stop();
			} else {
				this.mob.getNavigation().moveTo(this.target, this.speedModifier);
			}
			this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
			if (--this.attackTime == 0) {
				if (!flag) {
				((${name}Entity) rangedAttackMob).entityData.set(SHOOT, false);
					return;
				}
				((${name}Entity) rangedAttackMob).entityData.set(SHOOT, true);
				float f = (float) Math.sqrt(d0) / this.attackRadius;
				float f1 = Mth.clamp(f, 0.1F, 1.0F);
				this.rangedAttackMob.performRangedAttack(this.target, f1);
				this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) 				this.attackIntervalMin);
			} else if (this.attackTime < 0) {
				this.attackTime = Mth.floor(
						Mth.lerp(Math.sqrt(d0) / (double) this.attackRadius, (double) this.attackIntervalMin, (double) 				this.attackIntervalMax));
			}
			else
				((${name}Entity) rangedAttackMob).entityData.set(SHOOT, false);
		}
	}
	</#if>

	<#if !data.doesDespawnWhenIdle>
	@Override public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}
    </#if>

	<#if data.mountedYOffset != 0>
	@Override protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float f) {
		return super.getPassengerAttachmentPoint(entity, dimensions, f).add(0, ${data.mountedYOffset}f, 0);
	}
	</#if>

	<#if !data.mobDrop.isEmpty()>
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(serverLevel, source, recentlyHitIn);
        this.spawnAtLocation(${mappedMCItemToItemStackCode(data.mobDrop, 1)});
   	}
	</#if>

    <#if data.livingSound?has_content && data.livingSound.getUnmappedValue()?has_content>
 	@Override public SoundEvent getAmbientSound() {
 		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("${data.livingSound}"));
 	}
 	</#if>

    <#if data.stepSound?has_content && data.stepSound.getUnmappedValue()?has_content>
 	@Override public void playStepSound(BlockPos pos, BlockState blockIn) {
 		this.playSound(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("${data.stepSound}")), 0.15f, 1);
 	}
 	</#if>

 	<#if data.hurtSound?has_content && data.hurtSound.getUnmappedValue()?has_content>
 	@Override public SoundEvent getHurtSound(DamageSource ds) {
 		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("${data.hurtSound}"));
 	}
 	</#if>

 	<#if data.deathSound?has_content && data.deathSound.getUnmappedValue()?has_content>
 	@Override public SoundEvent getDeathSound() {
 		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("${data.deathSound}"));
 	}
 	</#if>

 	<#if data.mobBehaviourType == "Raider">
 	@Override public SoundEvent getCelebrateSound() {
 		<#if data.raidCelebrationSound?has_content && data.raidCelebrationSound.getMappedValue()?has_content>
 		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("${data.raidCelebrationSound}"));
 		<#else>
 		return SoundEvents.EMPTY;
 		</#if>
 	}
 	</#if>

	<#if hasProcedure(data.onStruckByLightning)>
	@Override public void thunderHit(ServerLevel serverWorld, LightningBolt lightningBolt) {
		super.thunderHit(serverWorld, lightningBolt);
		<@procedureCode data.onStruckByLightning, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"entity": "this",
			"world": "this.level()"
		}/>
	}
    </#if>

	<#if hasProcedure(data.whenMobFalls) || data.flyingMob>
	@Override public boolean causeFallDamage(float l, float d, DamageSource source) {
		<#if hasProcedure(data.whenMobFalls)>
			<@procedureCode data.whenMobFalls, {
				"x": "this.getX()",
				"y": "this.getY()",
				"z": "this.getZ()",
				"entity": "this",
				"world": "this.level()",
				"damagesource": "source"
			}/>
		</#if>

		<#if data.flyingMob >
			return false;
		<#else>
			return super.causeFallDamage(l, d, source);
		</#if>
	}
    </#if>

	<#if hasProcedure(data.whenMobIsHurt) || data.immuneToFire || data.immuneToArrows || data.immuneToFallDamage
		|| data.immuneToCactus || data.immuneToDrowning || data.immuneToLightning || data.immuneToPotions
		|| data.immuneToPlayer || data.immuneToExplosion || data.immuneToTrident || data.immuneToAnvil
		|| data.immuneToDragonBreath || data.immuneToWither>
	@Override public boolean hurt(DamageSource source, float amount) {
		<#if hasProcedure(data.whenMobIsHurt)>
			<@procedureCode data.whenMobIsHurt, {
				"x": "this.getX()",
				"y": "this.getY()",
				"z": "this.getZ()",
				"entity": "this",
				"damagesource": "source",
				"world": "this.level()",
				"sourceentity": "source.getEntity()"
			}/>
			Entity immediatesourceentity = source.getDirectEntity();
		</#if>
		<#if data.immuneToFire>
			if (source.is(DamageTypes.IN_FIRE))
				return false;
		</#if>
		<#if data.immuneToArrows>
			if (source.getDirectEntity() instanceof AbstractArrow)
				return false;
		</#if>
		<#if data.immuneToPlayer>
			if (source.getDirectEntity() instanceof Player)
				return false;
		</#if>
		<#if data.immuneToPotions>
			if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud
            					|| source.typeHolder().is(NeoForgeMod.POISON_DAMAGE))
				return false;
		</#if>
		<#if data.immuneToFallDamage>
			if (source.is(DamageTypes.FALL))
				return false;
		</#if>
		<#if data.immuneToCactus>
			if (source.is(DamageTypes.CACTUS))
				return false;
		</#if>
		<#if data.immuneToDrowning>
			if (source.is(DamageTypes.DROWN))
				return false;
		</#if>
		<#if data.immuneToLightning>
			if (source.is(DamageTypes.LIGHTNING_BOLT))
				return false;
		</#if>
		<#if data.immuneToExplosion>
			if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION))
				return false;
		</#if>
		<#if data.immuneToTrident>
			if (source.is(DamageTypes.TRIDENT))
				return false;
		</#if>
		<#if data.immuneToAnvil>
			if (source.is(DamageTypes.FALLING_ANVIL))
				return false;
		</#if>
		<#if data.immuneToDragonBreath>
			if (source.is(DamageTypes.DRAGON_BREATH))
				return false;
		</#if>
		<#if data.immuneToWither>
			if (source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL))
				return false;
		</#if>
		return super.hurt(source, amount);
	}
    </#if>

	<#if data.immuneToExplosion>
	@Override public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}
	</#if>

	<#if data.immuneToFire>
	@Override public boolean fireImmune() {
		return true;
	}
	</#if>

	<#if hasProcedure(data.whenMobDies)>
	@Override public void die(DamageSource source) {
		super.die(source);
		<@procedureCode data.whenMobDies, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"sourceentity": "source.getEntity()",
			"immediatesourceentity": "source.getDirectEntity()",
			"entity": "this",
			"world": "this.level()",
			"damagesource": "source"
		}/>
	}
    </#if>

	<#if hasProcedure(data.onInitialSpawn)>
	@Override public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata);
		<@procedureCode data.onInitialSpawn, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"world": "world",
			"entity": "this"
		}/>
		return retval;
	}
    </#if>

	<#if data.guiBoundTo?has_content && data.guiBoundTo != "<NONE>">
	private final ItemStackHandler inventory = new ItemStackHandler(${data.inventorySize}) {
		@Override public int getSlotLimit(int slot) {
			return ${data.inventoryStackSize};
		}
	};

	private final CombinedInvWrapper combined = new CombinedInvWrapper(inventory, new EntityHandsInvWrapper(this), new EntityArmorInvWrapper(this));

	public CombinedInvWrapper getInventory() {
		return combined;
	}

   	@Override protected void dropEquipment() {
		super.dropEquipment();
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack itemstack = inventory.getStackInSlot(i);
			if (!itemstack.isEmpty() && !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
				this.spawnAtLocation(itemstack);
			}
		}
	}
	</#if>

	@Override public void addAdditionalSaveData(CompoundTag compound) {
    	super.addAdditionalSaveData(compound);
    	<#if data.guiBoundTo?has_content && data.guiBoundTo != "<NONE>">
		    compound.put("InventoryCustom", inventory.serializeNBT(this.registryAccess()));
		</#if>
		compound.putString("Texture", this.getTexture());
		<#if data.entityDataEntries?has_content>
		    <#list data.entityDataEntries as entry>
			    <#if entry.value().getClass().getSimpleName() == "Integer">
			    compound.putInt("Data${entry.property().getName()}", this.entityData.get(DATA_${entry.property().getName()}));
			    <#elseif entry.value().getClass().getSimpleName() == "Boolean">
			    compound.putBoolean("Data${entry.property().getName()}", this.entityData.get(DATA_${entry.property().getName()}));
			    <#elseif entry.value().getClass().getSimpleName() == "String">
			    compound.putString("Data${entry.property().getName()}", this.entityData.get(DATA_${entry.property().getName()}));
			    </#if>
		    </#list>
		</#if>
	}

	@Override public void readAdditionalSaveData(CompoundTag compound) {
    	super.readAdditionalSaveData(compound);
    	<#if data.guiBoundTo?has_content && data.guiBoundTo != "<NONE>">
		    Tag inventoryCustom = compound.get("InventoryCustom");
		    if(inventoryCustom instanceof CompoundTag inventoryTag)
			    inventory.deserializeNBT(this.registryAccess(), inventoryTag);
		</#if>
		if (compound.contains("Texture"))
		    this.setTexture(compound.getString("Texture"));
		<#if data.entityDataEntries?has_content>
		    <#list data.entityDataEntries as entry>
			    if (compound.contains("Data${entry.property().getName()}"))
				    <#if entry.value().getClass().getSimpleName() == "Integer">
				    this.entityData.set(DATA_${entry.property().getName()}, compound.getInt("Data${entry.property().getName()}"));
				    <#elseif entry.value().getClass().getSimpleName() == "Boolean">
				    this.entityData.set(DATA_${entry.property().getName()}, compound.getBoolean("Data${entry.property().getName()}"));
				    <#elseif entry.value().getClass().getSimpleName() == "String">
				    this.entityData.set(DATA_${entry.property().getName()}, compound.getString("Data${entry.property().getName()}"));
				    </#if>
		    </#list>
		</#if>
    }

	<#if hasProcedure(data.onRightClickedOn) || data.ridable || (data.tameable && data.breedable) || (data.guiBoundTo?has_content && data.guiBoundTo != "<NONE>")>
	@Override public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		ItemStack itemstack = sourceentity.getItemInHand(hand);
		InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());

		<#if data.guiBoundTo?has_content && data.guiBoundTo != "<NONE>">
			<#if data.ridable>
				if (sourceentity.isSecondaryUseActive()) {
			</#if>
				if(sourceentity instanceof ServerPlayer serverPlayer) {
					serverPlayer.openMenu(new MenuProvider() {

						@Override public Component getDisplayName() {
							return Component.literal("${data.mobName}");
						}

						@Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
							packetBuffer.writeBlockPos(sourceentity.blockPosition());
							packetBuffer.writeByte(0);
							packetBuffer.writeVarInt(${name}Entity.this.getId());
							return new ${data.guiBoundTo}Menu(id, inventory, packetBuffer);
						}

					}, buf -> {
						buf.writeBlockPos(sourceentity.blockPosition());
						buf.writeByte(0);
						buf.writeVarInt(this.getId());
					});
				}
			<#if data.ridable>
					return InteractionResult.sidedSuccess(this.level().isClientSide());
				}
			</#if>
		</#if>

		<#if (data.tameable && data.breedable)>
			Item item = itemstack.getItem();
			if (itemstack.getItem() instanceof SpawnEggItem) {
				retval = super.mobInteract(sourceentity, hand);
			} else if (this.level().isClientSide()) {
				retval = (this.isTame() && this.isOwnedBy(sourceentity) || this.isFood(itemstack))
						? InteractionResult.sidedSuccess(this.level().isClientSide()) : InteractionResult.PASS;
			} else {
				if (this.isTame()) {
					if (this.isOwnedBy(sourceentity)) {
						if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
							this.usePlayerItem(sourceentity, hand, itemstack);
							FoodProperties foodproperties = itemstack.getFoodProperties(this);
							float nutrition = foodproperties != null ? (float) foodproperties.nutrition() : 1;
							this.heal(nutrition);
							retval = InteractionResult.sidedSuccess(this.level().isClientSide());
						} else if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
							this.usePlayerItem(sourceentity, hand, itemstack);
							this.heal(4);
							retval = InteractionResult.sidedSuccess(this.level().isClientSide());
						} else {
							retval = super.mobInteract(sourceentity, hand);
						}
					}
				} else if (this.isFood(itemstack)) {
					this.usePlayerItem(sourceentity, hand, itemstack);
					if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, sourceentity)) {
						this.tame(sourceentity);
						this.level().broadcastEntityEvent(this, (byte) 7);
					} else {
						this.level().broadcastEntityEvent(this, (byte) 6);
					}

					this.setPersistenceRequired();
					retval = InteractionResult.sidedSuccess(this.level().isClientSide());
				} else {
					retval = super.mobInteract(sourceentity, hand);
					if (retval == InteractionResult.SUCCESS || retval == InteractionResult.CONSUME)
						this.setPersistenceRequired();
				}
			}
		<#else>
			super.mobInteract(sourceentity, hand);
		</#if>

		<#if data.ridable>
		sourceentity.startRiding(this);
	    </#if>

		<#if hasProcedure(data.onRightClickedOn)>
			double x = this.getX();
			double y = this.getY();
			double z = this.getZ();
			Entity entity = this;
			Level world = this.level();
			<#if hasReturnValueOf(data.onRightClickedOn, "actionresulttype")>
				return <@procedureOBJToInteractionResultCode data.onRightClickedOn/>;
			<#else>
				<@procedureOBJToCode data.onRightClickedOn/>
				return retval;
			</#if>
		<#else>
			return retval;
		</#if>
	}
    </#if>

	<#if hasProcedure(data.whenThisMobKillsAnother)>
	@Override public void awardKillScore(Entity entity, int score, DamageSource damageSource) {
		super.awardKillScore(entity, score, damageSource);
		<@procedureCode data.whenThisMobKillsAnother, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"entity": "entity",
			"sourceentity": "this",
			"immediatesourceentity": "damageSource.getDirectEntity()",
			"world": "this.level()",
			"damagesource": "damageSource"
		}/>
	}
    </#if>

	<#if hasProcedure(data.onMobTickUpdate) || data.boundingBoxScale??>
	@Override public void baseTick() {
		super.baseTick();
		<#if hasProcedure(data.onMobTickUpdate)>
		<@procedureCode data.onMobTickUpdate, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"entity": "this",
			"world": "this.level()"
		}/>
		</#if>
		<#if data.boundingBoxScale??>
        	this.refreshDimensions();
        </#if>
	}
    </#if>

    <#if data.boundingBoxScale??>
    @Override public EntityDimensions getDefaultDimensions(Pose pose) {
    	<#if hasProcedure(data.boundingBoxScale)>
    		Entity entity = this;
    		Level world = this.level();
    		double x = this.getX();
    		double y = entity.getY();
    		double z = entity.getZ();
    		return super.getDefaultDimensions(pose).scale((float) <@procedureOBJToNumberCode data.boundingBoxScale/>)<#if data.eyeHeight>.withEyeHeight(${data.height}f)</#if>;
    	<#else>
    		return super.getDefaultDimensions(pose).scale(${data.boundingBoxScale.getFixedValue()}f)<#if data.eyeHeight>.withEyeHeight(${data.height}f)</#if>;
    	</#if>
    }
    </#if>

	<#if hasProcedure(data.onPlayerCollidesWith)>
	@Override public void playerTouch(Player sourceentity) {
		super.playerTouch(sourceentity);
		<@procedureCode data.onPlayerCollidesWith, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"entity": "this",
			"sourceentity": "sourceentity",
			"world": "this.level()"
		}/>
	}
    </#if>

    <#if data.ranged>
	    @Override public void performRangedAttack(LivingEntity target, float flval) {
			<#if data.rangedItemType == "Default item">
				<#if !data.rangedAttackItem.isEmpty()>
				${name}EntityProjectile entityarrow = new ${name}EntityProjectile(${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}_PROJECTILE.get(), this, this.level());
				<#else>
				Arrow entityarrow = new Arrow(this.level(), this, new ItemStack(Items.ARROW), null);
				</#if>
				double d0 = target.getY() + target.getEyeHeight() - 1.1;
				double d1 = target.getX() - this.getX();
				double d3 = target.getZ() - this.getZ();
				entityarrow.shoot(d1, d0 - entityarrow.getY() + Math.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 1.6F, 12.0F);
				this.level().addFreshEntity(entityarrow);
			<#else>
				${data.rangedItemType}Entity.shoot(this, target);
			</#if>
		}
    </#if>

	<#if data.breedable>
        @Override public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
			${name}Entity retval = ${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}.get().create(serverWorld);
			retval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(retval.blockPosition()), MobSpawnType.BREEDING, null);
			return retval;
		}

		@Override public boolean isFood(ItemStack stack) {
			return List.of(<#list data.breedTriggerItems as breedTriggerItem>${mappedMCItemToItem(breedTriggerItem)}<#sep>,</#list>).contains(stack.getItem());
		}
    </#if>

	<#if needsWaterAI>
	@Override public boolean canDrownInFluidType(FluidType type) {
    	return false;
    }

    @Override public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this);
	}

    @Override public boolean isPushedByFluid() {
		return false;
    }
	</#if>

	<#if data.disableCollisions>
	@Override public boolean isPushable() {
		return false;
	}

   	@Override protected void doPush(Entity entityIn) {
   	}

   	@Override protected void pushEntities() {
   	}
	</#if>

	<#if data.isBoss>
	@Override public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override public void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}
	</#if>

	<#-- Single travel() for ridable and/or water AI (cannot define twice) -->
	<#if needsWaterAI || (data.ridable && (data.canControlForward || data.canControlStrafe))>
	@Override public void travel(Vec3 travelVector) {
		<#if data.ridable && (data.canControlForward || data.canControlStrafe)>
		Entity rider = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
		if (this.isVehicle() && rider != null) {
			this.setYRot(rider.getYRot());
			this.yRotO = this.getYRot();
			this.setXRot(rider.getXRot() * 0.5F);
			this.setRot(this.getYRot(), this.getXRot());
			this.yBodyRot = rider.getYRot();
			this.yHeadRot = rider.getYRot();
			if (rider instanceof LivingEntity passenger) {
				this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
				<#if data.canControlForward>
				float forward = passenger.zza;
				<#else>
				float forward = 0;
				</#if>
				<#if data.canControlStrafe>
				float strafe = passenger.xxa;
				<#else>
				float strafe = 0;
				</#if>
				super.travel(new Vec3(strafe, 0, forward));
			}
			double d1 = this.getX() - this.xo;
			double d0 = this.getZ() - this.zo;
			float f1 = (float) Math.sqrt(d1 * d1 + d0 * d0) * 4;
			if (f1 > 1.0F) f1 = 1.0F;
			this.walkAnimation.setSpeed(this.walkAnimation.speed() + (f1 - this.walkAnimation.speed()) * 0.4F);
			this.walkAnimation.position(this.walkAnimation.position() + this.walkAnimation.speed());
			this.calculateEntityAnimation(true);
			return;
		}
		</#if>
		<#if needsWaterAI>
		// Fish-style travel: default water physics ignore getSpeed() unless WATER_MOVEMENT_EFFICIENCY > 0
		if (this.isEffectiveAi() && this.isInWater()) {
			float amount = Mth.clamp(this.getSpeed() * 0.1F, 0.01F, 1.0F);
			this.moveRelative(amount, travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
			return;
		}
		</#if>
		super.travel(travelVector);
	}
	</#if>

	<#if data.flyingMob>
	@Override protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   	}

   	@Override public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}
    </#if>

    <#if extendsClass != "Monster">
    	@Override
    </#if>
    <#if data.flyingMob || extendsClass != "Monster">
        public void aiStep() {
        super.aiStep();
        <#if extendsClass != "Monster">
        this.updateSwingTime();
        </#if>
        <#if data.flyingMob>
           this.setNoGravity(true);
           </#if>
        }
        </#if>

	public static void init(RegisterSpawnPlacementsEvent event) {
		<#if data.spawnThisMob>
			<#if data.mobSpawningType.getUnmappedValue() == "creature">
			event.register(${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}.get(),
					SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				<#if hasProcedure(data.spawningCondition)>
					(entityType, world, reason, pos, random) -> {
						int x = pos.getX();
						int y = pos.getY();
						int z = pos.getZ();
						return <@procedureOBJToConditionCode data.spawningCondition/>;
					}
				<#else>
					(entityType, world, reason, pos, random) ->
							(world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) &&
							world.getRawBrightness(pos, 0) > 8)
				</#if>,
				RegisterSpawnPlacementsEvent.Operation.REPLACE
			);
			<#elseif data.mobSpawningType.getUnmappedValue() == "ambient" || data.mobSpawningType.getUnmappedValue() == "misc">
			event.register(${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}.get(),
					SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					<#if hasProcedure(data.spawningCondition)>
					(entityType, world, reason, pos, random) -> {
						int x = pos.getX();
						int y = pos.getY();
						int z = pos.getZ();
						return <@procedureOBJToConditionCode data.spawningCondition/>;
					}
					<#else>
					Mob::checkMobSpawnRules
					</#if>,
					RegisterSpawnPlacementsEvent.Operation.REPLACE
			);
			<#elseif data.mobSpawningType.getUnmappedValue() == "waterCreature" || data.mobSpawningType.getUnmappedValue() == "waterAmbient">
			event.register(${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}.get(),
					SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					<#if hasProcedure(data.spawningCondition)>
					(entityType, world, reason, pos, random) -> {
						int x = pos.getX();
						int y = pos.getY();
						int z = pos.getZ();
						return <@procedureOBJToConditionCode data.spawningCondition/>;
					}
					<#else>
					(entityType, world, reason, pos, random) ->
							(world.getBlockState(pos).is(Blocks.WATER) &&
							world.getBlockState(pos.above()).is(Blocks.WATER))
					</#if>,
					RegisterSpawnPlacementsEvent.Operation.REPLACE
			);
			<#elseif data.mobSpawningType.getUnmappedValue() == "undergroundWaterCreature">
			event.register(${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}.get(),
					SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					<#if hasProcedure(data.spawningCondition)>
					(entityType, world, reason, pos, random) -> {
						int x = pos.getX();
						int y = pos.getY();
						int z = pos.getZ();
						return <@procedureOBJToConditionCode data.spawningCondition/>;
					}
					<#else>
					(entityType, world, reason, pos, random) ->
							(world.getFluidState(pos.below()).is(FluidTags.WATER) &&
							world.getBlockState(pos.above()).is(Blocks.WATER) &&
							pos.getY() >= (world.getSeaLevel() - 13) &&
							pos.getY() <= world.getSeaLevel())
					</#if>,
					RegisterSpawnPlacementsEvent.Operation.REPLACE
			);
			<#else>
			event.register(${JavaModName}Entities.${data.getModElement().getRegistryNameUpper()}.get(),
					SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					<#if hasProcedure(data.spawningCondition)>
					(entityType, world, reason, pos, random) -> {
						int x = pos.getX();
						int y = pos.getY();
						int z = pos.getZ();
						return <@procedureOBJToConditionCode data.spawningCondition/>;
					}
					<#else>
						(entityType, world, reason, pos, random) ->
								(world.getDifficulty() != Difficulty.PEACEFUL &&
								Monster.isDarkEnoughToSpawn(world, pos, random) &&
								Mob.checkMobSpawnRules(entityType, world, reason, pos, random))
					</#if>,
					RegisterSpawnPlacementsEvent.Operation.REPLACE
			);
			</#if>
		</#if>
	}

	<#if data.mobBehaviourType == "Raider">
   	@Override public void applyRaidBuffs(ServerLevel serverLevel, int num, boolean logic) {}
   	</#if>

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, ${data.movementSpeed});
		builder = builder.add(Attributes.MAX_HEALTH, ${data.health});
		builder = builder.add(Attributes.ARMOR, ${data.armorBaseValue});
		builder = builder.add(Attributes.ATTACK_DAMAGE, ${data.attackStrength});
		builder = builder.add(Attributes.FOLLOW_RANGE, ${data.followRange});
		builder = builder.add(Attributes.STEP_HEIGHT, ${data.stepHeight});

		<#if (data.knockbackResistance > 0)>
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, ${data.knockbackResistance});
		</#if>

		<#if (data.attackKnockback > 0)>
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, ${data.attackKnockback});
		</#if>

		<#if data.flyingMob>
		builder = builder.add(Attributes.FLYING_SPEED, ${data.movementSpeed});
		</#if>

		<#if needsWaterAI>
		builder = builder.add(NeoForgeMod.SWIM_SPEED, ${data.movementSpeed});
		// So default water travel (if used) actually respects getSpeed() from AI goals
		builder = builder.add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0);
		</#if>

		<#if data.aiBase == "Zombie">
		builder = builder.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
		</#if>

		return builder;
	}

	private PlayState movementPredicate(AnimationState event) {
	      if (this.animationprocedure.equals("empty")) {
		<#if data.enable2>
		if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))
		<#if data.enable8>&& this.onGround()</#if> <#if data.enable9>&& !this.isVehicle()</#if>
		<#if data.enable10>&& !this.isAggressive()</#if> <#if data.enable7>&& !this.isSprinting()</#if>) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation2}"));
		}
		</#if>
		<#if data.enable3>
		if (this.isDeadOrDying()) {
			return event.setAndContinue(RawAnimation.begin().thenPlay("${data.animation3}"));
		}
		</#if>
		<#if data.enable5>
		if (this.isInWaterOrBubble()) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation5}"));
		}
		</#if>
		<#if data.enable6>
		if (this.isShiftKeyDown()) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation6}"));
		}
		</#if>
		<#if data.enable7>
		if (this.isSprinting()) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation7}"));
		}
		</#if>
		<#if data.enable8>
		if (!this.onGround()) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation8}"));
		}
		</#if>
		<#if data.enable9>
		if (this.isVehicle() && event.isMoving()) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation9}"));
		}
		</#if>
		<#if data.enable10>
		if (this.isAggressive() && event.isMoving()<#if data.enable9> && !this.isVehicle()</#if>) {
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation10}"));
		}
		</#if>
			return event.setAndContinue(RawAnimation.begin().thenLoop("${data.animation1}"));
	}
        return PlayState.STOP;
	}

	<#if data.enable4>
	private PlayState attackingPredicate(AnimationState event) {
		double d1 = this.getX() - this.xOld;
		double d0 = this.getZ() - this.zOld;
		float velocity = (float) Math.sqrt(d1 * d1 + d0 * d0);
		if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
			this.swinging = true;
			this.lastSwing = level().getGameTime();
		}
		if (this.swinging && this.lastSwing + ${data.attackRate}L <= level().getGameTime()) {
			this.swinging = false;
		}
		if (<#if data.ranged>(</#if>this.swinging<#if data.ranged> || this.entityData.get(SHOOT))</#if>
		&& event.getController().getAnimationState() == AnimationController.State.STOPPED) {
			event.getController().forceAnimationReset();
			return event.setAndContinue(RawAnimation.begin().thenPlay("${data.animation4}"));
		}
	return PlayState.CONTINUE;
   	}
	</#if>

    String prevAnim = "empty";
	private PlayState procedurePredicate(AnimationState event) {
		if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
		    if (!this.animationprocedure.equals(prevAnim))
		        event.getController().forceAnimationReset();
			event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
				this.animationprocedure = "empty";
				event.getController().forceAnimationReset();
			}
		} else if (animationprocedure.equals("empty")) {
		    prevAnim = "empty";
			return PlayState.STOP;
		}
		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	<#if data.getValidControllers()?has_content>
	/**
	 * Whether the animation currently queued on this controller loops.
	 *
	 * <p>A controller must not be released at a looping animation's cycle boundary -
	 * doing so drops the pose for a frame before the next request restores it.
	 * thenPlay() queues LoopType.DEFAULT, so the baked animation's own loop type (the
	 * "loop" flag from the animation JSON) decides.
	 */
	private static boolean currentAnimationLoops(AnimationController<?> controller) {
		AnimationProcessor.QueuedAnimation queued = controller.getCurrentAnimation();
		if (queued == null)
			return false;
		Animation.LoopType loopType = queued.loopType();
		if (loopType == Animation.LoopType.DEFAULT && queued.animation() != null)
			loopType = queued.animation().loopType();
		return loopType == Animation.LoopType.LOOP;
	}
	</#if>

	<#-- One predicate per custom controller: start what a procedure requested, hold a
	     looping animation indefinitely, and release the controller once a one-shot has
	     finished so lower-priority controllers get their bones back. -->
	<#list data.getValidControllers() as ctrl>
	private PlayState controllerPredicate_${ctrl.name}(AnimationState event) {
		if (this.animation_${ctrl.name}.equals("empty")) {
			this.prevAnim_${ctrl.name} = "empty";
			return PlayState.STOP;
		}
		if (!this.animation_${ctrl.name}.equals(this.prevAnim_${ctrl.name})) {
			// New request - restart even when it is the same animation as before.
			event.getController().forceAnimationReset();
			event.getController().setAnimation(RawAnimation.begin().thenPlay(stripAnimationRequest(this.animation_${ctrl.name})));
			this.prevAnim_${ctrl.name} = this.animation_${ctrl.name};
			return PlayState.CONTINUE;
		}
		if (event.getController().getAnimationState() == AnimationController.State.STOPPED
				&& !currentAnimationLoops(event.getController())) {
			this.animation_${ctrl.name} = "empty";
			this.prevAnim_${ctrl.name} = "empty";
			event.getController().forceAnimationReset();
			return PlayState.STOP;
		}
		return PlayState.CONTINUE;
	}
	</#list>

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == ${data.deathTime}) {
			this.remove(${name}Entity.RemovalReason.KILLED);
			this.dropExperience(this);

	<#if hasProcedure(data.finishedDying)>
		<@procedureCode data.finishedDying, {
			"x": "this.getX()",
			"y": "this.getY()",
			"z": "this.getZ()",
			"entity": "this",
			"world": "this.level()"
		}/>
    	</#if>
		}
	}

	public String getSyncedAnimation() {
		return this.entityData.get(ANIMATION);
	}

	public void setAnimation(String animation) {
		this.entityData.set(ANIMATION, animation);
	}

	/**
	 * Moves whatever procedures wrote into the synched controller slots over to the
	 * fields the AnimationControllers read, the same way EntityAnimationFactory
	 * handles the built-in "procedure" controller.
	 */
	public void applySyncedControllerAnimations() {
		<#list data.getValidControllers() as ctrl>
		String synced_${ctrl.name} = this.entityData.get(ANIMATION_${ctrl.name?upper_case});
		if (!synced_${ctrl.name}.equals("undefined")) {
			this.entityData.set(ANIMATION_${ctrl.name?upper_case}, "undefined");
			this.animation_${ctrl.name} = synced_${ctrl.name};
		}
		</#list>
	}

	<#if data.getValidControllers()?has_content>
	/**
	 * Appends a counter so that requesting the animation a controller already holds
	 * still registers as a new request.
	 *
	 * <p>Without this, replaying the same animation would depend on the slot having been
	 * cleared when the previous run finished. That clearing happens in the animation
	 * predicate, which only runs while the entity is being rendered - so an entity that
	 * finished its animation off-screen would never play it again.
	 */
	private String tagAnimationRequest(String animation) {
		if (animation == null || animation.isBlank() || animation.equals("empty"))
			return "empty";
		return animation + ANIMATION_REQUEST_SEPARATOR + (++this.animationRequestCounter);
	}

	/** The animation name without the token added by {@link #tagAnimationRequest}. */
	private static String stripAnimationRequest(String animation) {
		int separator = animation.indexOf(ANIMATION_REQUEST_SEPARATOR);
		return separator < 0 ? animation : animation.substring(0, separator);
	}
	</#if>

	/**
	 * Plays an animation on a named controller. A blank or unknown controller name
	 * falls back to the built-in "procedure" controller, so procedures written
	 * before custom controllers existed keep behaving exactly as before.
	 */
	public void setControllerAnimation(String controller, String animation) {
		if (controller == null || controller.isBlank()) {
			this.setAnimation(animation);
			return;
		}
		switch (controller) {
			<#list data.getValidControllers() as ctrl>
			case "${ctrl.name}" -> this.entityData.set(ANIMATION_${ctrl.name?upper_case}, tagAnimationRequest(animation));
			</#list>
			default -> this.setAnimation(animation);
		}
	}

	/** Currently playing animation on a named controller, or "empty" if idle. */
	public String getControllerAnimation(String controller) {
		if (controller == null || controller.isBlank())
			return this.animationprocedure;
		return switch (controller) {
			<#list data.getValidControllers() as ctrl>
			case "${ctrl.name}" -> stripAnimationRequest(this.animation_${ctrl.name});
			</#list>
			default -> this.animationprocedure;
		};
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		<#-- GeckoLib 4.9.2 has no additiveAnimations() - true additive blending was only
		     added in GeckoLib 5, so there is deliberately no such call below. On this
		     generator the additive flag degrades to layering order only: a controller
		     registered later assigns over the bones its animation keyframes and leaves
		     every other bone to the controllers before it. Additive controllers are still
		     registered last so they win on shared bones.
		     The 26.1.2 template does support real additive blending. -->
		<#list data.getBaseControllers() as ctrl>
		data.add(new AnimationController<>(this, "${ctrl.name}", ${data.getTransitionTicks(ctrl)}, this::controllerPredicate_${ctrl.name}));
		</#list>
		data.add(new AnimationController<>(this, "movement", ${data.lerp}, this::movementPredicate));
		<#if data.enable4>
		data.add(new AnimationController<>(this, "attacking", ${data.lerp}, this::attackingPredicate));
		</#if>
                data.add(new AnimationController<>(this, "procedure", ${data.lerp}, this::procedurePredicate));
		<#list data.getAdditiveControllers() as ctrl>
		data.add(new AnimationController<>(this, "${ctrl.name}", ${data.getTransitionTicks(ctrl)}, this::controllerPredicate_${ctrl.name}));
		</#list>
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

}
<#-- @formatter:on -->