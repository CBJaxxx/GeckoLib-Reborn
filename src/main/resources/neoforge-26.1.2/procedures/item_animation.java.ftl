<#--
 # Play GeckoLib item procedure animation via CUSTOM_DATA geckoAnim.
 # ItemAnimationFactory applies it client-side each player tick.
-->
<#include "mcitems.ftl">
if (${mappedMCItemToItem(input$item)} instanceof ${(field$name)?replace("CUSTOM:", "")}Item) {
	CustomData.update(DataComponents.CUSTOM_DATA, ${mappedMCItemToItemStackCode(input$item, 1)}, tag -> tag.putString("geckoAnim", ${input$animation}));
}
