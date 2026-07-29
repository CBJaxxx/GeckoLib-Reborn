package ${package}.client;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import ${package}.init.${JavaModName}Entities;
<#list animatedentitys as entity>
import ${package}.client.renderer.${entity.getModElement().getName()}Renderer;
</#list>

@EventBusSubscriber(Dist.CLIENT)
public class AnimatedEntityRenderers {

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		<#list animatedentitys as entity>
		event.registerEntityRenderer(${JavaModName}Entities.${entity.getModElement().getRegistryNameUpper()}.get(),
				${entity.getModElement().getName()}Renderer::new);
		<#if entity.hasCustomProjectile()>
		event.registerEntityRenderer(${JavaModName}Entities.${entity.getModElement().getRegistryNameUpper()}_PROJECTILE.get(),
				ThrownItemRenderer::new);
		</#if>
		</#list>
	}
}
