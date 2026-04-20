package ${package}.item.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}ItemModel extends GeoModel<${name}Item, ${name}ItemRenderState> {
	@Override
	public ResourceLocation getAnimationResource(${name}ItemRenderState renderState) {
		return ResourceLocation.parse("${modid}:${geomodel?replace(".geo.json", "")}");
	}

	@Override
	public ResourceLocation getModelResource(${name}ItemRenderState renderState) {
		return ResourceLocation.parse("${modid}:${geomodel?replace(".geo.json", "")}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}ItemRenderState renderState) {
		return ResourceLocation.parse("${modid}:textures/item/${texture}.png");
	}
}