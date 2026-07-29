<#--
 # Stop the animation on the named GeckoLib controller.
-->
if (${input$entity} instanceof ${(field$name)?replace("CUSTOM:", "")}Entity _geckolibEntity) {
	_geckolibEntity.setControllerAnimation(${input$controller}, "empty");
}
