{
  "model": {
	<#if data.hasCustomBucketTexture()>
    "type": "minecraft:model",
	"model": "${modid}:item/${registryname}_bucket"
	<#else>
	"type": "forge:fluid_container",
    "fluid": "${modid}:${registryname}",
    "textures": {
      "base": "item/bucket",
      "fluid": "forge:item/mask/bucket_fluid",
      "cover": "forge:item/mask/bucket_fluid_cover"
    }
	</#if>
  }
}