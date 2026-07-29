/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.command;

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraftforge.common.WorldWorkerManager.IWorker;

public class ChunkGenWorker implements IWorker {
    private final CommandSourceStack listener;
    protected final BlockPos start;
    protected final int total;
    private final ServerLevel dim;
    private final Queue<BlockPos> queue;
    private final int notificationFrequency;
    private int lastNotification = 0;
    private long lastNotifcationTime = 0;
    private int genned = 0;

    public ChunkGenWorker(CommandSourceStack listener, BlockPos start, int total, ServerLevel dim, int interval) {
        this.listener = listener;
        this.start = start;
        this.total = total;
        this.dim  = dim;
        this.queue = buildQueue();
        this.notificationFrequency = interval != -1 ? interval : Math.max(total / 20, 100); //Every 5% or every 100, whichever is more.
        this.lastNotifcationTime = System.currentTimeMillis(); //We also notify at least once every 60 seconds, to show we haven't froze.
    }

    protected Queue<BlockPos> buildQueue() {
        Queue<BlockPos> ret = new ArrayDeque<BlockPos>();
        ret.add(start);

        //This *should* spiral outwards, starting on right side, down, left, up, right, but hey we'll see!
        int radius = 1;
        while (ret.size() < total) {
            for (int q = -radius + 1; q <= radius && ret.size() < total; q++)
                ret.add(start.m_7918_(radius, 0, q));

            for (int q = radius - 1; q >= -radius && ret.size() < total; q--)
                ret.add(start.m_7918_(q, 0, radius));

            for (int q = radius - 1; q >= -radius && ret.size() < total; q--)
                ret.add(start.m_7918_(-radius, 0, q));

            for (int q = -radius + 1; q <= radius && ret.size() < total; q++)
                ret.add(start.m_7918_(q, 0, -radius));

            radius++;
        }
        return ret;
    }

    public MutableComponent getStartMessage(CommandSourceStack sender) {
        return Component.m_237110_("commands.forge.gen.start", total, start.m_123341_(), start.m_123343_(), dim);
    }

    @Override
    public boolean hasWork() {
        return !queue.isEmpty();
    }

    @Override
    public boolean doWork() {
        /* TODO: Check how many things are pending save, and slow down world gen if to many
        AnvilChunkLoader loader = dim.getChunkProvider().chunkLoader instanceof AnvilChunkLoader ? (AnvilChunkLoader)world.getChunkProvider().chunkLoader : null;
        if (loader != null && loader.getPendingSaveCount() > 100) {
            if (lastNotifcationTime < System.currentTimeMillis() - 10*1000) {
                listener.sendFeedback(new TranslationTextComponent("commands.forge.gen.progress", total - queue.size(), total), true);
                lastNotifcationTime = System.currentTimeMillis();
            }
            return false;
        }
        */

        BlockPos next = queue.poll();

        if (next != null) {
            if (++lastNotification >= notificationFrequency || lastNotifcationTime < System.currentTimeMillis() - 60*1000) {
                listener.m_288197_(() -> Component.m_237110_("commands.forge.gen.progress", total - queue.size(), total), true);
                lastNotification = 0;
                lastNotifcationTime = System.currentTimeMillis();
            }

            int x = next.m_123341_();
            int z = next.m_123343_();

            if (!dim.m_7232_(x, z)) { //Chunk is unloaded
                ChunkAccess chunk = dim.m_6522_(x, z, ChunkStatus.f_314297_, true);
                if (!chunk.m_6415_().m_319325_(ChunkStatus.f_315432_)) {
                    chunk = dim.m_46819_(x, z, ChunkStatus.f_315432_);
                    genned++; //There isn't a way to check if the chunk is actually created just if it was loaded
                }
            }
        }

        if (queue.isEmpty()) {
            listener.m_288197_(() -> Component.m_237110_("commands.forge.gen.complete", genned, total, dim.m_46472_().m_135782_()), true);
            return false;
        }
        return true;
    }
}
