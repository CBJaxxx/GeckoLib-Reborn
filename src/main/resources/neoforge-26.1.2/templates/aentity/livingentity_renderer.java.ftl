<#-- @formatter:off -->
<#include "../procedures.java.ftl">

package ${package}.client.renderer;

import com.geckolib.constant.DefaultAnimations;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.level.Level;

import ${package}.entity.${name}Entity;
import ${package}.entity.model.${name}Model;
<#if data.mobModelGlowTexture?has_content && data.mobModelGlowTexture != data.mobModelTexture>
import ${package}.entity.layer.${name}Layer;
</#if>

public class ${name}Renderer extends GeoEntityRenderer<${name}Entity, LivingEntityRenderState> {
	public static final DataTicket<Float> VISUAL_SCALE = DataTicket.create("${modid}_${registryname}_visual_scale", Float.class);

	public ${name}Renderer(EntityRendererProvider.Context context) {
		super(context, new ${name}Model());
		this.shadowRadius = ${data.modelShadowSize}f;
		<#if data.mobModelGlowTexture?has_content && data.mobModelGlowTexture != data.mobModelTexture>
		this.withRenderLayer(new ${name}Layer(this));
		</#if>
	}


	@Override
    public void adjustModelBonesForRender(RenderPassInfo<LivingEntityRenderState> renderPassInfo, BoneSnapshots snapshots) {
        // Hide Blockbench utility bones (same as before)
        snapshots.ifPresent("hitbox", snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
        snapshots.ifPresent("tag_name", snapshot -> snapshot.skipRender(true).skipChildrenRender(true));

        // The head rotation is now done with this render helper to target the bone
        <#if data.headMovement>
        DefaultAnimations.hardcodedHeadRotation(renderPassInfo, snapshots, "${data.groupName}");
        </#if>
    }

	<#assign needsScale = data.visualScale?? && (hasProcedure(data.visualScale) || data.visualScale.getFixedValue() != 1)>
	<#if needsScale>
	@Override
	public void addRenderData(${name}Entity animatable, Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
		<#if hasProcedure(data.visualScale)>
		Level world = animatable.level();
		double x = animatable.getX();
		double y = animatable.getY();
		double z = animatable.getZ();
		float scale = (float) <@procedureOBJToNumberCode data.visualScale/>;
		<#else>
		float scale = ${data.visualScale.getFixedValue()}f;
		</#if>
		renderState.addGeckolibData(VISUAL_SCALE, scale);
	}

	@Override
	public void scaleModelForRender(RenderPassInfo<LivingEntityRenderState> renderPassInfo, float widthScale, float heightScale) {
		float scale = renderPassInfo.renderState().getOrDefaultGeckolibData(VISUAL_SCALE, 1f);
		super.scaleModelForRender(renderPassInfo, widthScale * scale, heightScale * scale);
	}
	</#if>

	<#if data.disableDeathRotation>
	@Override
	protected float getDeathMaxRotation(GeoRenderState renderState) {
		return 0.0F;
	}
	</#if>
}
<#-- @formatter:on -->
