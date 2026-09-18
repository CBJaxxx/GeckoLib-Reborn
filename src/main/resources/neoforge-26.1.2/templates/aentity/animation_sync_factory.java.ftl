package ${package}.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

<#list animatedentitys as syncable>
import ${package}.entity.${syncable.getModElement().getName()}Entity;
</#list>

<#-- Client-only: the trigger fields this reads are consumed (reset to "undefined") on
     read, so if the server also ran this it could reset its own sync pulse before the
     packet reaches remote clients. The server-side copy of animationprocedure/animation_X
     is now written directly by setAnimation()/setControllerAnimation() instead. -->
@EventBusSubscriber(value = Dist.CLIENT)
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
