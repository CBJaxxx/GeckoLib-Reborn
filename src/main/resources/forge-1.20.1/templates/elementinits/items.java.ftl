<#-- @formatter:off -->
<#include "../procedures.java.ftl">

/*
 *    MCreator note: This file will be REGENERATED on each build.
 */

package ${package}.init;

<#assign hasBlocks = false>
<#assign hasDoubleBlocks = false>
<#assign hasSigns = false>
<#assign hasHangingSigns = false>
<#assign hasItemsWithProperties = w.getGElementsOfType("item")?filter(e -> e.customProperties?has_content)?size != 0
	|| w.getGElementsOfType("tool")?filter(e -> e.toolType == "Shield")?size != 0>

<#assign chunks = items?chunk(2500)>
<#assign has_chunks = chunks?size gt 1>

<#if hasItemsWithProperties>
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
</#if>
public class ${JavaModName}Items {

	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ${JavaModName}.MODID);

	<@javacompress>
	<#list items as item>
		<#if item.getModElement().getTypeString() == "armor">
			<#if item.enableHelmet>public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()}_HELMET;</#if>
			<#if item.enableBody>public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()}_CHESTPLATE;</#if>
			<#if item.enableLeggings>public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()}_LEGGINGS;</#if>
			<#if item.enableBoots>public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()}_BOOTS;</#if>
		<#elseif item.getModElement().getTypeString() == "animatedarmor">
			<#if item.enableHelmet>public static <#if !has_chunks>final</#if> RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_HELMET;</#if>
			<#if item.enableBody>public static <#if !has_chunks>final</#if> RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_CHESTPLATE;</#if>
			<#if item.enableLeggings>public static <#if !has_chunks>final</#if> RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_LEGGINGS;</#if>
			<#if item.enableBoots>public static <#if !has_chunks>final</#if> RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_BOOTS;</#if>
		<#elseif item.getModElement().getTypeString() == "livingentity" || item.getModElement().getTypeString() == "animatedentity">
			public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()}_SPAWN_EGG;
		<#elseif item.getModElement().getTypeString() == "fluid" && item.generateBucket>
			public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()}_BUCKET;
		<#else>
			public static <#if !has_chunks>final</#if> RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()};
		</#if>
	</#list>
	</@javacompress>

	<#list chunks as sub_items>
	<#if has_chunks>public static void register${sub_items?index}()<#else>static</#if> {
		<#list sub_items as item>
			<#if item.getModElement().getTypeString() == "armor">
				<#if item.enableHelmet>
				${item.getModElement().getRegistryNameUpper()}_HELMET =
					REGISTRY.register("${item.getModElement().getRegistryName()}_helmet", ${item.getModElement().getName()}Item.Helmet::new);
				</#if>
				<#if item.enableBody>
				${item.getModElement().getRegistryNameUpper()}_CHESTPLATE =
					REGISTRY.register("${item.getModElement().getRegistryName()}_chestplate", ${item.getModElement().getName()}Item.Chestplate::new);
				</#if>
				<#if item.enableLeggings>
				${item.getModElement().getRegistryNameUpper()}_LEGGINGS =
					REGISTRY.register("${item.getModElement().getRegistryName()}_leggings", ${item.getModElement().getName()}Item.Leggings::new);
				</#if>
				<#if item.enableBoots>
				${item.getModElement().getRegistryNameUpper()}_BOOTS =
					REGISTRY.register("${item.getModElement().getRegistryName()}_boots", ${item.getModElement().getName()}Item.Boots::new);
				</#if>
			<#elseif item.getModElement().getTypeString() == "animatedarmor">
				<#if item.enableHelmet>
				${item.getModElement().getRegistryNameUpper()}_HELMET =
					REGISTRY.register("${item.getModElement().getRegistryName()}_helmet", () -> new ${item.getModElement().getName()}Item(ArmorItem.Type.HELMET, new Item.Properties()<#if item.helmetImmuneToFire>.fireResistant()</#if>));
				</#if>
				<#if item.enableBody>
				${item.getModElement().getRegistryNameUpper()}_CHESTPLATE =
					REGISTRY.register("${item.getModElement().getRegistryName()}_chestplate", () -> new ${item.getModElement().getName()}Item(ArmorItem.Type.CHESTPLATE, new Item.Properties()<#if item.bodyImmuneToFire>.fireResistant()</#if>));
				</#if>
				<#if item.enableLeggings>
				${item.getModElement().getRegistryNameUpper()}_LEGGINGS =
					REGISTRY.register("${item.getModElement().getRegistryName()}_leggings", () -> new ${item.getModElement().getName()}Item(ArmorItem.Type.LEGGINGS, new Item.Properties()<#if item.leggingsImmuneToFire>.fireResistant()</#if>));
				</#if>
				<#if item.enableBoots>
				${item.getModElement().getRegistryNameUpper()}_BOOTS =
					REGISTRY.register("${item.getModElement().getRegistryName()}_boots", () -> new ${item.getModElement().getName()}Item(ArmorItem.Type.BOOTS, new Item.Properties()<#if item.bootsImmuneToFire>.fireResistant()</#if>));
				</#if>
			<#elseif item.getModElement().getTypeString() == "livingentity" || item.getModElement().getTypeString() == "animatedentity">
				${item.getModElement().getRegistryNameUpper()}_SPAWN_EGG =
					REGISTRY.register("${item.getModElement().getRegistryName()}_spawn_egg",
						() -> new ForgeSpawnEggItem(${JavaModName}Entities.${item.getModElement().getRegistryNameUpper()},
						${item.spawnEggBaseColor.getRGB()}, ${item.spawnEggDotColor.getRGB()}, new Item.Properties()));
			<#elseif item.getModElement().getTypeString() == "specialentity">
				${item.getModElement().getRegistryNameUpper()} =
					REGISTRY.register("${item.getModElement().getRegistryName()}",
						() -> new ${JavaModName}BoatItem(${JavaModName}Boat.Type.${item.getModElement().getRegistryNameUpper()}<#if item.rarity != "COMMON">, Rarity.${item.rarity}</#if>));
			<#elseif item.getModElement().getTypeString() == "animatedblock">
				${item.getModElement().getRegistryNameUpper()} =
					REGISTRY.register(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}.getId().getPath(),
						() -> new ${item.getModElement().getName()}DisplayItem(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}.get(), new Item.Properties()));
			<#elseif item.getModElement().getTypeString() == "dimension" && item.hasIgniter()>
				${item.getModElement().getRegistryNameUpper()} =
					REGISTRY.register("${item.getModElement().getRegistryName()}", ${item.getModElement().getName()}Item::new);
			<#elseif item.getModElement().getTypeString() == "fluid" && item.generateBucket>
				${item.getModElement().getRegistryNameUpper()}_BUCKET =
					REGISTRY.register("${item.getModElement().getRegistryName()}_bucket", ${item.getModElement().getName()}Item::new);
			<#elseif item.getModElement().getTypeString() == "block" || item.getModElement().getTypeString() == "plant">
				<#if item.isDoubleBlock()>
					<#assign hasDoubleBlocks = true>
					${item.getModElement().getRegistryNameUpper()} =
					doubleBlock(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}
					<#if item.hasCustomItemProperties()>, <@blockItemProperties item/></#if>);
				<#elseif (item.getModElement().getTypeString() == "block") && (item.blockBase! == "Sign")>
					<#assign hasSigns = true>
					${item.getModElement().getRegistryNameUpper()} =
					signBlock(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}, ${JavaModName}Blocks.${item.getWallRegistryNameUpper()}
					<#if item.hasCustomItemProperties()>, <@blockItemProperties item/></#if>);
				<#elseif (item.getModElement().getTypeString() == "block") && (item.blockBase! == "HangingSign")>
					<#assign hasHangingSigns = true>
					${item.getModElement().getRegistryNameUpper()} =
					hangingSignBlock(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}, ${JavaModName}Blocks.${item.getWallRegistryNameUpper()}
					<#if item.hasCustomItemProperties()>, <@blockItemProperties item/></#if>);
				<#else>
					<#assign hasBlocks = true>
					${item.getModElement().getRegistryNameUpper()} =
					block(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}
					<#if item.hasCustomItemProperties()>, <@blockItemProperties item/></#if>);
				</#if>
			<#else>
				${item.getModElement().getRegistryNameUpper()} =
					REGISTRY.register("${item.getModElement().getRegistryName()}", ${item.getModElement().getName()}Item::new);
			</#if>
		</#list>
	}
	</#list>

	<#if has_chunks>
	static {
		<#list 0..chunks?size-1 as i>register${i}();</#list>
	}
	</#if>

	// Start of user code block custom items
	// End of user code block custom items

	<#if hasBlocks>
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return block(block, new Item.Properties());
	}

	private static RegistryObject<Item> block(RegistryObject<Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
	</#if>

	<#if hasDoubleBlocks>
	private static RegistryObject<Item> doubleBlock(RegistryObject<Block> block) {
		return doubleBlock(block, new Item.Properties());
	}

	private static RegistryObject<Item> doubleBlock(RegistryObject<Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new DoubleHighBlockItem(block.get(), properties));
	}
	</#if>

	<#if hasSigns>
	private static RegistryObject<Item> signBlock(RegistryObject<Block> block, RegistryObject<Block> wallBlock) {
		return signBlock(block, wallBlock, new Item.Properties());
	}

	private static RegistryObject<Item> signBlock(RegistryObject<Block> block, RegistryObject<Block> wallBlock, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new SignItem(properties, block.get(), wallBlock.get()));
	}
	</#if>

	<#if hasHangingSigns>
	private static RegistryObject<Item> hangingSignBlock(RegistryObject<Block> block, RegistryObject<Block> wallBlock) {
		return hangingSignBlock(block, wallBlock, new Item.Properties());
	}

	private static RegistryObject<Item> hangingSignBlock(RegistryObject<Block> block, RegistryObject<Block> wallBlock, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new HangingSignItem(block.get(), wallBlock.get(), properties));
	}
	</#if>

	<#if hasItemsWithProperties>
		@SubscribeEvent @OnlyIn(Dist.CLIENT) public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
		<@javacompress>
		<#list items as item>
			<#if item.getModElement().getTypeString() == "item">
				<#list item.customProperties.entrySet() as property>
				ItemProperties.register(${item.getModElement().getRegistryNameUpper()}.get(),
					ResourceLocation.parse("${modid}:${item.getModElement().getRegistryName()}_${property.getKey()}"),
					(itemStackToRender, clientWorld, entity, itemEntityId) ->
						<#if hasProcedure(property.getValue())>
							(float) <@procedureCode property.getValue(), {
								"x": "entity != null ? entity.getX() : 0",
								"y": "entity != null ? entity.getY() : 0",
								"z": "entity != null ? entity.getZ() : 0",
								"world": "entity != null ? entity.level() : clientWorld",
								"entity": "entity",
								"itemstack": "itemStackToRender"
							}, false/>
						<#else>0</#if>
				);
				</#list>
			<#elseif item.getModElement().getTypeString() == "tool" && item.toolType == "Shield">
				ItemProperties.register(${item.getModElement().getRegistryNameUpper()}.get(), ResourceLocation.parse("blocking"),
					ItemProperties.getProperty(Items.SHIELD, ResourceLocation.parse("blocking")));
			</#if>
		</#list>
		</@javacompress>
		});
	}
	</#if>
}
<#macro blockItemProperties block>
new Item.Properties()
<#if block.maxStackSize != 64>
	.stacksTo(${block.maxStackSize})
</#if>
<#if block.rarity != "COMMON">
	.rarity(Rarity.${block.rarity})
</#if>
<#if block.immuneToFire>
	.fireResistant()
</#if>
</#macro>
<#-- @formatter:on -->
