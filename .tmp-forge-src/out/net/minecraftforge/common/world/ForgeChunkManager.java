/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import org.jetbrains.annotations.Unmodifiable;

/** @deprecated Vanilla now handles all forced chunks using its new {@link TicketType} system. */
@Deprecated(forRemoval = true, since = "1.21.5")
public class ForgeChunkManager {
    private static <T> T error() {
        throw new UnsupportedOperationException("Mod used ForgeChnkManager when they should use vanilla's TicketType system, see Level.getChunkSource().addTicketWithRadius");
    }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    public static void setForcedChunkLoadingCallback(String modId, LoadingValidationCallback callback) { error(); }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    public static boolean hasForcedChunks(ServerLevel level) { return error(); }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    public static boolean forceChunk(ServerLevel level, String modId, BlockPos owner, int chunkX, int chunkZ, boolean add, boolean ticking) { return error(); }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    public static boolean forceChunk(ServerLevel level, String modId, Entity owner, int chunkX, int chunkZ, boolean add, boolean ticking) { return error(); }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    public static boolean forceChunk(ServerLevel level, String modId, UUID owner, int chunkX, int chunkZ, boolean add, boolean ticking) { return error(); }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    @FunctionalInterface
    @Deprecated(forRemoval = true, since = "1.21.5")
    public interface LoadingValidationCallback {
        void validateTickets(ServerLevel level, TicketHelper ticketHelper);
    }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    @Deprecated(forRemoval = true, since = "1.21.5")
    public static class TicketHelper {
        private TicketHelper() { }
        /** @deprecated Use Vanilla's {@link TicketType} system */
        public @Unmodifiable Map<BlockPos, Pair<LongSet, LongSet>> getBlockTickets() { return error(); }
        /** @deprecated Use Vanilla's {@link TicketType} system */
        public @Unmodifiable Map<UUID, Pair<LongSet, LongSet>> getEntityTickets() { return error(); }
        /** @deprecated Use Vanilla's {@link TicketType} system */
        public void removeAllTickets(BlockPos owner) { error(); }
        /** @deprecated Use Vanilla's {@link TicketType} system */
        public void removeAllTickets(UUID owner) { error(); }
        /** @deprecated Use Vanilla's {@link TicketType} system */
        public void removeTicket(BlockPos owner, long chunk, boolean ticking) { error(); }
        /** @deprecated Use Vanilla's {@link TicketType} system */
        public void removeTicket(UUID owner, long chunk, boolean ticking) { error(); }
    }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    @Deprecated(forRemoval = true, since = "1.21.5")
    public static class TicketOwner implements Comparable<TicketOwner> {
        private TicketOwner() { }
        @Override public int compareTo(TicketOwner o) { return error(); }
    }

    /** @deprecated Use Vanilla's {@link TicketType} system */
    @Deprecated(forRemoval = true, since = "1.21.5")
    public static class TicketTracker {
        public Map<TicketOwner, LongSet> getChunks() { return error(); }
        public boolean isEmpty() { return error(); }
    }
}
