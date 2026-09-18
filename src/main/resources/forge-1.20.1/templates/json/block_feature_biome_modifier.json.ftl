<#assign generationBiomes = w.filterBrokenReferences(data.restrictionBiomes)>
{
  "type": "forge:add_feature",
  <#if generationBiomes?size == 1>
  "biomes": "${generationBiomes?first}",
  <#elseif generationBiomes?size gt 1>
  "biomes": [
    <#list generationBiomes as generationBiome>"${generationBiome}"<#sep>,</#list>
  ],
  <#else>
  "biomes": {
    "type": "forge:any"
  },
  </#if>
  "features": ["${modid}:${registryname}"],
  "step": "${step}"
}
