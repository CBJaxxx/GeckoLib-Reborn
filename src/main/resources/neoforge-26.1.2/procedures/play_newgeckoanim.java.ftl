<#--
 # Play GeckoLib entity procedure animation (NeoForge 26.1.2 / GeckoLib 5.5.2).
 # Sets the synched slot of the named controller; EntityAnimationFactory copies it
 # into the field that controller's predicate reads. A blank or unknown controller
 # name falls back to the built-in "procedure" controller.
-->
if (${input$entity} instanceof ${(field$name)?replace("CUSTOM:", "")}Entity _geckolibEntity) {
	_geckolibEntity.setControllerAnimation(${input$controller}, ${input$animation});
}
