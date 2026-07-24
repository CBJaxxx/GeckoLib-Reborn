<#--
 # GeckoLib 4 animated item model.
 # Display settings must include parent builtin/entity (enforced on import).
-->
{
  "parent": "${modid}:displaysettings/${data.displaySettings?replace(".json", "")}",
  "textures": {
    "particle": "${modid}:item/${data.texture}"
  }
}
