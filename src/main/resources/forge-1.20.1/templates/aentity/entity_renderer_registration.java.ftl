package ${package}.client;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import ${package}.init.${JavaModName}Entities;
<#list animatedentitys as entity>
import ${package}.client.renderer.${entity.getModElement().getName()}Renderer;
</#list>

@Mod.EventBusSubscriber(modid = ${JavaModName}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
