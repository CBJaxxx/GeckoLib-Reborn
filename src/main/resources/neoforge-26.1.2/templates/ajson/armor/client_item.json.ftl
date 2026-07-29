{
  "model": {
    "type": "minecraft:model",
    "model": "${modid}:item/${registryname}<#if var_item?? && var_item=="helmet">_helmet<#elseif var_item?? && var_item=="body">_chestplate<#elseif var_item?? && var_item=="leggings">_leggings<#elseif var_item?? && var_item=="boots">_boots</#if>"
  }
}
