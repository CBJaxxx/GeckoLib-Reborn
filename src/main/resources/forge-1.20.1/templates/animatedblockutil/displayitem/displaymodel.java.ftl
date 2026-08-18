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

import ${package}.block.display.${name}DisplayItem;

public class ${name}DisplayModel extends GeoModel<${name}DisplayItem> {
	private static final ResourceLocation MODEL_BLOCK = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/block/${modelFile}");
	private static final ResourceLocation ANIM_BLOCK = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/block/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/entity/${modelFile}");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/entity/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/${modelFile}");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_ITEM = ResourceLocation.fromNamespaceAndPath("${modid}", "geo/item/${modelFile}");
	private static final ResourceLocation ANIM_ITEM = ResourceLocation.fromNamespaceAndPath("${modid}", "animations/item/${modelBase}.animation.json");

	@Override
	public ResourceLocation getModelResource(${name}DisplayItem animatable) {
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_BLOCK))
			return MODEL_BLOCK;
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_FLAT))
			return MODEL_FLAT;
		return MODEL_ITEM;
	}

	@Override
	public ResourceLocation getAnimationResource(${name}DisplayItem animatable) {
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_BLOCK))
			return ANIM_BLOCK;
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_FLAT))
			return ANIM_FLAT;
		return ANIM_ITEM;
	}

	@Override
	public ResourceLocation getTextureResource(${name}DisplayItem entity) {
		return ResourceLocation.fromNamespaceAndPath("${modid}", "textures/block/${texture}.png");
	}
}
