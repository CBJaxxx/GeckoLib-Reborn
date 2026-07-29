/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.util;

import java.util.List;
import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

public interface INBTBuilder {
    default Builder nbt() {
        return new Builder();
    }

    public static class Builder implements INBTBuilder {
        private final CompoundTag tag = new CompoundTag();

        public CompoundTag build() {
            return tag;
        }

        public Builder tag(String key, Tag value) {
            tag.m_128365_(key, value);
            return this;
        }

        public Builder putByte(String key, byte value) {
           tag.m_128344_(key, value);
           return this;
        }

        public Builder putShort(String key, short value) {
            tag.m_128376_(key, value);
            return this;
        }

        public Builder putInt(String key, int value) {
            tag.m_128405_(key, value);
            return this;
        }

        public Builder putLong(String key, long value) {
            tag.m_128356_(key, value);
            return this;
        }

        public Builder putFloat(String key, float value) {
            tag.m_128350_(key, value);
            return this;
        }

        public Builder putDouble(String key, double value) {
            tag.m_128347_(key, value);
            return this;
        }

        public Builder putByteArray(String key, byte... value) {
            tag.m_128382_(key, value);
            return this;
        }

        /** @deprecated Use {@link #putByteArray(String, byte...)} */
        @Deprecated(forRemoval = true, since = "1.21.5")
        public Builder putByteArray(String key, List<Byte> value) {
            var array = new byte[value.size()];
            for (var i = 0; i < value.size(); i++)
                array[i] = value.get(i);

            return this.putByteArray(key, array);
        }

        public Builder putIntArray(String key, int... value) {
            tag.m_128385_(key, value);
            return this;
        }

        /** @deprecated Use {@link #putIntArray(String, int...)} */
        @Deprecated(forRemoval = true, since = "1.21.5")
        public Builder putIntArray(String key, List<Integer> value) {
            var array = new int[value.size()];
            for (var i = 0; i < value.size(); i++)
                array[i] = value.get(i);

            return this.putIntArray(key, array);
        }

        public Builder putLongArray(String key, long... value) {
            tag.m_128388_(key, value);
            return this;
        }

        /** @deprecated Use {@link #putLongArray(String, long...)} */
        @Deprecated(forRemoval = true, since = "1.21.5")
        public Builder putLongArray(String key, List<Long> value) {
            var array = new long[value.size()];
            for (var i = 0; i < value.size(); i++)
                array[i] = value.get(i);

            return this.putLongArray(key, array);
        }

        public Builder put(String key, boolean value) {
            tag.m_128379_(key, value);
            return this;
        }

        public Builder put(String key, String value) {
            tag.m_128359_(key, value);
            return this;
        }

        public Builder put(String key, UUID value) {
            tag.m_128385_(key, UUIDUtil.m_235881_(value));
            return this;
        }
    }
}
