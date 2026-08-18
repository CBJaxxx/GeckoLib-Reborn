package ${package}.entity.model;

<#assign modelFile = data.model>
<#assign modelBase = data.model?replace(".geo.json", "")>

import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import ${package}.entity.${name}Entity;

public class ${name}Model extends GeoModel<${name}Entity> {
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/entity/${modelFile}");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/entity/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/${modelFile}");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/${modelBase}.animation.json");

	@Override
	public ResourceLocation getModelResource(${name}Entity animatable) {
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_FLAT;
	}

	@Override
	public ResourceLocation getTextureResource(${name}Entity animatable) {
		return ResourceLocation.fromNamespaceAndPath("${modid}", "textures/entities/" + animatable.getTexture() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(${name}Entity animatable) {
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_FLAT;
	}

	<#if data.headMovement>
	@Override
	public void setCustomAnimations(${name}Entity animatable, long instanceId, AnimationState<${name}Entity> animationState) {
		GeoBone head = this.getAnimationProcessor().getBone("${data.groupName}");
		if (head != null) {
			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			if (entityData != null) {
				head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
				head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
			}
		}
	}
	</#if>
}
