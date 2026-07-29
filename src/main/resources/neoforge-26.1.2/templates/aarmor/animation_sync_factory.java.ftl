package ${package}.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.geckolib.animatable.GeoItem;

<#list animatedarmors as aarmor>
import ${package}.item.${aarmor.getModElement().getName()}Item;
</#list>

@EventBusSubscriber
public class ArmorAnimationFactory {

	@SubscribeEvent
	public static void animatedArmors(PlayerTickEvent.Post event) {
		syncSlot(event, EquipmentSlot.HEAD);
		syncSlot(event, EquipmentSlot.CHEST);
		syncSlot(event, EquipmentSlot.LEGS);
		syncSlot(event, EquipmentSlot.FEET);
	}

	private static void syncSlot(PlayerTickEvent.Post event, EquipmentSlot slot) {
		ItemStack stack = event.getEntity().getItemBySlot(slot);
		if (stack.isEmpty() || !(stack.getItem() instanceof GeoItem))
			return;

		String animation = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("geckoAnim", "");
		if (animation.isEmpty())
			return;

		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString("geckoAnim", ""));
		if (!event.getEntity().level().isClientSide())
			return;

		<#list animatedarmors as aarmor>
		if (stack.getItem() instanceof ${aarmor.getModElement().getName()}Item animatable)
			animatable.animationprocedure = animation;
		</#list>
	}
}
