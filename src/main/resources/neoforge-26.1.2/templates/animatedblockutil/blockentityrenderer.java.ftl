package ${package}.block.renderer;

import com.geckolib.renderer.GeoBlockRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

import ${package}.block.entity.${name}TileEntity;
import ${package}.block.model.${name}BlockModel;

public class ${name}TileRenderer extends GeoBlockRenderer<${name}TileEntity, BlockEntityRenderState> {
	public ${name}TileRenderer(BlockEntityRendererProvider.Context context) {
		super(context, new ${name}BlockModel());
	}
}
