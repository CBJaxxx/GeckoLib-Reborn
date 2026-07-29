package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>
<#assign modelId = geomodel?replace(".geo.json", "")>

import com.geckolib.cache.GeckoLibResources;
import com.geckolib.model.DefaultedBlockGeoModel;
import com.geckolib.renderer.base.GeoRenderState;

import net.minecraft.resources.Identifier;

import ${package}.block.display.${name}DisplayItem;

public class ${name}DisplayModel extends DefaultedBlockGeoModel<${name}DisplayItem> {
	/** Preferred: geckolib/models/block/<name> (from DefaultedBlockGeoModel super) */
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelId}");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelId}");
	private static final Identifier MODEL_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelId}");
	private static final Identifier ANIM_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelId}");
	private static final Identifier MODEL_ITEM = Identifier.fromNamespaceAndPath("${modid}", "item/${modelId}");
	private static final Identifier ANIM_ITEM = Identifier.fromNamespaceAndPath("${modid}", "item/${modelId}");
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("${modid}", "textures/block/${texture}.png");

	public ${name}DisplayModel() {
		// Same block/ asset prefix as the block entity model
		super(Identifier.fromNamespaceAndPath("${modid}", "${modelId}"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		Identifier preferred = super.getModelResource(renderState); // modid:block/<name>
		if (GeckoLibResources.getBakedModels().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_FLAT))
			return MODEL_FLAT;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_ENTITY))
			return MODEL_ENTITY;
		return MODEL_ITEM;
	}

	@Override
	public Identifier getAnimationResource(${name}DisplayItem animatable) {
		Identifier preferred = super.getAnimationResource(animatable); // modid:block/<name>
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_FLAT))
			return ANIM_FLAT;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_ENTITY))
			return ANIM_ENTITY;
		return ANIM_ITEM;
	}

	@Override
	public Identifier[] getAnimationResourceFallbacks(${name}DisplayItem animatable) {
		return new Identifier[] { ANIM_FLAT, ANIM_ENTITY, ANIM_ITEM };
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		// Display items use the block texture (MCreator textures/block/)
		return TEXTURE;
	}
}
