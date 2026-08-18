<#-- @formatter:off -->
package ${package}.init;

<#assign hasLivingEntities = w.hasElementsOfType("livingentity") || w.hasElementsOfType("animatedentity")>
<#assign entitiesWithInventory = w.getGElementsOfType("livingentity")?filter(e -> e.guiBoundTo?has_content)>
<#assign animatedEntitiesWithInventory = w.getGElementsOfType("animatedentity")?filter(e -> e.guiBoundTo?has_content && e.guiBoundTo != "<NONE>")>

<#if hasLivingEntities || entitiesWithInventory?size != 0 || animatedEntitiesWithInventory?size != 0>
@EventBusSubscriber
</#if>
public class ${JavaModName}Entities {

	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, ${JavaModName}.MODID);

	<#list entities as entity>
		<#if entity.getModElement().getTypeString() == "projectile">
			public static final DeferredHolder<EntityType<?>, EntityType<${entity.getModElement().getName()}Entity>> ${entity.getModElement().getRegistryNameUpper()} =
				register("${entity.getModElement().getRegistryName()}", EntityType.Builder.<${entity.getModElement().getName()}Entity>
						of(${entity.getModElement().getName()}Entity::new, MobCategory.MISC)
						.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(${entity.modelWidth}f, ${entity.modelHeight}f));
		<#elseif entity.getModElement().getTypeString() == "livingentity" || entity.getModElement().getTypeString() == "animatedentity">
			public static final DeferredHolder<EntityType<?>, EntityType<${entity.getModElement().getName()}Entity>> ${entity.getModElement().getRegistryNameUpper()} =
				register("${entity.getModElement().getRegistryName()}", EntityType.Builder.<${entity.getModElement().getName()}Entity>
						of(${entity.getModElement().getName()}Entity::new, ${generator.map(entity.mobSpawningType, "mobspawntypes")})
							.setShouldReceiveVelocityUpdates(true).setTrackingRange(${entity.trackingRange}).setUpdateInterval(3)
							 <#if entity.immuneToFire>.fireImmune()</#if>
                             <#if entity.getModElement().getTypeString() == "livingentity" && (entity.mobModelName)?? && entity.mobModelName == "Biped">.ridingOffset(-0.6f)</#if>
                             <#if (entity.mobBehaviourType)?? && entity.mobBehaviourType != "Creature">.notInPeaceful()</#if>
                             .sized(${entity.modelWidth}f, ${entity.modelHeight}f)
                             <#if entity.getModElement().getTypeString() == "animatedentity" && entity.eyeHeight>.eyeHeight(${entity.height}f)</#if>
						);
			<#if entity.hasCustomProjectile()>
			public static final DeferredHolder<EntityType<?>, EntityType<${entity.getModElement().getName()}EntityProjectile>> ${entity.getModElement().getRegistryNameUpper()}_PROJECTILE =
				register("projectile_${entity.getModElement().getRegistryName()}", EntityType.Builder.<${entity.getModElement().getName()}EntityProjectile>
					of(${entity.getModElement().getName()}EntityProjectile::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
						.setUpdateInterval(1).sized(0.5f, 0.5f));
			</#if>
        <#elseif entity.getModElement().getTypeString() == "specialentity">
            public static final DeferredHolder<EntityType<?>, EntityType<${entity.getModElement().getName()}Entity>> ${entity.getModElement().getRegistryNameUpper()} =
                register("${entity.getModElement().getRegistryName()}",
                EntityType.Builder.<${entity.getModElement().getName()}Entity>of(${entity.getModElement().getName()}Entity::new, MobCategory.MISC)
                    .noLootTable().sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10));
        </#if>
	</#list>

	// Start of user code block custom entities
	// End of user code block custom entities

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(
				ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(${JavaModName}.MODID, registryname))
		));
	}

	<#if entitiesWithInventory?size != 0 || animatedEntitiesWithInventory?size != 0>
	<#compress>
	<#-- #4780: entities have equipment inventory capability registered before custom ones without priority set -->
	@SubscribeEvent(priority = EventPriority.HIGHEST) public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		<#list entitiesWithInventory as entity>
			event.registerEntity(Capabilities.Item.ENTITY, ${entity.getModElement().getRegistryNameUpper()}.get(), (living, context) -> living.getCombinedInventory());
		</#list>
		<#list animatedEntitiesWithInventory as entity>
            event.registerEntity(Capabilities.Item.ENTITY, ${entity.getModElement().getRegistryNameUpper()}.get(), (living, context) -> living.getCombinedInventory());
        </#list>
	}
	</#compress>
	</#if>

	<#if hasLivingEntities>
	@SubscribeEvent public static void init(RegisterSpawnPlacementsEvent event) {
		<#list entities as entity>
			<#assign typestring = entity.getModElement().getTypeString()>
            <#if typestring == "livingentity" || typestring == "animatedentity">
				${entity.getModElement().getName()}Entity.init(event);
			</#if>
		</#list>
	}

	@SubscribeEvent public static void registerAttributes(EntityAttributeCreationEvent event) {
		<#list entities as entity>
			<#assign typestring = entity.getModElement().getTypeString()>
            <#if typestring == "livingentity" || typestring == "animatedentity">
				event.put(${entity.getModElement().getRegistryNameUpper()}.get(), ${entity.getModElement().getName()}Entity.createAttributes().build());
			</#if>
		</#list>
	}
	</#if>

}
<#-- @formatter:on -->