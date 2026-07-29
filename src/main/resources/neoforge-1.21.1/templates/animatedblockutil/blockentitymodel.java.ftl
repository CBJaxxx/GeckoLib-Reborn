<#--
 # GeckoLib 4.9.2 block GeoModel (NeoForge 1.21.1)
 # Wiki: https://github.com/bernie-g/geckolib/wiki (GeckoLib 4)
 #
 # Preferred DefaultedBlockGeoModel-style layout:
 #   geo/block/<name>.geo.json
 #   animations/block/<name>.animation.json
 #   textures/block/<texture>.png
 #
 # Plugin model import places files under geo/entity/ and animations/entity/
 # (shared with entities). Resolve in order: block/ → entity/ → flat.
 # GeckoLibCache keys are full resource paths including .geo.json / .animation.json.
-->
package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>
<#assign modelBase = geomodel?replace(".geo.json", "")>
<#assign modelFile = geomodel>
<#if !geomodel?ends_with(".geo.json")>
	<#assign modelFile = geomodel + ".geo.json">
	<#assign modelBase = geomodel>
</#if>

import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import ${package}.block.entity.${name}TileEntity;

public class ${name}BlockModel extends GeoModel<${name}TileEntity> {
	// Preferred GeckoLib 4 block layout
	private static final ResourceLocation MODEL_BLOCK = ResourceLocation.parse("${modid}:geo/block/${modelFile}");
	private static final ResourceLocation ANIM_BLOCK = ResourceLocation.parse("${modid}:animations/block/${modelBase}.animation.json");
	// Shared plugin import layout (entity/ subfolder)
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.parse("${modid}:geo/entity/${modelFile}");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.parse("${modid}:animations/entity/${modelBase}.animation.json");
	// Flat legacy layout
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.parse("${modid}:geo/${modelFile}");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.parse("${modid}:animations/${modelBase}.animation.json");

	private static ResourceLocation resolveModel(ResourceLocation block, ResourceLocation entity, ResourceLocation flat) {
		if (GeckoLibCache.getBakedModels().containsKey(block))
			return block;
		if (GeckoLibCache.getBakedModels().containsKey(entity))
			return entity;
		return flat;
	}

	private static ResourceLocation resolveAnim(ResourceLocation block, ResourceLocation entity, ResourceLocation flat) {
		if (GeckoLibCache.getBakedAnimations().containsKey(block))
			return block;
		if (GeckoLibCache.getBakedAnimations().containsKey(entity))
			return entity;
		return flat;
	}

	@Override
	public ResourceLocation getAnimationResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            <#assign stateBase = state.customModelName?replace(".geo.json", "")>
	            <#assign stateFile = state.customModelName>
	            <#if !state.customModelName?ends_with(".geo.json")>
	            	<#assign stateFile = state.customModelName + ".geo.json">
	            	<#assign stateBase = state.customModelName>
	            </#if>
	            if (blockstate == ${state?index + 1})
	                return resolveAnim(
	                	ResourceLocation.parse("${modid}:animations/block/${stateBase}.animation.json"),
	                	ResourceLocation.parse("${modid}:animations/entity/${stateBase}.animation.json"),
	                	ResourceLocation.parse("${modid}:animations/${stateBase}.animation.json"));
	        </#list>
	    </#if>
		return resolveAnim(ANIM_BLOCK, ANIM_ENTITY, ANIM_FLAT);
	}

	@Override
	public ResourceLocation getModelResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            <#assign stateFile = state.customModelName>
	            <#if !state.customModelName?ends_with(".geo.json")>
	            	<#assign stateFile = state.customModelName + ".geo.json">
	            </#if>
	            if (blockstate == ${state?index + 1})
	                return resolveModel(
	                	ResourceLocation.parse("${modid}:geo/block/${stateFile}"),
	                	ResourceLocation.parse("${modid}:geo/entity/${stateFile}"),
	                	ResourceLocation.parse("${modid}:geo/${stateFile}"));
	        </#list>
	    </#if>
		return resolveModel(MODEL_BLOCK, MODEL_ENTITY, MODEL_FLAT);
	}

	@Override
	public ResourceLocation getTextureResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return ResourceLocation.parse("${modid}:textures/block/${state.texture}.png");
	        </#list>
	    </#if>
		// MCreator block textures: assets/<modid>/textures/block/<name>.png
		return ResourceLocation.parse("${modid}:textures/block/${texture}.png");
	}
}
