package ${package}.item.model;

<#assign modelBase = data.model?replace(".geo.json", "")>
<#assign textureFile = data.armorTextureFile?replace(".png", "")>

import com.geckolib.cache.GeckoLibResources;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

import net.minecraft.resources.Identifier;

import ${package}.item.${name}Item;

public class ${name}Model extends GeoModel<${name}Item> {
	private static final Identifier MODEL_ARMOR = Identifier.fromNamespaceAndPath("${modid}", "armor/${modelBase}");
	private static final Identifier ANIM_ARMOR = Identifier.fromNamespaceAndPath("${modid}", "armor/${modelBase}");
	private static final Identifier MODEL_ITEM = Identifier.fromNamespaceAndPath("${modid}", "item/${modelBase}");
	private static final Identifier ANIM_ITEM = Identifier.fromNamespaceAndPath("${modid}", "item/${modelBase}");
	private static final Identifier MODEL_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelBase}");
	private static final Identifier ANIM_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelBase}");
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelBase}");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelBase}");
	private static final Identifier TEXTURE_ITEM = Identifier.fromNamespaceAndPath("${modid}", "textures/item/${textureFile}.png");
	private static final Identifier TEXTURE_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "textures/entity/${textureFile}.png");

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_ARMOR))
			return MODEL_ARMOR;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_ITEM))
			return MODEL_ITEM;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_FLAT;
	}

	@Override
	public Identifier getAnimationResource(${name}Item animatable) {
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_ARMOR))
			return ANIM_ARMOR;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_ITEM))
			return ANIM_ITEM;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_FLAT;
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		return TEXTURE_ITEM;
	}
}
