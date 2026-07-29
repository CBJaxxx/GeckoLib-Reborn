<#include "procedures.java.ftl">
@Mod.EventBusSubscriber public class ${name}Procedure {
	@SubscribeEvent public static void onWorldLoad(net.minecraftforge.event.level.LevelEvent.Load event) {
		<#assign dependenciesCode>
			<@procedureDependenciesCode dependencies, {
				"world": "event.getLevel()",
				"event": "event"
			}/>
		</#assign>
		execute(event<#if dependenciesCode?has_content>,</#if>${dependenciesCode});
	}