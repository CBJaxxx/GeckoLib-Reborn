package ${package}.utils;

import com.geckolib.cache.model.GeoBone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;

public class AnimUtils {

	public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer,
			int packedLightIn, int packedOverlayIn) {
		setupModelFromBone(model, bone);
		model.render(stack, buffer, packedLightIn, packedOverlayIn);
	}

	public static void setupModelFromBone(ModelPart model, GeoBone bone) {
		// GeckoLib 5 GeoBone: pivot accessors are pivotX/Y/Z (not getPivot*)
		model.setPos(bone.pivotX(), bone.pivotY(), bone.pivotZ());
		model.xRot = 0.0f;
		model.yRot = 0.0f;
		model.zRot = 0.0f;
	}

}
