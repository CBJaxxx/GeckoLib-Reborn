package ${package}.item.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>
<#assign modelId = geomodel?replace(".geo.json", "")>

import com.geckolib.cache.GeckoLibResources;
import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.base.GeoRenderState;

import net.minecraft.resources.Identifier;

import ${package}.item.${name}Item;

public class ${name}ItemModel extends DefaultedItemGeoModel<${name}Item> {
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelId}");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelId}");
	private static final Identifier MODEL_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelId}");
	private static final Identifier ANIM_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelId}");
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("${modid}", "textures/item/${texture}.png");

	public ${name}ItemModel() {
		// DefaultedItemGeoModel prefixes model/anim keys with item/
		super(Identifier.fromNamespaceAndPath("${modid}", "${modelId}"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		Identifier preferred = super.getModelResource(renderState);
		if (GeckoLibResources.getBakedModels().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_FLAT))
			return MODEL_FLAT;
		return MODEL_ENTITY;
	}

	@Override
	public Identifier getAnimationResource(${name}Item animatable) {
		Identifier preferred = super.getAnimationResource(animatable);
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_FLAT))
			return ANIM_FLAT;
		return ANIM_ENTITY;
	}

	@Override
	public Identifier[] getAnimationResourceFallbacks(${name}Item animatable) {
		return new Identifier[] { ANIM_FLAT, ANIM_ENTITY };
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		// MCreator item textures: assets/<modid>/textures/item/<name>.png
		return TEXTURE;
	}
}
