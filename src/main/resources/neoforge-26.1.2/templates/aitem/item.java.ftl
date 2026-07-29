<#-- @formatter:off -->
<#include "../procedures.java.ftl">
<#include "../mcitems.ftl">
<#include "../triggers.java.ftl">

package ${package}.item;

import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.util.GeckoLibUtil;

import ${package}.item.renderer.${name}ItemRenderer;

public class ${name}Item extends Item implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public String animationprocedure = "empty";

	public ${name}Item(Item.Properties properties) {
		super(properties
				<#if data.hasInventory()>
				.stacksTo(1)
				<#elseif data.damageCount != 0>
				.durability(${data.damageCount})
				<#else>
				.stacksTo(${data.stackSize})
				</#if>
				<#if data.immuneToFire>
				.fireResistant()
				</#if>
				<#if data.rarity != "COMMON">
				.rarity(Rarity.${data.rarity})
				</#if>
				<#if data.isFood>
				.food((new FoodProperties.Builder())
					.nutrition(${data.nutritionalValue})
					.saturationModifier(${data.saturation}f)
					<#if data.isAlwaysEdible>.alwaysEdible()</#if>
					.build())
				</#if>
				<#if data.enableMeleeDamage>
				.attributes(ItemAttributeModifiers.builder()
					.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ${data.damageVsEntity - 1},
							AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
					.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4,
							AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
					.build())
				</#if>
				<#if data.enchantability != 0>
				.enchantable(${data.enchantability})
				</#if>
				<#if data.stayInGridWhenCrafting && (!data.recipeRemainder?? || data.recipeRemainder.isEmpty()) && data.damageCount != 0>
				.setNoCombineRepair()
				</#if>
		);
		// Allow procedure-driven / synced item animations (GeckoLib 5 GeoItem)
		GeoItem.registerSyncedAnimatable(this);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	/**
	 * GeckoLib 5: supply GeoItemRenderer via GeoRenderProvider.
	 * Client item JSON (items/*.json) uses "type": "geckolib:geckolib" special renderer
	 * which resolves this provider at render time.
	 */
	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private ${name}ItemRenderer renderer;

			@Override
			public GeoItemRenderer<?> getGeoItemRenderer() {
				if (this.renderer == null)
					this.renderer = new ${name}ItemRenderer();
				return this.renderer;
			}
		});
	}

	<#if data.enableArmPose>
	public static final EnumProxy<HumanoidModel.ArmPose> ARM_POSE = new EnumProxy<>(HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
	    if (arm == HumanoidArm.LEFT) {
        <#list data.armPoseList as pose>
	        <#if pose.armHeld == "LEFT">
                model.${pose.arm?lower_case}Arm.${pose.angle?lower_case}Rot = <#if pose.swings>model.${pose.arm?lower_case}Arm.${pose.angle?lower_case}Rot +</#if> ${pose.rotation}F<#if pose.followsHead> + model.head.${pose.angle?lower_case}Rot</#if>;
            </#if>
        </#list>
        } else {
        <#list data.armPoseList as pose>
            <#if pose.armHeld == "RIGHT">
                model.${pose.arm?lower_case}Arm.${pose.angle?lower_case}Rot = <#if pose.swings>model.${pose.arm?lower_case}Arm.${pose.angle?lower_case}Rot +</#if> ${pose.rotation}F<#if pose.followsHead> + model.head.${pose.angle?lower_case}Rot</#if>;
            </#if>
        </#list>
        }
    });
	</#if>

	private PlayState idlePredicate(AnimationTest<${name}Item> event) {
		if (this.animationprocedure.equals("empty")) {
			event.controller().setAnimation(RawAnimation.begin().thenLoop("${data.idle}"));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	String prevAnim = "empty";
	private PlayState procedurePredicate(AnimationTest<${name}Item> event) {
		if (!this.animationprocedure.equals("empty") && event.controller().hasAnimationFinished() || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
			if (!this.animationprocedure.equals(prevAnim))
				event.controller().reset();
			event.controller().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (event.controller().hasAnimationFinished()) {
				this.animationprocedure = "empty";
				event.controller().reset();
			}
		} else if (this.animationprocedure.equals("empty")) {
			prevAnim = "empty";
			return PlayState.STOP;
		}
		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>("procedureController", 0, this::procedurePredicate));
		data.add(new AnimationController<>("idleController", 0, this::idlePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	<#if data.hasNonDefaultAnimation()>
	@Override public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
		return ItemUseAnimation.${data.animation?upper_case};
	}
	</#if>

	<#if data.stayInGridWhenCrafting>
		<#if data.recipeRemainder?? && !data.recipeRemainder.isEmpty()>
			@Override public ItemStackTemplate getCraftingRemainder(ItemInstance itemInstance) {
				return new ItemStackTemplate(${mappedMCItemToItem(data.recipeRemainder)});
			}
		<#elseif data.damageOnCrafting && data.damageCount != 0>
			@Override public ItemStackTemplate getCraftingRemainder(ItemInstance itemInstance) {
				ItemStack retval = new ItemStack(this);
				retval.setDamageValue(itemInstance.getOrDefault(DataComponents.DAMAGE, 0) + 1);
				if (retval.getDamageValue() >= retval.getMaxDamage()) {
					return null;
				}
				return ItemStackTemplate.fromNonEmptyStack(retval);
			}
		<#else>
			@Override public ItemStackTemplate getCraftingRemainder(ItemInstance itemInstance) {
				return new ItemStackTemplate(this);
			}
		</#if>
	</#if>

	<#if (!data.isFood && data.useDuration != 0) || (data.isFood && data.useDuration != 32)>
	@Override public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
		return ${data.useDuration};
	}
	</#if>

	<#if data.toolType != 1>
	@Override public float getDestroySpeed(ItemStack itemstack, BlockState state) {
		return ${data.toolType}f;
	}
	</#if>

	<#if data.hasGlow>
	<@hasGlow data.glowCondition/>
	</#if>

	<#if data.destroyAnyBlock>
	@Override public boolean isCorrectToolForDrops(ItemStack itemstack, BlockState state) {
		return true;
	}
	</#if>

	<@addSpecialInformation data.specialInformation, "item." + modid + "." + registryname/>

	<#assign shouldExplicitlyCallStartUsing = !data.isFood && (data.useDuration > 0)>
	<#if hasProcedure(data.onRightClickedInAir) || data.hasInventory() || (hasProcedure(data.onStoppedUsing) && shouldExplicitlyCallStartUsing)>
	@Override public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		InteractionResult ar = super.use(world, entity, hand);
		ItemStack itemstack = entity.getItemInHand(hand);

		<#if hasProcedure(data.onStoppedUsing) && shouldExplicitlyCallStartUsing>
		entity.startUsingItem(hand);
		</#if>

		<#if data.hasInventory()>
		if (entity instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(new MenuProvider() {
				@Override public Component getDisplayName() {
					return Component.literal("${data.name}");
				}

				@Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
					packetBuffer.writeBlockPos(entity.blockPosition());
					packetBuffer.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
					return new ${data.guiBoundTo}Menu(id, inventory, packetBuffer);
				}
			}, buf -> {
				buf.writeBlockPos(entity.blockPosition());
				buf.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
			});
		}
		</#if>

		<#if hasProcedure(data.onRightClickedInAir)>
			<@procedureCode data.onRightClickedInAir, {
				"x": "entity.getX()",
				"y": "entity.getY()",
				"z": "entity.getZ()",
				"world": "world",
				"entity": "entity",
				"itemstack": "itemstack"
			}/>
		</#if>
		return ar;
	}
	</#if>

	<#if hasProcedure(data.onFinishUsingItem) || data.hasEatResultItem()>
		@Override public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
			ItemStack retval =
				<#if data.hasEatResultItem()>
					${mappedMCItemToItemStackCode(data.eatResultItem, 1)};
				<#else>
					itemstack;
				</#if>
			super.finishUsingItem(itemstack, world, entity);

			<#if hasProcedure(data.onFinishUsingItem)>
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				<@procedureOBJToCode data.onFinishUsingItem/>
			</#if>

			<#if data.hasEatResultItem()>
				if (itemstack.isEmpty()) {
					return retval;
				} else {
					if (entity instanceof Player player && !player.getAbilities().instabuild) {
						if (!player.getInventory().add(retval))
							player.drop(retval, false);
					}
					return itemstack;
				}
			<#else>
				return retval;
			</#if>
		}
	</#if>

	<@onItemUsedOnBlock data.onRightClickedOnBlock/>

	<@onEntityHitWith data.onEntityHitWith, (data.damageCount != 0 && data.enableMeleeDamage), 1/>

	<@onEntitySwing data.onEntitySwing/>

	<@onCrafted data.onCrafted/>

	<@onItemTick data.onItemInUseTick, data.onItemInInventoryTick/>

	<@onDroppedByPlayer data.onDroppedByPlayer/>

	<#if hasProcedure(data.onStoppedUsing)>
		@Override public boolean releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
			<@procedureCode data.onStoppedUsing, {
				"x": "entity.getX()",
				"y": "entity.getY()",
				"z": "entity.getZ()",
				"world": "world",
				"entity": "entity",
				"itemstack": "itemstack",
				"time": "time"
			}/>
			return super.releaseUsing(itemstack, world, entity, time);
		}
	</#if>

}
<#-- @formatter:on -->
