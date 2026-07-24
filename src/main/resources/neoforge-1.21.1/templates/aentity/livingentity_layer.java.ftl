<#--
 # Optional emissive overlay for a dedicated glow texture (not the main texture).
 # Prefer a separate glow map; do not assign the main body texture here.
 # Official auto-glow alternative: AutoGlowingGeoLayer + textures named *_glowing.png
 # https://github.com/bernie-g/geckolib/wiki/Emissive-Textures-Glow-Layer
-->
package ${package}.entity.layer;

<#assign glowTex = data.mobModelGlowTexture?replace(".png", "")>

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import ${package}.entity.${name}Entity;

public class ${name}Layer extends GeoRenderLayer<${name}Entity> {
	private static final ResourceLocation GLOW_TEXTURE = ResourceLocation.parse("${modid}:textures/entities/${glowTex}.png");

	public ${name}Layer(GeoRenderer<${name}Entity> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, ${name}Entity animatable, BakedGeoModel bakedModel, RenderType renderType,
			MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		RenderType glowRenderType = RenderType.eyes(GLOW_TEXTURE);
		getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, glowRenderType,
				bufferSource.getBuffer(glowRenderType), partialTick, LightTexture.FULL_SKY, OverlayTexture.NO_OVERLAY,
				getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt());
	}
}
