<#-- @formatter:off -->
package ${package};

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.event.TickEvent;

@Mod("${modid}") public class ${JavaModName} {

	public static final Logger LOGGER = LogManager.getLogger(${JavaModName}.class);

	public static final String MODID = "${modid}";

	public static final SimpleChannel PACKET_HANDLER = ChannelBuilder
		.named(ResourceLocation.fromNamespaceAndPath(MODID, MODID))
		.networkProtocolVersion(1)
		.simpleChannel();

	private static int messageID = 0;

	public ${JavaModName}(FMLJavaModLoadingContext context) {
		// Start of user code block mod constructor
		// End of user code block mod constructor

		var modBusGroup = context.getModBusGroup();

		MinecraftForge.EVENT_BUS.register(this);

		<@javacompress>
		<#if w.hasSounds()>${JavaModName}Sounds.REGISTRY.register(modBusGroup);</#if>
		<#if types["base:blocks"]??>${JavaModName}Blocks.REGISTRY.register(modBusGroup);</#if>
		<#if types["base:blockentities"]??>${JavaModName}BlockEntities.REGISTRY.register(modBusGroup);</#if>
		<#if types["base:items"]??>${JavaModName}Items.REGISTRY.register(modBusGroup);</#if>
		<#if types["base:entities"]??>${JavaModName}Entities.REGISTRY.register(modBusGroup);</#if>
		<#if w.hasItemsInTabs()>${JavaModName}Tabs.REGISTRY.register(modBusGroup);</#if>
		<#if types["base:features"]??>${JavaModName}Features.REGISTRY.register(modBusGroup);</#if>
		<#if w.getElementsOfType("feature")?filter(e -> e.getMetadata("has_nbt_structure")??)?size != 0>StructureFeature.REGISTRY.register(modBusGroup);</#if>
		<#if types["potions"]??>${JavaModName}Potions.REGISTRY.register(modBusGroup);</#if>
		<#if types["potioneffects"]??>${JavaModName}MobEffects.REGISTRY.register(modBusGroup);</#if>
		<#if types["guis"]??>${JavaModName}Menus.REGISTRY.register(modBusGroup);</#if>
		<#if types["particles"]??>${JavaModName}ParticleTypes.REGISTRY.register(modBusGroup);</#if>
		<#if types["villagerprofessions"]??>${JavaModName}VillagerProfessions.PROFESSIONS.register(modBusGroup);</#if>
		<#if types["fluids"]??>
			${JavaModName}Fluids.REGISTRY.register(modBusGroup);
			${JavaModName}FluidTypes.REGISTRY.register(modBusGroup);
		</#if>
		<#if types["attributes"]??>${JavaModName}Attributes.REGISTRY.register(modBusGroup);</#if>
		</@javacompress>

		// Start of user code block mod init
		// End of user code block mod init
	}

	// Start of user code block mod methods
	// End of user code block mod methods

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
			BiConsumer<T, CustomPayloadEvent.Context> messageConsumer) {
		PACKET_HANDLER.messageBuilder(messageType, messageID)
			.encoder(encoder)
			.decoder(decoder)
			.consumerMainThread(messageConsumer)
			.add();
		messageID++;
	}

	<#-- Wait procedure block support below -->
	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent public void tick(TickEvent.ServerTickEvent.Post event) {
		List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setValue(work.getValue() - 1);
			if (work.getValue() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getKey().run());
		workQueue.removeAll(actions);
	}

}
<#-- @formatter:on -->
