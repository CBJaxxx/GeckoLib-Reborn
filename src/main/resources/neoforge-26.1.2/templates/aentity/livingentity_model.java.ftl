package ${package}.entity.model;

<#assign modelBase = data.model?replace(".geo.json", "")>

import com.geckolib.cache.GeckoLibResources;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.model.DefaultedEntityGeoModel;
import com.geckolib.renderer.base.GeoRenderState;

import net.minecraft.resources.Identifier;

import ${package}.entity.${name}Entity;

public class ${name}Model extends DefaultedEntityGeoModel<${name}Entity> {
	public static final DataTicket<String> TEXTURE = DataTicket.create("${modid}_${registryname}_texture", String.class);

	/** Flat layout fallback when assets were imported without entity/ subfolders. */
	private static final Identifier MODEL_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelBase}");
	private static final Identifier ANIM_FLAT = Identifier.fromNamespaceAndPath("${modid}", "${modelBase}");

	public ${name}Model() {
		// DefaultedEntityGeoModel prefixes model/anim keys with entity/
		// matching: geckolib/models/entity/<path> and geckolib/animations/entity/<path>
		super(Identifier.fromNamespaceAndPath("${modid}", "${modelBase}"));
	}

	@Override
	public Identifier getModelResource(GeoRenderState renderState) {
		Identifier preferred = super.getModelResource(renderState);
		if (GeckoLibResources.getBakedModels().cache().containsKey(preferred))
			return preferred;
		return MODEL_FLAT;
	}

	@Override
	public Identifier getAnimationResource(${name}Entity animatable) {
		Identifier preferred = super.getAnimationResource(animatable);
		if (GeckoLibResources.getBakedAnimations().cache().containsKey(preferred))
			return preferred;
		return ANIM_FLAT;
	}

	@Override
	public Identifier[] getAnimationResourceFallbacks(${name}Entity animatable) {
		return new Identifier[] { ANIM_FLAT };
	}

	/**
	 * MCreator stores entity textures under textures/entities/ (plural),
	 * not GeckoLib DefaultedEntityGeoModel's textures/entity/.
	 */
	@Override
	public Identifier getTextureResource(GeoRenderState renderState) {
		String texture = renderState.getOrDefaultGeckolibData(TEXTURE, "${data.mobModelTexture?replace(".png", "")}");
		return Identifier.fromNamespaceAndPath("${modid}", "textures/entities/" + texture + ".png");
	}

	@Override
	public void addAdditionalStateData(${name}Entity animatable, Object relatedObject, GeoRenderState renderState) {
		renderState.addGeckolibData(TEXTURE, animatable.getTexture());
	}
}
