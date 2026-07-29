/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.network.packets;

import java.util.UUID;

import org.jetbrains.annotations.ApiStatus;

import io.netty.buffer.Unpooled;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Used to spawn a custom entity without the same restrictions as
 * {@link ClientboundAddEntityPacket}
 * <p>
 * To customize how your entity is created clientside (instead of using the default factory provided to the
 * {@link EntityType})
 * see {@link EntityType.Builder#setCustomClientFactory}.
 */
// TODO: Re-write this packet into something simpler. This is literally just ClientboundAddEntityPacket with an extra byte[]
public class SpawnEntity {
    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnEntity> STREAM_CODEC = StreamCodec.m_324771_(SpawnEntity::encode, SpawnEntity::decode);
    private final Entity entity;
    private final int typeId;
    private final int entityId;
    private final UUID uuid;
    private final double posX, posY, posZ;
    private final byte pitch, yaw, headYaw;
    private final int velX, velY, velZ;
    private final FriendlyByteBuf buf;

    @ApiStatus.Internal
    public SpawnEntity(Entity e) {
        this.entity = e;
        this.typeId = BuiltInRegistries.f_256780_.m_7447_(e.m_6095_()); //TODO: Codecs
        this.entityId = e.m_19879_();
        this.uuid = e.m_20148_();
        this.posX = e.m_20185_();
        this.posY = e.m_20186_();
        this.posZ = e.m_20189_();
        this.pitch = (byte) Mth.m_14143_(e.m_146909_() * 256.0F / 360.0F);
        this.yaw = (byte) Mth.m_14143_(e.m_146908_() * 256.0F / 360.0F);
        this.headYaw = (byte) (e.m_6080_() * 256.0F / 360.0F);
        Vec3 vec3d = e.m_20184_();
        double d1 = Mth.m_14008_(vec3d.f_82479_, -3.9D, 3.9D);
        double d2 = Mth.m_14008_(vec3d.f_82480_, -3.9D, 3.9D);
        double d3 = Mth.m_14008_(vec3d.f_82481_, -3.9D, 3.9D);
        this.velX = (int) (d1 * 8000.0D);
        this.velY = (int) (d2 * 8000.0D);
        this.velZ = (int) (d3 * 8000.0D);
        this.buf = null;
    }

    private SpawnEntity(int typeId, int entityId, UUID uuid, double posX, double posY, double posZ, byte pitch, byte yaw, byte headYaw, int velX, int velY, int velZ, FriendlyByteBuf buf) {
        this.entity = null;
        this.typeId = typeId;
        this.entityId = entityId;
        this.uuid = uuid;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.buf = buf;
    }

    public static void encode(SpawnEntity msg, FriendlyByteBuf buf) {
        buf.m_130130_(msg.typeId);
        buf.writeInt(msg.entityId);
        buf.writeLong(msg.uuid.getMostSignificantBits());
        buf.writeLong(msg.uuid.getLeastSignificantBits());
        buf.writeDouble(msg.posX);
        buf.writeDouble(msg.posY);
        buf.writeDouble(msg.posZ);
        buf.writeByte(msg.pitch);
        buf.writeByte(msg.yaw);
        buf.writeByte(msg.headYaw);
        buf.writeShort(msg.velX);
        buf.writeShort(msg.velY);
        buf.writeShort(msg.velZ);
        if (msg.entity instanceof IEntityAdditionalSpawnData entityAdditionalSpawnData) {
            final FriendlyByteBuf spawnDataBuffer = new FriendlyByteBuf(Unpooled.buffer());

            entityAdditionalSpawnData.writeSpawnData(spawnDataBuffer);

            buf.m_130130_(spawnDataBuffer.readableBytes());
            buf.writeBytes(spawnDataBuffer);

            spawnDataBuffer.release();
        } else {
            buf.m_130130_(0);
        }
    }

    public static SpawnEntity decode(FriendlyByteBuf buf) {
        return new SpawnEntity(buf.m_130242_(), buf.readInt(), new UUID(buf.readLong(), buf.readLong()), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readShort(), buf.readShort(), buf.readShort(), readSpawnDataPacket(buf));
    }

    private static FriendlyByteBuf readSpawnDataPacket(FriendlyByteBuf buf) {
        final int count = buf.m_130242_();
        if (count > 0) {
            final FriendlyByteBuf spawnDataBuffer = new FriendlyByteBuf(Unpooled.buffer());
            spawnDataBuffer.writeBytes(buf, count);
            return spawnDataBuffer;
        }

        return new FriendlyByteBuf(Unpooled.buffer());
    }

    public static void handle(SpawnEntity msg, CustomPayloadEvent.Context ctx) {
        try {
            EntityType<?> type = BuiltInRegistries.f_256780_.m_7942_(msg.typeId);
            var world = LogicalSidedProvider.CLIENTWORLD.get(ctx.isClientSide());
            Entity e = world.map(w -> type.customClientSpawn(msg, w)).orElse(null);
            if (e == null)
                return;

            /*
             * Sets the postiion on the client, Mirrors what
             * Entity#recreateFromPacket and LivingEntity#recreateFromPacket does.
             */
            e.m_217006_(msg.posX, msg.posY, msg.posZ);
            e.m_388475_(msg.posX, msg.posY, msg.posZ, (msg.yaw * 360) / 256.0F, (msg.pitch * 360) / 256.0F);
            e.m_5616_((msg.headYaw * 360) / 256.0F);
            e.m_5618_((msg.headYaw * 360) / 256.0F);

            e.m_20234_(msg.entityId);
            e.m_20084_(msg.uuid);
            if (world.orElse(null) instanceof ClientLevel cworld)
                cworld.m_104739_(e);
            e.m_6001_(msg.velX / 8000.0, msg.velY / 8000.0, msg.velZ / 8000.0);
            if (e instanceof IEntityAdditionalSpawnData entityAdditionalSpawnData)
                entityAdditionalSpawnData.readSpawnData(msg.buf);
        } finally {
            msg.buf.release();
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public byte getPitch() {
        return pitch;
    }

    public byte getYaw() {
        return yaw;
    }

    public byte getHeadYaw() {
        return headYaw;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public int getVelZ() {
        return velZ;
    }

    public FriendlyByteBuf getAdditionalData() {
        return buf;
    }
}