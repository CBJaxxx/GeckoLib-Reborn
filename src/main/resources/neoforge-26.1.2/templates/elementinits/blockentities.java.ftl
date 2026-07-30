<#-- @formatter:off -->
package ${package}.init;

<#assign blockentitiesWithInventory = w.getGElementsOfType("block")?filter(e -> e.hasInventory)>
<#assign animatedBlockentitiesWithInventory = w.getGElementsOfType("animatedblock")?filter(e -> e.hasInventory)>

<#if blockentitiesWithInventory?size != 0 || animatedBlockentitiesWithInventory?size != 0>
@EventBusSubscriber
</#if>
public class ${JavaModName}BlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ${JavaModName}.MODID);

	<#list blockentities as blockentity>
	<#if blockentity.getModElement().getTypeString() != "animatedblock">
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<${blockentity.getModElement().getName()}BlockEntity>> ${blockentity.getModElement().getRegistryNameUpper()} =
		register("${blockentity.getModElement().getRegistryName()}", ${JavaModName}Blocks.${blockentity.getModElement().getRegistryNameUpper()},
			${blockentity.getModElement().getName()}BlockEntity::new);
	<#else>
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<${blockentity.getModElement().getName()}TileEntity>> ${blockentity.getModElement().getRegistryNameUpper()} =
		register("${blockentity.getModElement().getRegistryName()}", ${JavaModName}Blocks.${blockentity.getModElement().getRegistryNameUpper()},
			${blockentity.getModElement().getName()}TileEntity::new);
	</#if>
	</#list>

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> new BlockEntityType(supplier, block.get()));
	}

	<#if blockentitiesWithInventory?size != 0 || animatedBlockentitiesWithInventory?size != 0>
	<#compress>
	@SubscribeEvent public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		<#list blockentitiesWithInventory as blockentity>
			event.registerBlockEntity(Capabilities.Item.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(), WorldlyContainerWrapper::new);
			<#if blockentity.hasEnergyStorage>
			event.registerBlockEntity(Capabilities.Energy.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}BlockEntity) blockEntity).getEnergyStorage());
			</#if>
			<#if blockentity.isFluidTank>
			event.registerBlockEntity(Capabilities.Fluid.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}BlockEntity) blockEntity).getFluidTank());
			</#if>
		</#list>
		<#list animatedBlockentitiesWithInventory as blockentity>
			event.registerBlockEntity(Capabilities.Item.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(), WorldlyContainerWrapper::new);
			<#if blockentity.hasEnergyStorage>
			event.registerBlockEntity(Capabilities.Energy.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}TileEntity) blockEntity).getEnergyStorage());
			</#if>
			<#if blockentity.isFluidTank>
			event.registerBlockEntity(Capabilities.Fluid.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}TileEntity) blockEntity).getFluidTank());
			</#if>
		</#list>
	}
	</#compress>
	</#if>

}
<#-- @formatter:on -->
