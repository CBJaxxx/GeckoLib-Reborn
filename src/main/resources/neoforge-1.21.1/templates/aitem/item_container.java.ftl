<#-- @formatter:off -->
package ${package}.item.inventory;

<#compress>
@EventBusSubscriber(Dist.CLIENT) public class ${name}InventoryCapability extends ComponentItemHandler {

	@SubscribeEvent @OnlyIn(Dist.CLIENT) public static void onItemDropped(ItemTossEvent event) {
		if (event.getEntity().getItem().getItem() == ${JavaModName}Items.${data.getModElement().getRegistryNameUpper()}.get()) {
			if (Minecraft.getInstance().screen instanceof ${data.guiBoundTo}Screen) {
				Minecraft.getInstance().player.closeContainer();
			}
		}
	}

	public ${name}InventoryCapability(MutableDataComponentHolder parent) {
		super(parent, DataComponents.CONTAINER, ${data.inventorySize});
	}

	@Override public int getSlotLimit(int slot) {
		return ${data.inventoryStackSize};
	}

	@Override public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.getItem() != ${JavaModName}Items.${data.getModElement().getRegistryNameUpper()}.get();
	}

	@Override public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot).copy();
	}

}
</#compress>

<#-- @formatter:on -->