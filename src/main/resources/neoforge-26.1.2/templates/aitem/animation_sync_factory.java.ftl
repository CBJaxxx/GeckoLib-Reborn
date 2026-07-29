package ${package}.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.geckolib.animatable.GeoItem;

<#list animateditems as item>
import ${package}.item.${item.getModElement().getName()}Item;
</#list>

/**
 * Syncs procedure-set geckoAnim custom data onto the client GeoItem instance.
 * CompoundTag#getString returns Optional on NeoForge 26.1 — use getStringOr.
 */
@EventBusSubscriber
public class ItemAnimationFactory {

	@SubscribeEvent
	public static void animatedItems(PlayerTickEvent.Post event) {
		ItemStack mainhandItem = event.getEntity().getMainHandItem();
		ItemStack offhandItem = event.getEntity().getOffhandItem();
		if (!(mainhandItem.getItem() instanceof GeoItem) && !(offhandItem.getItem() instanceof GeoItem))
			return;

		<#list animateditems as item>
		if (mainhandItem.getItem() instanceof ${item.getModElement().getName()}Item) {
			String animation = mainhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("geckoAnim", "");
			if (!animation.isEmpty()) {
				CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getMainHandItem(), tag -> tag.putString("geckoAnim", ""));
				if (event.getEntity().level().isClientSide()) {
					((${item.getModElement().getName()}Item) event.getEntity().getMainHandItem().getItem()).animationprocedure = animation;
				}
			}
		}
		if (offhandItem.getItem() instanceof ${item.getModElement().getName()}Item) {
			String animation = offhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("geckoAnim", "");
			if (!animation.isEmpty()) {
				CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getOffhandItem(), tag -> tag.putString("geckoAnim", ""));
				if (event.getEntity().level().isClientSide()) {
					((${item.getModElement().getName()}Item) event.getEntity().getOffhandItem().getItem()).animationprocedure = animation;
				}
			}
		}
		</#list>
	}

}
