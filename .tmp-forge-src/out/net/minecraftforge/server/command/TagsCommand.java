/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code /forge tags} command for listing a registry's tags, getting the elements of tags, and querying the tags of a
 * registry object.
 *
 * <p>Each command is paginated, showing {@value PAGE_SIZE} entries at a time. When there are more than 0 entries,
 * the text indicating the amount of entries is highlighted and can be clicked to copy the list of all entries (across
 * all pages) to the clipboard. (This is reflected by the use of green text in brackets, mimicking the clickable
 * coordinates in the {@code /locate} command's message)</p>
 *
 * <p>The command has three subcommands:</p>
 * <ul>
 *     <li>{@code /forge tags &lt;registry> list [page]} - Lists all available tags in the given registry.</li>
 *     <li>{@code /forge tags &lt;registry> get &lt;tag> [page]} - Gets all elements of the given tag in the given registry.</li>
 *     <li>{@code /forge tags &lt;registry> query &lt;element> [page]} - Queries for all tags in the given registry which
 *     contain the given registry object.</li>
 * </ul>
 */
class TagsCommand {
    private static final long PAGE_SIZE = 8;
    private static final ResourceKey<Registry<Registry<?>>> ROOT_REGISTRY_KEY =
            ResourceKey.m_135788_(ResourceLocation.m_338530_("root"));

    private static final DynamicCommandExceptionType UNKNOWN_REGISTRY = new DynamicCommandExceptionType(key ->
            Component.m_237110_("commands.forge.tags.error.unknown_registry", key));
    private static final Dynamic2CommandExceptionType UNKNOWN_TAG = new Dynamic2CommandExceptionType((tag, registry) ->
            Component.m_237110_("commands.forge.tags.error.unknown_tag", tag, registry));
    private static final Dynamic2CommandExceptionType UNKNOWN_ELEMENT = new Dynamic2CommandExceptionType((tag, registry) ->
            Component.m_237110_("commands.forge.tags.error.unknown_element", tag, registry));

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        /*
         * /forge tags <registry> list [page]
         * /forge tags <registry> get <tag> [page]
         * /forge tags <registry> query <element> [page]
         */
        return Commands.m_82127_("tags")
                .requires(cs -> cs.m_81369_(2))
                .then(Commands.m_82129_("registry", ResourceKeyArgument.m_212386_(ROOT_REGISTRY_KEY))
                        .suggests(TagsCommand::suggestRegistries)
                        .then(Commands.m_82127_("list")
                                .executes(ctx -> listTags(ctx, 1))
                                .then(Commands.m_82129_("page", IntegerArgumentType.integer(1))
                                        .executes(ctx -> listTags(ctx, IntegerArgumentType.getInteger(ctx, "page")))
                                )
                        )
                        .then(Commands.m_82127_("get")
                                .then(Commands.m_82129_("tag", ResourceLocationArgument.m_106984_())
                                        .suggests(suggestFromRegistry(r -> r.m_203612_().map(t -> t.m_205839_().f_203868_())::iterator))
                                        .executes(ctx -> listTagElements(ctx, 1))
                                        .then(Commands.m_82129_("page", IntegerArgumentType.integer(1))
                                                .executes(ctx -> listTagElements(ctx, IntegerArgumentType.getInteger(ctx, "page")))
                                        )
                                )
                        )
                        .then(Commands.m_82127_("query")
                                .then(Commands.m_82129_("element", ResourceLocationArgument.m_106984_())
                                        .suggests(suggestFromRegistry(Registry::m_6566_))
                                        .executes(ctx -> queryElementTags(ctx, 1))
                                        .then(Commands.m_82129_("page", IntegerArgumentType.integer(1))
                                                .executes(ctx -> queryElementTags(ctx, IntegerArgumentType.getInteger(ctx, "page")))
                                        )
                                )
                        )
                );
    }

    private static int listTags(final CommandContext<CommandSourceStack> ctx, final int page) throws CommandSyntaxException {
        final ResourceKey<? extends Registry<?>> registryKey = getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY)
                .orElseThrow(); // Expect to be always retrieve a resource key for the root registry (registry key)
        final Registry<?> registry = ctx.getSource().m_81377_().m_206579_().m_206219_(registryKey)
                .orElseThrow(() -> UNKNOWN_REGISTRY.create(registryKey.m_135782_()));

        final long tagCount = registry.m_203612_().count();

        ctx.getSource().m_288197_(() -> createMessage(
                Component.m_237110_("commands.forge.tags.registry_key", Component.m_237113_(registryKey.m_135782_().toString()).m_130940_(ChatFormatting.GOLD)),
                "commands.forge.tags.tag_count",
                "commands.forge.tags.copy_tag_names",
                tagCount,
                page,
                ChatFormatting.DARK_GREEN,
                () -> registry.m_203612_()
                        .map(s -> s.m_203440_().map(k -> k.f_203868_().toString(), Object::toString))
        ), false);

        return (int) tagCount;
    }

    private static int listTagElements(final CommandContext<CommandSourceStack> ctx, final int page) throws CommandSyntaxException
    {
        final ResourceKey<? extends Registry<?>> registryKey = getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY)
                .orElseThrow(); // Expect to be always retrieve a resource key for the root registry (registry key)
        final Registry<Object> registry = ctx.getSource().m_81377_().m_206579_().m_206219_(registryKey)
                .orElseThrow(() -> UNKNOWN_REGISTRY.create(registryKey.m_135782_()));

        final ResourceLocation tagLocation = ResourceLocationArgument.m_107011_(ctx, "tag");
        final TagKey<Object> tagKey = TagKey.m_203882_(cast(registryKey), tagLocation);

        final HolderSet.Named<?> tag = registry.m_255050_(tagKey)
                .orElseThrow(() -> UNKNOWN_TAG.create(tagKey.f_203868_(), registryKey.m_135782_()));

        ctx.getSource().m_288197_(() -> createMessage(
                Component.m_237110_("commands.forge.tags.tag_key",
                        Component.m_237113_(tagKey.f_203867_().m_135782_().toString()).m_130940_(ChatFormatting.GOLD),
                        Component.m_237113_(tagKey.f_203868_().toString()).m_130940_(ChatFormatting.DARK_GREEN)),
                "commands.forge.tags.element_count",
                "commands.forge.tags.copy_element_names",
                tag.m_203632_(),
                page,
                ChatFormatting.YELLOW,
                () -> tag.m_203614_().map(s -> s.m_203439_().map(k -> k.m_135782_().toString(), Object::toString))
        ), false);

        return tag.m_203632_();
    }

    private static <T> int queryElementTags(final CommandContext<CommandSourceStack> ctx, final int page) throws CommandSyntaxException {
        final ResourceKey<? extends Registry<?>> registryKey = getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY)
                .orElseThrow(); // Expect to be always retrieve a resource key for the root registry (registry key)
        final Registry<Object> registry = ctx.getSource().m_81377_().m_206579_().m_206219_(registryKey)
                .orElseThrow(() -> UNKNOWN_REGISTRY.create(registryKey.m_135782_()));

        final ResourceLocation elementLocation = ResourceLocationArgument.m_107011_(ctx, "element");
        final ResourceKey<Object> elementKey = ResourceKey.m_135785_(cast(registryKey), elementLocation);

        final Holder<?> elementHolder = registry.m_205904_(elementKey)
                .orElseThrow(() -> UNKNOWN_ELEMENT.create(elementLocation, registryKey.m_135782_()));

        final long containingTagsCount = elementHolder.m_203616_().count();

        ctx.getSource().m_288197_(() -> createMessage(
                Component.m_237110_("commands.forge.tags.element",
                        Component.m_237113_(registryKey.m_135782_().toString()).m_130940_(ChatFormatting.GOLD),
                        Component.m_237113_(elementLocation.toString()).m_130940_(ChatFormatting.YELLOW)),
                "commands.forge.tags.containing_tag_count",
                "commands.forge.tags.copy_tag_names",
                containingTagsCount,
                page,
                ChatFormatting.DARK_GREEN,
                () -> elementHolder.m_203616_().map(k -> k.f_203868_().toString())
        ), false);

        return (int) containingTagsCount;
    }

    private static MutableComponent createMessage(final MutableComponent header,
            final String containsText,
            final String copyHoverText,
            final long count,
            final long currentPage,
            final ChatFormatting elementColor,
            final Supplier<Stream<String>> names
    ) {
        final String allElementNames = names.get().sorted().collect(Collectors.joining("\n"));
        final long totalPages = (count - 1) / PAGE_SIZE + 1;
        final long actualPage = (long) Mth.m_295574_(currentPage, 1, totalPages);

        MutableComponent containsComponent = Component.m_237110_(containsText, count);
        if (count > 0) { // Highlight the count text, make it clickable, and append page counters
            containsComponent = ComponentUtils.m_130748_(containsComponent.m_130938_(s -> s
                    .m_131140_(ChatFormatting.GREEN)
                    .m_131142_(new ClickEvent.CopyToClipboard(allElementNames))
                    .m_131144_(new HoverEvent.ShowText(Component.m_237115_(copyHoverText)))));
            containsComponent = Component.m_237110_("commands.forge.tags.page_info",
                    containsComponent, actualPage, totalPages);
        }

        final MutableComponent tagElements = Component.m_237113_("").m_7220_(containsComponent);
        names.get()
                .sorted()
                .skip(PAGE_SIZE * (actualPage - 1))
                .limit(PAGE_SIZE)
                .map(Component::m_237113_)
                .map(t -> t.m_130940_(elementColor))
                .map(t -> Component.m_237115_("\n - ").m_7220_(t))
                .forEach(tagElements::m_7220_);

        return header.m_130946_("\n").m_7220_(tagElements);
    }

    private static CompletableFuture<Suggestions> suggestRegistries(
        final CommandContext<CommandSourceStack> ctx,
        final SuggestionsBuilder builder
    ) {
        ctx.getSource().m_5894_().m_206193_()
                .map(RegistryAccess.RegistryEntry::f_206233_)
                .map(ResourceKey::m_135782_)
                .map(ResourceLocation::toString)
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static SuggestionProvider<CommandSourceStack> suggestFromRegistry(final Function<Registry<?>, Iterable<ResourceLocation>> namesFunction) {
        return (ctx, builder) -> {
            var key = getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY).orElse(null);
            if (key == null)
                return builder.buildFuture();

            var reg = ctx.getSource().m_5894_().m_206219_(key).orElse(null);
            if (reg == null)
                return builder.buildFuture();

            SharedSuggestionProvider.m_82926_(namesFunction.apply(reg), builder);
            return builder.buildFuture();
        };
    }

    private static <T> Optional<ResourceKey<T>> getResourceKey(
        final CommandContext<CommandSourceStack> ctx,
        final String name,
        final ResourceKey<Registry<T>> registryKey
    ) {
        // Don't inline to avoid an unchecked cast warning due to raw types
        final ResourceKey<?> key = ctx.getArgument(name, ResourceKey.class);
        return key.m_195975_(registryKey);
    }

    @SuppressWarnings("unchecked")
    private static <O> O cast(final Object input) {
        return (O)input;
    }
}

