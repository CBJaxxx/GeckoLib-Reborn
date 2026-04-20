package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}DisplayModel extends GeoModel<${name}DisplayItem, ${name}DisplayRenderState> {
	@Override
	public ResourceLocation getAnimationResource(${name}DisplayRenderState renderState) {
		return ResourceLocation.parse("${modid}:${geomodel?replace(".geo.json", "")}");
	}

	@Override
	public ResourceLocation getModelResource(${name}DisplayRenderState renderState) {
		return ResourceLocation.parse("${modid}:${geomodel?replace(".geo.json", "")}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}DisplayRenderState renderState) {
		return ResourceLocation.parse("${modid}:textures/block/${texture}.png");
	}
}