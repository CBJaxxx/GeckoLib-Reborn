<#-- @formatter:off -->
package ${package}.item.inventory;

<#compress>
@EventBusSubscriber
public class ${name}InventoryCapability extends ItemAccessItemHandler {

	@SubscribeEvent
	public static void onItemDropped(ItemTossEvent event) {
		if (event.getEntity().getItem().getItem() == ${JavaModName}Items.${data.getModElement().getRegistryNameUpper()}.get()) {
			Player player = event.getPlayer();
			if (player.containerMenu instanceof ${data.guiBoundTo}Menu)
				player.closeContainer();
		}
	}

	public ${name}InventoryCapability(ItemAccess access) {
		super(access, DataComponents.CONTAINER, ${data.inventorySize});
	}

	<#if data.inventoryStackSize != 99>
	@Override
	protected int getCapacity(int index, ItemResource resource) {
		return Math.min(${data.inventoryStackSize}, super.getCapacity(index, resource));
	}
	</#if>

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return super.isValid(index, resource)
				&& resource.getItem() != ${JavaModName}Items.${data.getModElement().getRegistryNameUpper()}.get();
	}

}
</#compress>
<#-- @formatter:on -->
