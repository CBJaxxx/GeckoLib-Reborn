/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common;

import java.util.ArrayList;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;

public class DungeonHooks {
    private static WeightedList<EntityType<?>> dungeonMobs = WeightedList.<EntityType<?>>m_384576_()
        .m_385754_(EntityType.f_20524_, 100)
        .m_385754_(EntityType.f_20501_, 200)
        .m_385754_(EntityType.f_20479_, 100)
        .m_384449_();

    /**
     * Adds a mob to the possible list of creatures the spawner will create.
     * If the mob is already in the spawn list, the rarity will be added to the existing one,
     * causing the mob to be more common.
     *
     * @param type Monster type
     * @param rarity The rarity of selecting this mob over others. Must be greater then 0.
     *        Vanilla Minecraft has the following mobs:
     *        Spider   100
     *        Skeleton 100
     *        Zombie   200
     *        Meaning, Zombies are twice as common as spiders or skeletons.
     * @return The new rarity of the monster,
     */
    public static float addDungeonMob(EntityType<?> type, int rarity)
    {
        if (rarity <= 0)
            throw new IllegalArgumentException("Rarity must be greater then zero");

        var list = new ArrayList<>(dungeonMobs.m_387721_());
        var itr = list.iterator();
        while (itr.hasNext()) {
            var mob = itr.next();
            if (type == mob.f_380007_()) {
                itr.remove();
                rarity = mob.f_380172_() + rarity;
                break;
            }
        }

        dungeonMobs = WeightedList.<EntityType<?>>m_384576_().addAll(list).m_385754_(type, rarity).m_384449_();
        return rarity;
    }

    /**
     * Will completely remove a Mob from the dungeon spawn list.
     *
     * @param name The name of the mob to remove
     * @return The rarity of the removed mob, prior to being removed.
     */
    public static int removeDungeonMob(EntityType<?> name) {
        var list = new ArrayList<>(dungeonMobs.m_387721_());
        for (var mob : list) {
            if (mob.f_380007_() != name) continue;

            list.remove(mob);
            dungeonMobs = WeightedList.<EntityType<?>>m_384576_().addAll(list).m_384449_();
            return mob.f_380172_();
        }

        return 0;
    }

    /**
     * Gets a random mob name from the list.
     * @param rand World generation random number generator
     * @return The mob name
     */
    public static EntityType<?> getRandomDungeonMob(RandomSource rand) {
        return dungeonMobs.m_387013_(rand);
    }
}
