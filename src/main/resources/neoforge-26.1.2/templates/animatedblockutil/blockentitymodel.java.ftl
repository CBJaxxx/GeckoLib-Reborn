package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>
<#assign modelId = geomodel?replace(".geo.json", "")>

import com.geckolib.cache.GeckoLibResources;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.model.DefaultedBlockGeoModel;
import com.geckolib.renderer.base.GeoRenderState;

import net.minecraft.resources.Identifier;

import ${package}.block.entity.${name}TileEntity;

public class ${name}BlockModel extends DefaultedBlockGeoModel<${name}TileEntity> {
	<#if data.hasBlockstates()>
	public static final DataTicket<Integer> BLOCKSTATE = DataTicket.create("${modid}_${registryname}_blockstate", Integer.class);
	</#if>

	/** Flat layout: geckolib/models/<name>.geo.json */
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelId}");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelId}");
	/** Importer default: geckolib/models/entity/<name>.geo.json */
	private static final Identifier MODEL_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelId}");
	private static final Identifier ANIM_ENTITY = Identifier.fromNamespaceAndPath("${modid}", "entity/${modelId}");
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("${modid}", "textures/block/${texture}.png");

	public ${name}BlockModel() {
		// DefaultedBlockGeoModel prefixes model/anim keys with block/
		// matching: geckolib/models/block/<path> and geckolib/animations/block/<path>
		super(Identifier.fromNamespaceAndPath("${modid}", "${modelId}"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		<#if data.hasBlockstates()>
		final int blockstate = renderState.getOrDefaultGeckolibData(BLOCKSTATE, 0);
		<#list data.blockstateList as state>
		<#assign stateModelId = state.customModelName?replace(".geo.json", "")>
		if (blockstate == ${state?index + 1}) {
			Identifier stateBlock = Identifier.fromNamespaceAndPath("${modid}", "block/${stateModelId}");
			if (GeckoLibResources.getBakedModels().cache().containsKey(stateBlock))
				return stateBlock;
			Identifier stateFlat = Identifier.fromNamespaceAndPath("${modid}", "${stateModelId}");
			if (GeckoLibResources.getBakedModels().cache().containsKey(stateFlat))
				return stateFlat;
			return Identifier.fromNamespaceAndPath("${modid}", "entity/${stateModelId}");
		}
		</#list>
		</#if>
		Identifier preferred = super.getModelResource(renderState);
		if (GeckoLibResources.getBakedModels().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedModels().cache().containsKey(MODEL_FLAT))
			return MODEL_FLAT;
		return MODEL_ENTITY;
	}

	@Override
	public Identifier getAnimationResource(${name}TileEntity animatable) {
		<#if data.hasBlockstates()>
		final int blockstate = animatable.blockstateNew;
		<#list data.blockstateList as state>
		<#assign stateModelId = state.customModelName?replace(".geo.json", "")>
		if (blockstate == ${state?index + 1}) {
			Identifier stateBlock = Identifier.fromNamespaceAndPath("${modid}", "block/${stateModelId}");
			if (GeckoLibResources.getBakedAnimations().cache().containsKey(stateBlock))
				return stateBlock;
			Identifier stateFlat = Identifier.fromNamespaceAndPath("${modid}", "${stateModelId}");
			if (GeckoLibResources.getBakedAnimations().cache().containsKey(stateFlat))
				return stateFlat;
			return Identifier.fromNamespaceAndPath("${modid}", "entity/${stateModelId}");
		}
		</#list>
		</#if>
		Identifier preferred = super.getAnimationResource(animatable);
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(preferred))
			return preferred;
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(ANIM_FLAT))
			return ANIM_FLAT;
		return ANIM_ENTITY;
	}

	@Override
	public Identifier[] getAnimationResourceFallbacks(${name}TileEntity animatable) {
		return new Identifier[] { ANIM_FLAT, ANIM_ENTITY };
	}

	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		<#if data.hasBlockstates()>
		final int blockstate = renderState.getOrDefaultGeckolibData(BLOCKSTATE, 0);
		<#list data.blockstateList as state>
		if (blockstate == ${state?index + 1})
			return Identifier.fromNamespaceAndPath("${modid}", "textures/block/${state.texture}.png");
		</#list>
		</#if>
		// MCreator block textures: assets/<modid>/textures/block/<name>.png
		return TEXTURE;
	}

	<#if data.hasBlockstates()>
	@Override
	public void addAdditionalStateData(${name}TileEntity animatable, Object relatedObject, GeoRenderState renderState) {
		renderState.addGeckolibData(BLOCKSTATE, animatable.blockstateNew);
	}
	</#if>
}
