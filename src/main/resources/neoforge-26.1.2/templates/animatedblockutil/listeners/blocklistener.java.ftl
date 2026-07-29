package ${package}.block.listener;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import ${package}.init.${JavaModName}BlockEntities;
<#list animatedblocks as ablock>
import ${package}.block.renderer.${ablock.getModElement().getName()}TileRenderer;
</#list>

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientListener {

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		<#list animatedblocks as ablock>
		event.registerBlockEntityRenderer(${JavaModName}BlockEntities.${ablock.getModElement().getRegistryNameUpper()}.get(),
				${ablock.getModElement().getName()}TileRenderer::new);
		</#list>
	}

}
