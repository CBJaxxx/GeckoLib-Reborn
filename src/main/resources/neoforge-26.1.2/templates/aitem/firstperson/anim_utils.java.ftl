package ${package}.utils;

import com.geckolib.cache.model.GeoBone;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class AnimUtils {

	public static void setupModelFromBone(ModelPart model, GeoBone bone) {
		model.resetPose();
		model.setPos(bone.pivotX(), bone.pivotY(), bone.pivotZ());
		model.xRot = 0.0f;
		model.yRot = 0.0f;
		model.zRot = 0.0f;
		model.visible = true;
	}

	public static void submitPartOverBone(ModelPart model, GeoBone bone, PoseStack poseStack,
			SubmitNodeCollector renderTasks, Identifier texture, int packedLight) {
		setupModelFromBone(model, bone);
		renderTasks.submitModelPart(model, poseStack, RenderTypes.entityTranslucent(texture), packedLight,
				OverlayTexture.NO_OVERLAY, null);
	}

}
