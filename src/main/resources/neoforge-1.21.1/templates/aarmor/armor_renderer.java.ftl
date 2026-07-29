package ${package}.client.renderer;

import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import ${package}.item.model.${name}Model;
import ${package}.item.${name}Item;

import javax.annotation.Nullable;

public class ${name}ArmorRenderer extends GeoArmorRenderer<${name}Item> {
	public ${name}ArmorRenderer() {
		super(new ${name}Model());
	}

	<#-- Only override bone getters when custom names differ from GeckoLib defaults -->
	<#if data.head != "armorHead">
	@Override
	public GeoBone getHeadBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.head}").orElse(null);
	}
	</#if>
	<#if data.chest != "armorBody">
	@Override
	public GeoBone getBodyBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.chest}").orElse(null);
	}
	</#if>
	<#if data.rightArm != "armorRightArm">
	@Override
	public GeoBone getRightArmBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.rightArm}").orElse(null);
	}
	</#if>
	<#if data.leftArm != "armorLeftArm">
	@Override
	public GeoBone getLeftArmBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.leftArm}").orElse(null);
	}
	</#if>
	<#if data.rightLeg != "armorRightLeg">
	@Override
	public GeoBone getRightLegBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.rightLeg}").orElse(null);
	}
	</#if>
	<#if data.leftLeg != "armorLeftLeg">
	@Override
	public GeoBone getLeftLegBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.leftLeg}").orElse(null);
	}
	</#if>
	<#if data.rightBoot != "armorRightBoot">
	@Override
	public GeoBone getRightBootBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.rightBoot}").orElse(null);
	}
	</#if>
	<#if data.leftBoot != "armorLeftBoot">
	@Override
	public GeoBone getLeftBootBone(GeoModel<${name}Item> model) {
		return model.getBone("${data.leftBoot}").orElse(null);
	}
	</#if>

	/**
	 * Match player limb poses onto geo bones. Prefer biped* parents from the
	 * Blockbench armor template so animations on armor* children do not wipe
	 * head look / arm swing.
	 */
	@Override
	protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
		GeoModel<${name}Item> model = getGeoModel();

		GeoBone headBone = resolvePoseBone(model, "bipedHead", this.head);
		if (headBone != null) {
			ModelPart headPart = baseModel.head;
			RenderUtil.matchModelPartRot(headPart, headBone);
			headBone.updatePosition(headPart.x, -headPart.y, headPart.z);
		}

		GeoBone bodyBone = resolvePoseBone(model, "bipedBody", this.body);
		if (bodyBone != null) {
			ModelPart bodyPart = baseModel.body;
			RenderUtil.matchModelPartRot(bodyPart, bodyBone);
			bodyBone.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
		}

		GeoBone rightArmBone = resolvePoseBone(model, "bipedRightArm", this.rightArm);
		if (rightArmBone != null) {
			ModelPart rightArmPart = baseModel.rightArm;
			RenderUtil.matchModelPartRot(rightArmPart, rightArmBone);
			rightArmBone.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
		}

		GeoBone leftArmBone = resolvePoseBone(model, "bipedLeftArm", this.leftArm);
		if (leftArmBone != null) {
			ModelPart leftArmPart = baseModel.leftArm;
			RenderUtil.matchModelPartRot(leftArmPart, leftArmBone);
			leftArmBone.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
		}

		GeoBone rightLegBone = resolvePoseBone(model, "bipedRightLeg", this.rightLeg);
		if (rightLegBone != null) {
			ModelPart rightLegPart = baseModel.rightLeg;
			RenderUtil.matchModelPartRot(rightLegPart, rightLegBone);
			rightLegBone.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);

			// Only pose boots separately when not using a shared biped* parent
			// (boots are siblings of armor* legs under biped*Left/RightLeg).
			if (this.rightBoot != null && rightLegBone == this.rightLeg) {
				RenderUtil.matchModelPartRot(rightLegPart, this.rightBoot);
				this.rightBoot.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);
			}
		}

		GeoBone leftLegBone = resolvePoseBone(model, "bipedLeftLeg", this.leftLeg);
		if (leftLegBone != null) {
			ModelPart leftLegPart = baseModel.leftLeg;
			RenderUtil.matchModelPartRot(leftLegPart, leftLegBone);
			leftLegBone.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);

			if (this.leftBoot != null && leftLegBone == this.leftLeg) {
				RenderUtil.matchModelPartRot(leftLegPart, this.leftBoot);
				this.leftBoot.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);
			}
		}
	}

	@Nullable
	private static GeoBone resolvePoseBone(GeoModel<?> model, String bipedName, @Nullable GeoBone armorFallback) {
		return model.getBone(bipedName).orElse(armorFallback);
	}

	@Override
	public RenderType getRenderType(${name}Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
