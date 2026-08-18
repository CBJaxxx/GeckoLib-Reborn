<#-- @formatter:off -->

/*
 *    MCreator note: This file will be REGENERATED on each build.
 */

package ${package}.init;

<#assign specialentities = w.getGElementsOfType("specialentity")>
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) public class ${JavaModName}Entities {

	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ${JavaModName}.MODID);

	<#assign hasLivingEntities = false>

	<#list entities as entity>
		<#if entity.getModElement().getTypeString() == "projectile">
			public static final RegistryObject<EntityType<${entity.getModElement().getName()}Entity>> ${entity.getModElement().getRegistryNameUpper()} =
				register("${entity.getModElement().getRegistryName()}", EntityType.Builder.<${entity.getModElement().getName()}Entity>
						of(${entity.getModElement().getName()}Entity::new, MobCategory.MISC).setCustomClientFactory(${entity.getModElement().getName()}Entity::new)
						.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(${entity.modelWidth}f, ${entity.modelHeight}f));
		<#elseif entity.getModElement().getTypeString() == "livingentity" || entity.getModElement().getTypeString() == "animatedentity">
			<#assign hasLivingEntities = true>

			public static final RegistryObject<EntityType<${entity.getModElement().getName()}Entity>> ${entity.getModElement().getRegistryNameUpper()} =
				register("${entity.getModElement().getRegistryName()}", EntityType.Builder.<${entity.getModElement().getName()}Entity>
						of(${entity.getModElement().getName()}Entity::new, ${generator.map(entity.mobSpawningType, "mobspawntypes")})
							.setShouldReceiveVelocityUpdates(true).setTrackingRange(${entity.trackingRange}).setUpdateInterval(3)
							.setCustomClientFactory(${entity.getModElement().getName()}Entity::new)
							<#if entity.immuneToFire>.fireImmune()</#if>
							.sized(${entity.modelWidth}f, ${entity.modelHeight}f)
						);
			<#if entity.hasCustomProjectile()>
			public static final RegistryObject<EntityType<${entity.getModElement().getName()}EntityProjectile>> ${entity.getModElement().getRegistryNameUpper()}_PROJECTILE =
				register("projectile_${entity.getModElement().getRegistryName()}", EntityType.Builder.<${entity.getModElement().getName()}EntityProjectile>
					of(${entity.getModElement().getName()}EntityProjectile::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
						.setUpdateInterval(1).setCustomClientFactory(${entity.getModElement().getName()}EntityProjectile::new).sized(0.5f, 0.5f));
			</#if>
		</#if>
	</#list>

	<#if specialentities?size != 0>
		<#if specialentities?filter(e -> !e.isBoatChestVariant())?size != 0>
			public static final RegistryObject<EntityType<${JavaModName}Boat>> ${JavaModName?upper_case}_BOAT =
				register("boat", EntityType.Builder.<${JavaModName}Boat>
					of(${JavaModName}Boat::new, MobCategory.MISC).sized(1.375f, 0.5625f).clientTrackingRange(10));
		</#if>
		<#if specialentities?filter(e -> e.isBoatChestVariant())?size != 0>
			public static final RegistryObject<EntityType<${JavaModName}ChestBoat>> ${JavaModName?upper_case}_CHEST_BOAT =
				register("chest_boat", EntityType.Builder.<${JavaModName}ChestBoat>
					of(${JavaModName}ChestBoat::new, MobCategory.MISC).sized(1.375f, 0.5625f).clientTrackingRange(10));
		</#if>
	</#if>

	// Start of user code block custom entities
	// End of user code block custom entities

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	<#if hasLivingEntities>
	@SubscribeEvent public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		<#list entities as entity>
			<#if entity.getModElement().getTypeString() == "livingentity" || entity.getModElement().getTypeString() == "animatedentity">
				${entity.getModElement().getName()}Entity.init();
			</#if>
		</#list>
		});
	}

	@SubscribeEvent public static void registerAttributes(EntityAttributeCreationEvent event) {
		<#list entities as entity>
			<#if entity.getModElement().getTypeString() == "livingentity" || entity.getModElement().getTypeString() == "animatedentity">
				event.put(${entity.getModElement().getRegistryNameUpper()}.get(), ${entity.getModElement().getName()}Entity.createAttributes().build());
			</#if>
		</#list>
	}
	</#if>

}
<#-- @formatter:on -->
