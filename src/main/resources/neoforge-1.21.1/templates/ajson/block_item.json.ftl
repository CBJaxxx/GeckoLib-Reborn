<#-- @formatter:off -->
<#--
 # GeckoLib 4 item model for animated blocks.
 # Parent must ultimately resolve to builtin/entity (via displaysettings) so
 # inventory/hand rendering uses GeoItemRenderer / BEWLR.
 # Display transforms come from the imported Blockbench display settings.
-->
<#if data.itemTexture?has_content>
{
  "parent": "item/generated",
  "textures": {
    "layer0": "${modid}:item/${data.itemTexture}"
  }
}
<#else>
{
  "parent": "${modid}:displaysettings/${data.displaySettings?replace(".json", "")}",
  "textures": {
    "particle": "${modid}:block/${data.texture}"
  }
}
</#if>
<#-- @formatter:on -->
