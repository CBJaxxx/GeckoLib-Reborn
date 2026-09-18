package ${package}.init;

import net.minecraftforge.api.distmarker.Dist;

<#-- Client-only: the trigger field this reads is consumed (reset to "undefined") on
     read, so if the server also ran this it could reset its own sync pulse before the
     packet reaches remote clients. The server-side copy of animationprocedure is now
     written directly by setAnimation() instead. -->
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class EntityAnimationFactory {

	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
	if (event != null && event.getEntity() != null) {
	<#list animatedentitys as syncable>
	if (event.getEntity() instanceof ${syncable.getModElement().getName()}Entity syncable) {
		String animation = syncable.getSyncedAnimation();
		if (!animation.equals("undefined")) {
			syncable.setAnimation("undefined");
			syncable.animationprocedure = animation;
			}
		}
	</#list>
		}
	}

}