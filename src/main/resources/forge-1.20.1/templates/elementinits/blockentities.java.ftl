<#-- @formatter:off -->

/*
 *    MCreator note: This file will be REGENERATED on each build.
 */

package ${package}.init;

public class ${JavaModName}BlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ${JavaModName}.MODID);

	<#list blockentities as blockentity>
	<#if blockentity.getModElement().getTypeString() != "animatedblock">
	public static final RegistryObject<BlockEntityType<${blockentity.getModElement().getName()}BlockEntity>> ${blockentity.getModElement().getRegistryNameUpper()} =
		register("${blockentity.getModElement().getRegistryName()}", ${JavaModName}Blocks.${blockentity.getModElement().getRegistryNameUpper()},
			${blockentity.getModElement().getName()}BlockEntity::new);
	<#else>
	public static final RegistryObject<BlockEntityType<${blockentity.getModElement().getName()}TileEntity>> ${blockentity.getModElement().getRegistryNameUpper()} =
		register("${blockentity.getModElement().getRegistryName()}", ${JavaModName}Blocks.${blockentity.getModElement().getRegistryNameUpper()},
			${blockentity.getModElement().getName()}TileEntity::new);
	</#if>
	</#list>

	// Start of user code block custom block entities
	// End of user code block custom block entities

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

}
<#-- @formatter:on -->
