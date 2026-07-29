package ${package}.item.renderer;

import com.geckolib.renderer.GeoItemRenderer;

import ${package}.item.${name}Item;
import ${package}.item.model.${name}ItemModel;

public class ${name}ItemRenderer extends GeoItemRenderer<${name}Item> {
	public ${name}ItemRenderer() {
		super(new ${name}ItemModel());
	}
}
