package ${package}.block.renderer;

import com.geckolib.renderer.GeoItemRenderer;

import ${package}.block.display.${name}DisplayItem;
import ${package}.block.model.${name}DisplayModel;

public class ${name}DisplayItemRenderer extends GeoItemRenderer<${name}DisplayItem> {
	public ${name}DisplayItemRenderer() {
		super(new ${name}DisplayModel());
	}
}
