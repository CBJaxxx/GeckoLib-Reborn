package ${package}.item.model;

public class ${name}Model extends GeoModel<${name}Item, ${name}ItemRenderState> {
    @Override
    public ResourceLocation getAnimationResource(${name}ItemRenderState renderState) {
        return ResourceLocation.parse("${modid}:${data.model?replace(".geo.json", "")}");
    }

    @Override
    public ResourceLocation getModelResource(${name}ItemRenderState renderState) {
        return ResourceLocation.parse("${modid}:${data.model?replace(".geo.json", "")}");
    }

    @Override
    public ResourceLocation getTextureResource(${name}ItemRenderState renderState) {
        return ResourceLocation.parse("${modid}:textures/item/${data.armorTextureFile}");
    }

}