<#-- @formatter:off -->
<#if data.itemTexture?has_content>
{
  "parent": "item/generated",
  "textures": {
    "layer0": "${modid}:item/${data.itemTexture}",
    "particle": "${modid}:item/${data.itemTexture}"
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
