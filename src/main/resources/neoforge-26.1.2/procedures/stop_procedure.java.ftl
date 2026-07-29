<#--
 # Stop GeckoLib entity procedure animation.
-->
if (${input$entity} instanceof ${(field$name)?replace("CUSTOM:", "")}Entity _geckolibEntity) {
	_geckolibEntity.setAnimation("empty");
}
