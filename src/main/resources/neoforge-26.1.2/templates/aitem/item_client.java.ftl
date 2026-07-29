<#-- @formatter:off -->
package ${package}.client.renderer.item;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;

import ${package}.init.${JavaModName}Items;
import ${package}.item.${name}Item;

@EventBusSubscriber(Dist.CLIENT)
public class ${name}ItemClient {

	@SubscribeEvent
	public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			<#if data.enableArmPose>
			@Override
			public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
				if (!itemStack.isEmpty() && entityLiving.getUsedItemHand() == hand) {
					return (HumanoidModel.ArmPose) ${name}Item.ARM_POSE.getValue();
				}
				return HumanoidModel.ArmPose.EMPTY;
			}
			</#if>

			<#if data.disableSwing>
			@Override
			public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
				int i = arm == HumanoidArm.RIGHT ? 1 : -1;
				poseStack.translate(i * 0.56F, -0.52F, -0.72F);
				if (player.getUseItem() == itemInHand) {
					poseStack.translate(0.05, 0.05, 0.05);
				}
				return true;
			}
			</#if>
		}, ${JavaModName}Items.${data.getModElement().getRegistryNameUpper()}.get());
	}
}
<#-- @formatter:on -->
