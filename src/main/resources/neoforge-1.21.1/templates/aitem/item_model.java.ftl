package ${package}.item.model;

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

import ${package}.item.${name}Item;

public class ${name}ItemModel extends GeoModel<${name}Item> {
	// Preferred GeckoLib 4 item layout
	private static final ResourceLocation MODEL_ITEM = ResourceLocation.parse("${modid}:geo/item/${modelFile}");
	private static final ResourceLocation ANIM_ITEM = ResourceLocation.parse("${modid}:animations/item/${modelBase}.animation.json");
	// Shared plugin import layout (entity/ subfolder)
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.parse("${modid}:geo/entity/${modelFile}");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.parse("${modid}:animations/entity/${modelBase}.animation.json");
	// Flat legacy layout
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.parse("${modid}:geo/${modelFile}");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.parse("${modid}:animations/${modelBase}.animation.json");

	@Override
	public ResourceLocation getModelResource(${name}Item animatable) {
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ITEM))
			return MODEL_ITEM;
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_FLAT;
	}

	@Override
	public ResourceLocation getAnimationResource(${name}Item animatable) {
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ITEM))
			return ANIM_ITEM;
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_FLAT;
	}

	@Override
	public ResourceLocation getTextureResource(${name}Item animatable) {
		// MCreator item textures: assets/<modid>/textures/item/<name>.png
		return ResourceLocation.parse("${modid}:textures/item/${texture}.png");
	}
}
