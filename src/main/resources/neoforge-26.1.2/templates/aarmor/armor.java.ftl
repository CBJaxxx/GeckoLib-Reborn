<#-- @formatter:off -->
<#include "../mcitems.ftl">
<#include "../procedures.java.ftl">

package ${package}.item;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.util.GeckoLibUtil;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import ${package}.client.renderer.${name}ArmorRenderer;

public abstract class ${name}Item extends Item implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public String animationprocedure = "empty";

	public static final ArmorMaterial ARMOR_MATERIAL = new ArmorMaterial(
		${data.maxDamage},
		Map.of(
			ArmorType.BOOTS, ${data.damageValueBoots},
			ArmorType.LEGGINGS, ${data.damageValueLeggings},
			ArmorType.CHESTPLATE, ${data.damageValueBody},
			ArmorType.HELMET, ${data.damageValueHelmet},
			ArmorType.BODY, ${data.damageValueBody}
		),
		${data.enchantability},
		<#if data.equipSound?has_content && data.equipSound.getUnmappedValue()?has_content>
		DeferredHolder.create(Registries.SOUND_EVENT, Identifier.parse("${data.equipSound}")),
		<#else>
		BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY),
		</#if>
		${data.toughness}f,
		${data.knockbackResistance}f,
		TagKey.create(Registries.ITEM, Identifier.parse("${modid}:${registryname}_repair_items")),
		ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("${modid}:${registryname}"))
	);

	private ${name}Item(Item.Properties properties) {
		super(properties);
		GeoItem.registerSyncedAnimatable(this);
	}

	<#if data.enableHelmet>
	public static class Helmet extends ${name}Item {
		public Helmet(Item.Properties properties) {
			super(properties<#if data.helmetImmuneToFire>.fireResistant()</#if>.humanoidArmor(ARMOR_MATERIAL, ArmorType.HELMET));
		}

		<#if data.helmetSpecialInfo?has_content>
		@Override
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, tooltipDisplay, tooltip, flag);
			<#list data.helmetSpecialInfo as entry>
			tooltip.accept(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
	}
	</#if>

	<#if data.enableBody>
	public static class Chestplate extends ${name}Item {
		public Chestplate(Item.Properties properties) {
			super(properties<#if data.bodyImmuneToFire>.fireResistant()</#if>.humanoidArmor(ARMOR_MATERIAL, ArmorType.CHESTPLATE));
		}

		<#if data.bodySpecialInfo?has_content>
		@Override
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, tooltipDisplay, tooltip, flag);
			<#list data.bodySpecialInfo as entry>
			tooltip.accept(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
	}
	</#if>

	<#if data.enableLeggings>
	public static class Leggings extends ${name}Item {
		public Leggings(Item.Properties properties) {
			super(properties<#if data.leggingsImmuneToFire>.fireResistant()</#if>.humanoidArmor(ARMOR_MATERIAL, ArmorType.LEGGINGS));
		}

		<#if data.leggingsSpecialInfo?has_content>
		@Override
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, tooltipDisplay, tooltip, flag);
			<#list data.leggingsSpecialInfo as entry>
			tooltip.accept(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
	}
	</#if>

	<#if data.enableBoots>
	public static class Boots extends ${name}Item {
		public Boots(Item.Properties properties) {
			super(properties<#if data.bootsImmuneToFire>.fireResistant()</#if>.humanoidArmor(ARMOR_MATERIAL, ArmorType.BOOTS));
		}

		<#if data.bootsSpecialInfo?has_content>
		@Override
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, tooltipDisplay, tooltip, flag);
			<#list data.bootsSpecialInfo as entry>
			tooltip.accept(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
	}
	</#if>

	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private ${name}ArmorRenderer renderer;

			@Override
			public GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
				if (this.renderer == null)
					this.renderer = new ${name}ArmorRenderer();
				return this.renderer;
			}
		});
	}

	private static final String IDLE_ANIMATION = "${data.idle!""}";

	private PlayState predicate(AnimationTest<${name}Item> event) {
		if (!this.animationprocedure.equals("empty"))
			return PlayState.STOP;

		// No idle name configured — render static armor (limb poses still apply)
		if (IDLE_ANIMATION == null || IDLE_ANIMATION.isBlank())
			return PlayState.STOP;

		event.controller().setAnimation(RawAnimation.begin().thenLoop(IDLE_ANIMATION));

		LivingEntity entity = event.getData(${name}ArmorRenderer.WEARER);

		if (entity instanceof ArmorStand) {
			return PlayState.CONTINUE;
		}

		<#if data.fullyEquipped>
		Set<Item> wornArmor = new ObjectOpenHashSet<>();

		if (entity != null) {
			for (ItemStack stack : entity.getArmorSlots()) {
				if (stack.isEmpty())
					return PlayState.STOP;

				wornArmor.add(stack.getItem());
			}
		} else {
			return PlayState.STOP;
		}

		boolean isWearingAll = wornArmor.containsAll(ObjectArrayList.of(
		<#if data.enableBoots>
		${JavaModName}Items.${(registryname)?upper_case}_BOOTS.get()
		</#if><#if data.enableBoots && (data.enableLeggings || data.enableBody || data.enableHelmet)>,</#if>
		<#if data.enableLeggings>
		${JavaModName}Items.${(registryname)?upper_case}_LEGGINGS.get()
		</#if><#if data.enableLeggings && (data.enableBody || data.enableHelmet)>,</#if>
		<#if data.enableBody>
		${JavaModName}Items.${(registryname)?upper_case}_CHESTPLATE.get()
		</#if><#if data.enableBody && data.enableHelmet>,</#if>
		<#if data.enableHelmet>
		${JavaModName}Items.${(registryname)?upper_case}_HELMET.get()
		</#if>));
		return isWearingAll ? PlayState.CONTINUE : PlayState.STOP;
		<#else>
		return PlayState.CONTINUE;
		</#if>
	}

	String prevAnim = "empty";
	private PlayState procedurePredicate(AnimationTest<${name}Item> event) {
		// Guard blank procedure names — same GeckoLib empty-animation crash
		if (this.animationprocedure == null || this.animationprocedure.isBlank() || this.animationprocedure.equals("empty")) {
			prevAnim = "empty";
			return PlayState.STOP;
		}

		boolean shouldPlay = event.controller().hasAnimationFinished()
				|| !this.animationprocedure.equals(prevAnim);

		if (shouldPlay) {
			if (!this.animationprocedure.equals(prevAnim))
				event.controller().reset();
			event.controller().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (event.controller().hasAnimationFinished()) {
				this.animationprocedure = "empty";
				event.controller().reset();
			}

			LivingEntity entity = event.getData(${name}ArmorRenderer.WEARER);

			if (entity instanceof ArmorStand) {
				prevAnim = this.animationprocedure;
				return PlayState.CONTINUE;
			}

			<#if data.fullyEquipped>
			Set<Item> wornArmor = new ObjectOpenHashSet<>();

			if (entity != null) {
				for (ItemStack stack : entity.getArmorSlots()) {
					if (stack.isEmpty())
						return PlayState.STOP;

					wornArmor.add(stack.getItem());
				}
			} else {
				return PlayState.STOP;
			}

			boolean isWearingAll = wornArmor.containsAll(ObjectArrayList.of(
			<#if data.enableBoots>
			${JavaModName}Items.${(registryname)?upper_case}_BOOTS.get()
			</#if><#if data.enableBoots && (data.enableLeggings || data.enableBody || data.enableHelmet)>,</#if>
			<#if data.enableLeggings>
			${JavaModName}Items.${(registryname)?upper_case}_LEGGINGS.get()
			</#if><#if data.enableLeggings && (data.enableBody || data.enableHelmet)>,</#if>
			<#if data.enableBody>
			${JavaModName}Items.${(registryname)?upper_case}_CHESTPLATE.get()
			</#if><#if data.enableBody && data.enableHelmet>,</#if>
			<#if data.enableHelmet>
			${JavaModName}Items.${(registryname)?upper_case}_HELMET.get()
			</#if>));
			prevAnim = this.animationprocedure;
			return isWearingAll ? PlayState.CONTINUE : PlayState.STOP;
			<#else>
			prevAnim = this.animationprocedure;
			return PlayState.CONTINUE;
			</#if>
		}

		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		// Transition 0: if an animation name is missing, GeckoLib returns null timeline
		// instead of crashing in AnimationTimeline.create (getLast on empty stages).
		data.add(new AnimationController<>("controller", 0, this::predicate));
		data.add(new AnimationController<>("procedureController", 0, this::procedurePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
<#-- @formatter:on -->
