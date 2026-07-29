package ${package}.entity.layer;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import ${package}.entity.${name}Entity;

public class ${name}Layer extends AutoGlowingGeoLayer<${name}Entity, Void, LivingEntityRenderState> {
	private static final Identifier GLOW = Identifier.fromNamespaceAndPath("${modid}",
			"textures/entities/${data.mobModelGlowTexture}");

	public ${name}Layer(GeoRenderer<${name}Entity, Void, LivingEntityRenderState> renderer) {
		super(renderer);
	}

	@Override
	protected Identifier getTextureResource(LivingEntityRenderState renderState) {
		return GLOW;
	}
}
