<#--
 # GeckoLib 4.9.2 armor GeoModel (NeoForge 1.21.1)
 # Wiki: https://github.com/bernie-g/geckolib/wiki/Geckolib-Armor-(Geckolib4)
 #
 # Preferred: geo/item/<model>.geo.json + animations/item/<model>.animation.json
 # Plugin import also uses geo/entity/ + animations/entity/
 # Flat geo/ + animations/ as last fallback.
-->
package ${package}.item.model;

<#assign modelFile = data.model>
<#assign modelBase = data.model?replace(".geo.json", "")>
<#if !modelFile?ends_with(".geo.json")>
	<#assign modelFile = modelFile + ".geo.json">
</#if>
<#assign textureFile = data.armorTextureFile?replace(".png", "")>

import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import ${package}.item.${name}Item;

public class ${name}Model extends GeoModel<${name}Item> {
	private static final ResourceLocation MODEL_ITEM = ResourceLocation.parse("${modid}:geo/item/${modelFile}");
	private static final ResourceLocation ANIM_ITEM = ResourceLocation.parse("${modid}:animations/item/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_ENTITY = ResourceLocation.parse("${modid}:geo/entity/${modelFile}");
	private static final ResourceLocation ANIM_ENTITY = ResourceLocation.parse("${modid}:animations/entity/${modelBase}.animation.json");
	private static final ResourceLocation MODEL_FLAT = ResourceLocation.parse("${modid}:geo/${modelFile}");
	private static final ResourceLocation ANIM_FLAT = ResourceLocation.parse("${modid}:animations/${modelBase}.animation.json");

	@Override
	public ResourceLocation getModelResource(${name}Item object) {
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ITEM))
			return MODEL_ITEM;
		if (GeckoLibCache.getBakedModels().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_FLAT;
	}

	@Override
	public ResourceLocation getAnimationResource(${name}Item object) {
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ITEM))
			return ANIM_ITEM;
		if (GeckoLibCache.getBakedAnimations().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_FLAT;
	}

	@Override
	public ResourceLocation getTextureResource(${name}Item object) {
		// Prefer textures/item/, fall back to textures/entities/ (common for shared armor maps)
		return ResourceLocation.parse("${modid}:textures/item/${textureFile}.png");
	}
}
