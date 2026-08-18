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
	private static final ResourceLocation MODEL_BLOCK = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/block/${modelFile}");
	private static final ResourceLocation ANIM_BLOCK = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/block/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/entity/${modelFile}");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/entity/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/${modelFile}");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/${modelBase}.animation.json");

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
	                	ResourceLocation.fromNamespaceAndPath("${modid}", "animations/block/${stateBase}.animation.json"),
	                	ResourceLocation.fromNamespaceAndPath("${modid}", "animations/entity/${stateBase}.animation.json"),
	                	ResourceLocation.fromNamespaceAndPath("${modid}", "animations/${stateBase}.animation.json"));
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
	                	ResourceLocation.fromNamespaceAndPath("${modid}", "geo/block/${stateFile}"),
	                	ResourceLocation.fromNamespaceAndPath("${modid}", "geo/entity/${stateFile}"),
	                	ResourceLocation.fromNamespaceAndPath("${modid}", "geo/${stateFile}"));
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
	                return ResourceLocation.fromNamespaceAndPath("${modid}", "textures/block/${state.texture}.png");
	        </#list>
	    </#if>
		return ResourceLocation.fromNamespaceAndPath("${modid}", "textures/block/${texture}.png");
	}
}
