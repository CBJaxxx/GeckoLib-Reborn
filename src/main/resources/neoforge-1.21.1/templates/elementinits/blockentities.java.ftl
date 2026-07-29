<#-- @formatter:off -->
package ${package}.init;

<#assign blockentitiesWithInventory = w.getGElementsOfType("block")?filter(e -> e.hasInventory)>
<#assign animatedBlockentitiesWithInventory = w.getGElementsOfType("animatedblock")?filter(e -> e.hasInventory)>

<#if blockentitiesWithInventory?size != 0 || animatedBlockentitiesWithInventory?size != 0>
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
</#if>
public class ${JavaModName}BlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ${JavaModName}.MODID);

	<#list blockentities as blockentity>
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ${blockentity.getModElement().getRegistryNameUpper()} =
		register("${blockentity.getModElement().getRegistryName()}", ${JavaModName}Blocks.${blockentity.getModElement().getRegistryNameUpper()},
		<#if blockentity.getModElement().getTypeString() != "animatedblock">
			${blockentity.getModElement().getName()}BlockEntity::new);
	    <#else>
	        ${blockentity.getModElement().getName()}TileEntity::new);
	    </#if>
	</#list>

	// Start of user code block custom block entities
	// End of user code block custom block entities

	private static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	<#if blockentitiesWithInventory?size != 0 || animatedBlockentitiesWithInventory?size != 0>
	<#compress>
	@SubscribeEvent public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		<#list blockentitiesWithInventory as blockentity>
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}BlockEntity) blockEntity).getItemHandler());
			<#if blockentity.hasEnergyStorage>
			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}BlockEntity) blockEntity).getEnergyStorage());
			</#if>
			<#if blockentity.isFluidTank>
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}BlockEntity) blockEntity).getFluidTank());
			</#if>
		</#list>
		<#list animatedBlockentitiesWithInventory as blockentity>
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}TileEntity) blockEntity).getItemHandler());
			<#if blockentity.hasEnergyStorage>
			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}TileEntity) blockEntity).getEnergyStorage());
			</#if>
			<#if blockentity.isFluidTank>
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ${blockentity.getModElement().getRegistryNameUpper()}.get(),
				(blockEntity, side) -> ((${blockentity.getModElement().getName()}TileEntity) blockEntity).getFluidTank());
			</#if>
		</#list>
	}
	</#compress>
	</#if>

}
<#-- @formatter:on -->