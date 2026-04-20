package ${package}.entity.model;

import software.bernie.geckolib.animation.AnimationState;

public class ${name}Model extends GeoModel<${name}Entity, ${name}RenderState> {
    @Override
    public ResourceLocation getAnimationResource(${name}RenderState renderState) {
        return ResourceLocation.parse("${modid}:${data.model?replace(".geo.json", "")}");
    }

    @Override
    public ResourceLocation getModelResource(${name}RenderState renderState) {
        return ResourceLocation.parse("${modid}:${data.model?replace(".geo.json", "")}");
    }

    @Override
    public ResourceLocation getTextureResource(${name}RenderState renderState) {
        return ResourceLocation.parse("${modid}:textures/entities/" + renderState.texture + ".png");
    }
     
    <#if data.headMovement>
    @Override
    public void setCustomAnimations(${name}Entity animatable, long instanceId, AnimationState animationState) {
	    GeoBone head = getAnimationProcessor().getBone("${data.groupName}");
	    if (head != null) {
		    EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}
	
    }
    </#if>
}