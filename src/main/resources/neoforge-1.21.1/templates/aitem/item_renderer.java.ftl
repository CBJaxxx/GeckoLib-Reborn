package ${package}.item.renderer;

import software.bernie.geckolib.renderer.GeoItemRenderer;
<#if data.firstPersonArms>
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtil;
</#if>

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
<#if data.firstPersonArms>
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import ${package}.utils.AnimUtils;
</#if>

import ${package}.item.model.${name}ItemModel;
import ${package}.item.${name}Item;

public class ${name}ItemRenderer extends GeoItemRenderer<${name}Item> {
	public ${name}ItemRenderer() {
		super(new ${name}ItemModel());
	}

	@Override
	public RenderType getRenderType(${name}Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	<#if data.firstPersonArms>
	private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
	protected MultiBufferSource currentBuffer;
	public ItemDisplayContext transformType;

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int packedOverlayIn) {
		this.transformType = transformType;
		super.renderByItem(stack, transformType, matrixStack, bufferIn, combinedLightIn, packedOverlayIn);
	}

	@Override
	public void actuallyRender(PoseStack matrixStackIn, ${name}Item animatable, BakedGeoModel model, RenderType type, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, boolean isRenderer, float partialTicks, int packedLightIn, int packedOverlayIn, int color) {
		this.currentBuffer = renderTypeBuffer;
		super.actuallyRender(matrixStackIn, animatable, model, type, renderTypeBuffer, vertexBuilder, isRenderer, partialTicks, packedLightIn, packedOverlayIn, color);
	}

	@Override
	public void renderRecursively(PoseStack stack, ${name}Item animatable, GeoBone bone, RenderType type, MultiBufferSource buffer, VertexConsumer bufferIn, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, int color) {
		Minecraft mc = Minecraft.getInstance();
		String name = bone.getName();
		boolean renderingArms = name.equals("${data.leftArm}") || name.equals("${data.rightArm}");
		if (renderingArms) {
			bone.setHidden(true);
		}
		if (this.transformType != null && this.transformType.firstPerson() && renderingArms && mc.player != null && this.currentBuffer != null) {
			AbstractClientPlayer player = mc.player;
			PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
			PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
			stack.pushPose();
			RenderUtil.translateMatrixToBone(stack, bone);
			RenderUtil.translateToPivotPoint(stack, bone);
			RenderUtil.rotateMatrixAroundBone(stack, bone);
			RenderUtil.scaleMatrixForBone(stack, bone);
			RenderUtil.translateAwayFromPivotPoint(stack, bone);
			ResourceLocation loc = player.getSkin().texture();
			if (name.equals("${data.leftArm}")) {
				stack.translate(-1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				if (!player.isInvisible()) {
					AnimUtils.renderPartOverBone(model.leftArm, bone, stack, this.currentBuffer.getBuffer(RenderType.entitySolid(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
					AnimUtils.renderPartOverBone(model.leftSleeve, bone, stack, this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
				}
			} else if (name.equals("${data.rightArm}")) {
				stack.translate(1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				if (!player.isInvisible()) {
					AnimUtils.renderPartOverBone(model.rightArm, bone, stack, this.currentBuffer.getBuffer(RenderType.entitySolid(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
					AnimUtils.renderPartOverBone(model.rightSleeve, bone, stack, this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
				}
			}
			stack.popPose();
		}
		super.renderRecursively(stack, animatable, bone, type, buffer, bufferIn, isReRender, partialTick, packedLightIn, packedOverlayIn, color);
	}
	</#if>
}
