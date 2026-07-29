<#--
 # Play GeckoLib entity procedure animation (NeoForge 26.1.2 / GeckoLib 5.5.2).
 # Sets synched ANIMATION data; EntityAnimationFactory copies it to animationprocedure
 # so the entity's AnimationController can play the named animation.
-->
if (${input$entity} instanceof ${(field$name)?replace("CUSTOM:", "")}Entity _geckolibEntity) {
	_geckolibEntity.setAnimation(${input$animation});
}
