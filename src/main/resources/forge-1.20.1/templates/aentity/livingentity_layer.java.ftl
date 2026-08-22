package ${package}.entity.layer;

<#assign glowTex = data.mobModelGlowTexture?replace(".png", "")>

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import ${package}.entity.${name}Entity;

public class ${name}Layer extends GeoRenderLayer<${name}Entity> {
	private static final ResourceLocation GLOW_TEXTURE = ResourceLocation.fromNamespaceAndPath("${modid}", "textures/entities/${glowTex}.png");

	public ${name}Layer(GeoRenderer<${name}Entity> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, ${name}Entity animatable, BakedGeoModel bakedModel, RenderType renderType,
			MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		RenderType glowRenderType = RenderType.eyes(GLOW_TEXTURE);
		getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, glowRenderType,
				bufferSource.getBuffer(glowRenderType), partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
				1, 1, 1, 1);
	}
}
