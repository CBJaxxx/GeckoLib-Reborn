<#-- @formatter:off -->
<#include "../procedures.java.ftl">

package ${package}.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import ${package}.entity.${name}Entity;
import ${package}.entity.model.${name}Model;
<#if data.mobModelGlowTexture?has_content && data.mobModelGlowTexture != data.mobModelTexture>
import ${package}.entity.layer.${name}Layer;
</#if>

public class ${name}Renderer extends GeoEntityRenderer<${name}Entity> {
	public ${name}Renderer(EntityRendererProvider.Context context) {
		super(context, new ${name}Model());
		this.shadowRadius = ${data.modelShadowSize}f;
		<#if data.mobModelGlowTexture?has_content && data.mobModelGlowTexture != data.mobModelTexture>
		this.addRenderLayer(new ${name}Layer(this));
		</#if>
	}

	@Override
	public void preRender(PoseStack poseStack, ${name}Entity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		// Hide Blockbench utility bones so they are never drawn as geometry
		model.getBone("hitbox").ifPresent(bone -> bone.setHidden(true));
		model.getBone("tag_name").ifPresent(bone -> bone.setHidden(true));

		<#assign needsScale = data.visualScale?? && (hasProcedure(data.visualScale) || data.visualScale.getFixedValue() != 1)>
		<#if needsScale>
		<#if hasProcedure(data.visualScale)>
		Level world = animatable.level();
		double x = animatable.getX();
		double y = animatable.getY();
		double z = animatable.getZ();
		float scale = (float) <@procedureOBJToNumberCode data.visualScale/>;
		<#else>
		float scale = ${data.visualScale.getFixedValue()}f;
		</#if>
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		</#if>

		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}

	<#if data.disableDeathRotation>
	@Override
	protected float getDeathMaxRotation(${name}Entity animatable) {
		return 0.0F;
	}
	</#if>
}
<#-- @formatter:on -->
