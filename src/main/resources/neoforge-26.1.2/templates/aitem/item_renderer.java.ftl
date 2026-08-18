package ${package}.item.renderer;

import com.geckolib.renderer.GeoItemRenderer;
<#if data.firstPersonArms>
import com.geckolib.cache.model.GeoBone;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.PerBoneRender;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

import com.mojang.blaze3d.vertex.PoseStack;

import ${package}.utils.AnimUtils;

import java.util.function.BiConsumer;
</#if>

import ${package}.item.${name}Item;
import ${package}.item.model.${name}ItemModel;

public class ${name}ItemRenderer extends GeoItemRenderer<${name}Item> {
	public ${name}ItemRenderer() {
		super(new ${name}ItemModel());
		<#if data.firstPersonArms>
		withRenderLayer(new FirstPersonArmLayer(this));
		</#if>
	}

	<#if data.firstPersonArms>
	@Override
	public void adjustModelBonesForRender(RenderPassInfo<GeoRenderState> renderPassInfo, BoneSnapshots snapshots) {
		snapshots.ifPresent("${data.leftArm}", snapshot -> snapshot.skipRender(true));
		snapshots.ifPresent("${data.rightArm}", snapshot -> snapshot.skipRender(true));
	}

	private static final class FirstPersonArmLayer extends GeoRenderLayer<${name}Item, GeoItemRenderer.RenderData, GeoRenderState> {
		private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;

		private FirstPersonArmLayer(GeoItemRenderer<${name}Item> renderer) {
			super(renderer);
		}

		@Override
		public void addPerBoneRender(RenderPassInfo<GeoRenderState> renderPassInfo,
				BiConsumer<GeoBone, PerBoneRender<GeoRenderState>> consumer) {
			if (!renderPassInfo.willRender())
				return;

			ItemDisplayContext context = renderPassInfo.renderState()
					.getOrDefaultGeckolibData(DataTickets.ITEM_RENDER_PERSPECTIVE, ItemDisplayContext.NONE);
			if (context == null || !context.firstPerson())
				return;

			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.player == null || minecraft.player.isInvisible())
				return;

			renderPassInfo.model().getBone("${data.leftArm}")
					.ifPresent(bone -> consumer.accept(bone, (info, geoBone, tasks) -> submitArm(info, geoBone, tasks, true)));
			renderPassInfo.model().getBone("${data.rightArm}")
					.ifPresent(bone -> consumer.accept(bone, (info, geoBone, tasks) -> submitArm(info, geoBone, tasks, false)));
		}

		private static void submitArm(RenderPassInfo<GeoRenderState> renderPassInfo, GeoBone bone,
				SubmitNodeCollector renderTasks, boolean left) {
			Minecraft minecraft = Minecraft.getInstance();
			AbstractClientPlayer player = minecraft.player;
			if (player == null)
				return;

			AvatarRenderer<?> avatarRenderer = (AvatarRenderer<?>) minecraft.getEntityRenderDispatcher().getRenderer(player);
			PlayerModel model = avatarRenderer.getModel();
			Identifier skin = player.getSkin().body().texturePath();
			ModelPart arm = left ? model.leftArm : model.rightArm;
			ModelPart sleeve = left ? model.leftSleeve : model.rightSleeve;

			arm.resetPose();
			sleeve.resetPose();
			arm.visible = true;
			sleeve.visible = true;

			PoseStack poseStack = renderPassInfo.poseStack();
			poseStack.pushPose();
			poseStack.translate((left ? -1.0f : 1.0f) * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
			AnimUtils.submitPartOverBone(arm, bone, poseStack, renderTasks, skin, renderPassInfo.packedLight());
			AnimUtils.submitPartOverBone(sleeve, bone, poseStack, renderTasks, skin, renderPassInfo.packedLight());
			poseStack.popPose();
		}
	}
	</#if>
}
