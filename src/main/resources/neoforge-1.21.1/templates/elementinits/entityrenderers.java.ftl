<#-- @formatter:off -->
package ${package}.init;

@EventBusSubscriber(Dist.CLIENT)
public class ${JavaModName}EntityRenderers {

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		<#list entities as entity>
			<#if entity.getModElement().getTypeString() == "projectile">
				<#if entity.isCustomModel()>
		event.registerEntityRenderer(${JavaModName}Entities.${entity.getModElement().getRegistryNameUpper()}.get(), ${entity.getModElement().getName()}Renderer::new);
				<#else>
		event.registerEntityRenderer(${JavaModName}Entities.${entity.getModElement().getRegistryNameUpper()}.get(), ThrownItemRenderer::new);
				</#if>
			<#elseif entity.getModElement().getTypeString() == "livingentity">
		event.registerEntityRenderer(${JavaModName}Entities.${entity.getModElement().getRegistryNameUpper()}.get(), ${entity.getModElement().getName()}Renderer::new);
				<#if entity.hasCustomProjectile()>
		event.registerEntityRenderer(${JavaModName}Entities.${entity.getModElement().getRegistryNameUpper()}_PROJECTILE.get(), ThrownItemRenderer::new);
				</#if>
			</#if>
		</#list>
	}
}
<#-- @formatter:on -->
