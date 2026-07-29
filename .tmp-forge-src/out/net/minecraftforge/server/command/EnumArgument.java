/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumArgument<T extends Enum<T>> implements ArgumentType<T> {
    private static final Dynamic2CommandExceptionType INVALID_ENUM = new Dynamic2CommandExceptionType(
            (found, constants) -> Component.m_237110_("commands.forge.arguments.enum.invalid", constants, found));
    private final Class<T> enumClass;

    public static <R extends Enum<R>> EnumArgument<R> enumArgument(Class<R> enumClass) {
        return new EnumArgument<>(enumClass);
    }
    private EnumArgument(final Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(final StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        try {
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException e) {
            throw INVALID_ENUM.createWithContext(reader, name, Arrays.toString(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray()));
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return SharedSuggestionProvider.m_82981_(Stream.of(enumClass.getEnumConstants()).map(Enum::name), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return Stream.of(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }

    public static class Info<T extends Enum<T>> implements ArgumentTypeInfo<EnumArgument<T>, Info<T>.Template>
    {
        @Override
        public void m_214155_(Template template, FriendlyByteBuf buffer)
        {
            buffer.m_130070_(template.enumClass.getName());
        }

        @SuppressWarnings("unchecked")
        @Override
        public Template m_213618_(FriendlyByteBuf buffer)
        {
            try
            {
                String name = buffer.m_130277_();
                return new Template((Class<T>) Class.forName(name));
            }
            catch (ClassNotFoundException e)
            {
                return null;
            }
        }

        @Override
        public void m_213719_(Template template, JsonObject json)
        {
            json.addProperty("enum", template.enumClass.getName());
        }

        @Override
        public Template m_214163_(EnumArgument<T> argument)
        {
            return new Template(argument.enumClass);
        }

        public class Template implements ArgumentTypeInfo.Template<EnumArgument<T>>
        {
            final Class<T> enumClass;

            Template(Class<T> enumClass)
            {
                this.enumClass = enumClass;
            }

            @Override
            public EnumArgument<T> m_213879_(CommandBuildContext p_223435_)
            {
                return new EnumArgument<>(this.enumClass);
            }

            @Override
            public ArgumentTypeInfo<EnumArgument<T>, ?> m_213709_()
            {
                return Info.this;
            }
        }
    }
}
