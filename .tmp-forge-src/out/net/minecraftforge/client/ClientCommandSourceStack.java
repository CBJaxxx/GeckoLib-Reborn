/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * overrides for {@link CommandSourceStack} so that the methods will run successfully client side
 */
public class ClientCommandSourceStack extends CommandSourceStack {
    public ClientCommandSourceStack(CommandSource source, Vec3 position, Vec2 rotation, int permission, String plainTextName, Component displayName, Entity executing) {
        super(source, position, rotation, null, permission, plainTextName, displayName, null, executing);
    }

    /**
     * Sends a success message without attempting to get the server side list of admins
     */
    @SuppressWarnings("resource")
    @Override
    public void m_288197_(Supplier<Component> message, boolean sendToAdmins) {
        Minecraft.m_91087_().f_91065_.m_93076_().m_93785_(message.get());
    }

    /**
     * {@return the list of teams from the client side}
     */
    @SuppressWarnings("resource")
    @Override
    public Collection<String> m_5983_() {
        return Minecraft.m_91087_().f_91073_.m_6188_().m_83488_();
    }

    /**
     * {@return the list of online player names from the client side}
     */
    @Override
    public Collection<String> m_5982_() {
        return Minecraft.m_91087_().m_91403_().m_105142_().stream().map((player) -> player.m_105312_().getName()).collect(Collectors.toList());
    }

    /**
     * {@return a set of {@link ResourceKey} for levels from the client side}
     */
    @Override
    public Set<ResourceKey<Level>> m_6553_() {
        return Minecraft.m_91087_().m_91403_().m_105151_();
    }

    /**
     * {@return the {@link RegistryAccess} from the client side}
     */
    @Override
    public RegistryAccess m_5894_() {
        return Minecraft.m_91087_().m_91403_().m_105152_();
    }

    /**
     * {@return the scoreboard from the client side}
     */
    @SuppressWarnings("resource")
    @Override
    public Scoreboard getScoreboard() {
        return Minecraft.m_91087_().f_91073_.m_6188_();
    }

    /**
     * {@return the advancement from the id from the client side where the advancement needs to be visible to the player}
     */
    @Override
    public AdvancementHolder getAdvancement(ResourceLocation id) {
        return Minecraft.m_91087_().m_91403_().m_105145_().m_295984_(id);
    }

    /**
     * {@return the level from the client side}
     */
    @SuppressWarnings("resource")
    @Override
    public Level getUnsidedLevel() {
        return Minecraft.m_91087_().f_91073_;
    }

    /**
     * @throws UnsupportedOperationException
     *             because the server isn't available on the client
     */
    @Override
    public MinecraftServer m_81377_() {
        throw new UnsupportedOperationException("Attempted to get server in client command");
    }

    /**
     * @throws UnsupportedOperationException
     *             because the server side level isn't available on the client side
     */
    @Override
    public ServerLevel m_81372_() {
        throw new UnsupportedOperationException("Attempted to get server level in client command");
    }
}
