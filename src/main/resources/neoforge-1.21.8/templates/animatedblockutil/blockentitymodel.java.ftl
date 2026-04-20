package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}BlockModel extends GeoModel<${name}TileEntity, ${name}BlockRenderState> {
	@Override
	public ResourceLocation getAnimationResource(${name}BlockRenderState renderState) {
	   <#if data.hasBlockstates()>
	        final int blockstate = renderState.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return ResourceLocation.parse("${modid}:${state.customModelName?replace(".geo.json", "")}");
	        </#list>
	    </#if>
		return ResourceLocation.parse("${modid}:${geomodel?replace(".geo.json", "")}");
	}

	@Override
	public ResourceLocation getModelResource(${name}BlockRenderState renderState) {
	   <#if data.hasBlockstates()>
	        final int blockstate = renderState.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return ResourceLocation.parse("${modid}:${state.customModelName?replace(".geo.json", "")}");
	        </#list>
	    </#if>
		return ResourceLocation.parse("${modid}:${geomodel?replace(".geo.json", "")}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}BlockRenderState renderState) {
	   <#if data.hasBlockstates()>
	        final int blockstate = renderState.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return ResourceLocation.parse("${modid}:textures/block/${state.texture}.png");
	        </#list>
	    </#if>
		return ResourceLocation.parse("${modid}:textures/block/${texture}.png");
	}
}