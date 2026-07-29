package ${package}.init;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

<#list animatedentitys as syncable>
import ${package}.entity.${syncable.getModElement().getName()}Entity;
</#list>

@EventBusSubscriber
public class EntityAnimationFactory {

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		if (event == null || event.getEntity() == null)
			return;

		<#list animatedentitys as syncable>
		if (event.getEntity() instanceof ${syncable.getModElement().getName()}Entity syncable) {
			String animation = syncable.getSyncedAnimation();
			if (!animation.equals("undefined")) {
				syncable.setAnimation("undefined");
				syncable.animationprocedure = animation;
			}
			syncable.applySyncedControllerAnimations();
		}
		</#list>
	}
}
