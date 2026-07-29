package ${package}.client.renderer;

import com.geckolib.cache.model.BakedGeoModel;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import ${package}.item.${name}Item;
import ${package}.item.model.${name}Model;

public class ${name}ArmorRenderer extends GeoArmorRenderer<${name}Item, HumanoidRenderState> {
	public static final DataTicket<LivingEntity> WEARER = DataTicket.create("${modid}_${registryname}_armor_wearer", LivingEntity.class);

	public ${name}ArmorRenderer() {
		super(new ${name}Model());
		<#if data.glowTexture?has_content>
		this.withRenderLayer(new AutoGlowingGeoLayer<>(this));
		</#if>
	}

	@Override
	public void addRenderData(${name}Item animatable, RenderData relatedObject, HumanoidRenderState renderState, float partialTick) {
		renderState.addGeckolibData(WEARER, relatedObject.entity());
	}

	@Override
	public String getBoneNameForSegment(HumanoidRenderState renderState, ArmorSegment segment) {
		return switch (segment) {
			case HEAD -> "${data.head}";
			case CHEST -> "${data.chest}";
			case LEFT_ARM -> "${data.leftArm}";
			case RIGHT_ARM -> "${data.rightArm}";
			case LEFT_LEG -> "${data.leftLeg}";
			case RIGHT_LEG -> "${data.rightLeg}";
			case LEFT_FOOT -> "${data.leftBoot}";
			case RIGHT_FOOT -> "${data.rightBoot}";
		};
	}


	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void adjustModelBonesForRender(RenderPassInfo<HumanoidRenderState> renderPassInfo, BoneSnapshots snapshots) {
		final HumanoidRenderState renderState = renderPassInfo.renderState();
		final EquipmentSlot slot = Objects.requireNonNull(renderState.getGeckolibData(CURRENT_SLOT));
		final HumanoidModel baseModel = Objects.requireNonNull(renderState.getGeckolibData(BASE_MODEL));
		final BakedGeoModel model = renderPassInfo.model();

		if (getSegmentsForSlot(renderState, slot).isEmpty())
			return;

		baseModel.setupAnim(renderState);

		for (ArmorSegment segment : getSegmentsForSlot(renderState, slot)) {
			String poseBone = resolvePoseBoneName(model, renderState, segment);
			snapshots.get(poseBone).ifPresent(snapshot -> {
				final ModelPart modelPart = segment.modelPartGetter.apply(baseModel);
				final Vector3f bonePos = segment.modelPartMatcher.apply(new Vector3f(modelPart.x, modelPart.y, modelPart.z));

				snapshot.setRotX(-modelPart.xRot)
						.setRotY(-modelPart.yRot)
						.setRotZ(modelPart.zRot)
						.setTranslateX(bonePos.x)
						.setTranslateY(bonePos.y)
						.setTranslateZ(bonePos.z);
			});
		}
	}


	@Override
	public void submitRenderTasks(RenderPassInfo<HumanoidRenderState> renderPassInfo, OrderedSubmitNodeCollector renderTasks, @Nullable RenderType renderType) {
		if (renderType == null)
			return;

		final int packedLight = renderPassInfo.packedLight();
		final int packedOverlay = renderPassInfo.packedOverlay();
		final int renderColor = renderPassInfo.renderColor();
		final HumanoidRenderState renderState = renderPassInfo.renderState();
		final EquipmentSlot slot = Objects.requireNonNull(renderState.getGeckolibData(CURRENT_SLOT));
		final BakedGeoModel model = renderPassInfo.model();

		if (model.isMissingno()) {
			submitMissingModelRender(renderPassInfo, renderTasks);
			return;
		}

		renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), renderType, (pose, vertexConsumer) -> {
			final PoseStack poseStack = renderPassInfo.poseStack();

			poseStack.pushPose();
			poseStack.last().set(pose);
			renderPassInfo.renderPosed(() -> {
				for (ArmorSegment segment : getSegmentsForSlot(renderState, slot)) {
					String boneName = resolvePoseBoneName(model, renderState, segment);
					renderPassInfo.model().getBone(boneName)
							.ifPresent(bone -> bone.positionAndRender(renderPassInfo, vertexConsumer, packedLight, packedOverlay, renderColor));
				}
			});
			poseStack.popPose();
		});
	}


	private String resolvePoseBoneName(BakedGeoModel model, HumanoidRenderState renderState, ArmorSegment segment) {
		String biped = switch (segment) {
			case HEAD -> "bipedHead";
			case CHEST -> "bipedBody";
			case LEFT_ARM -> "bipedLeftArm";
			case RIGHT_ARM -> "bipedRightArm";
			default -> null;
		};
		String armor = getBoneNameForSegment(renderState, segment);

		if (biped != null && model.getBone(biped).isPresent())
			return biped;
		return armor;
	}

	@Override
	public RenderType getRenderType(HumanoidRenderState renderState, Identifier texture) {
		return RenderTypes.entityTranslucent(texture);
	}
}
