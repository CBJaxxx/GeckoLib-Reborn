/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/*
 * Extension added to ItemStack that bounces to ItemSack sensitive Item methods. Typically this is just for convince.
 */
public interface IForgeItemStack {
    // Helpers for accessing Item data
    private ItemStack self() {
        return (ItemStack)this;
    }

    /**
     * ItemStack sensitive version of {@link Item#getCraftingRemainder()}.
     * Returns a full ItemStack instance of the result.
     *
     * @return The resulting ItemStack
     */
    default ItemStack getCraftingRemainder() {
        return self().m_41720_().getCraftingRemainder(self());
    }

    /**
     * @return the fuel burn time for this itemStack in a furnace. Return 0 to make
     *         it not act as a fuel. Return -1 to let the default vanilla logic
     *         decide.
     */
    default int getBurnTime(@Nullable RecipeType<?> recipeType) {
        return self().m_41720_().getBurnTime(self(), recipeType);
    }

    default InteractionResult onItemUseFirst(UseOnContext context) {
       Player entityplayer = context.m_43723_();
       BlockPos blockpos = context.m_8083_();
       BlockInWorld blockworldstate = new BlockInWorld(context.m_43725_(), blockpos, false);
       var adventure = (self().m_323082_(blockworldstate) || self().m_321400_(blockworldstate));
       if (entityplayer != null && !entityplayer.m_150110_().f_35938_ && !adventure) {
          return InteractionResult.f_19070_;
       } else {
          Item item = self().m_41720_();
          InteractionResult enumactionresult = item.onItemUseFirst(self(), context);
          if (entityplayer != null && enumactionresult == InteractionResult.f_19068_) {
             entityplayer.m_36246_(Stats.f_12982_.m_12902_(item));
          }

          return enumactionresult;
       }
    }

    /**
     * Queries if an item can perform the given action.
     * See {@link ToolActions} for a description of each stock action
     * @param toolAction The action being queried
     * @return True if the stack can perform the action
     */
    default boolean canPerformAction(ToolAction toolAction) {
        return self().m_41720_().canPerformAction(self(), toolAction);
    }

    /**
     * Called before a block is broken. Return true to prevent default block
     * harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param pos       Block's position in world
     * @param player    The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    default boolean onBlockStartBreak(BlockPos pos, Player player) {
        return !self().m_41619_() && self().m_41720_().onBlockStartBreak(self(), pos, player);
    }

    /**
     * Called when the player is mining a block and the item in his hand changes.
     * Allows to not reset blockbreaking if only NBT or similar changes.
     *
     * @param newStack The new stack
     * @return True to reset block break progress
     */
    default boolean shouldCauseBlockBreakReset(ItemStack newStack) {
        return self().m_41720_().shouldCauseBlockBreakReset(self(), newStack);
    }

    /**
     * Checks whether an item can be enchanted with a certain enchantment. This
     * applies specifically to enchanting an item in the enchanting table and is
     * called when retrieving the list of possible enchantments for an item.
     * Enchantments may additionally (or exclusively) be doing their own checks in
     * {@link Enchantment#canApplyAtEnchantingTable(ItemStack)};
     * check the individual implementation for reference. By default this will check
     * if the enchantment type is valid for this item type.
     *
     * @param enchantment the enchantment to be applied
     * @return true if the enchantment can be applied to this item
     *
     * @deprecated Use {@link #canApplyAtEnchantingTable(Holder)}
     */
    @Deprecated(forRemoval = true, since = "1.21.3")
    default boolean canApplyAtEnchantingTable(Enchantment enchantment) {
        return self().m_41720_().canApplyAtEnchantingTable(self(), enchantment);
    }

    /**
     * Checks whether an item can be enchanted with a certain enchantment. This
     * applies specifically to enchanting an item in the enchanting table and is
     * called when retrieving the list of possible enchantments for an item.
     * Enchantments may additionally (or exclusively) be doing their own checks in
     * {@link Enchantment#canApplyAtEnchantingTable(ItemStack)};
     * check the individual implementation for reference. By default this will check
     * if the enchantment type is valid for this item type.
     *
     * @param enchantment the enchantment to be applied
     * @return true if the enchantment can be applied to this item
     */
    default boolean canApplyAtEnchantingTable(Holder<Enchantment> enchantment) {
        return self().m_41720_().canApplyAtEnchantingTable(self(), enchantment);
    }


    /**
     * Override this to set a non-default armor slot for an ItemStack, but <em>do
     * not use this to get the armor slot of said stack; for that, use
     * {@link LivingEntity#getEquipmentSlotForItem(ItemStack)}.</em>
     *
     * @return the armor slot of the ItemStack, or {@code null} to let the default
     *         vanilla logic as per {@code LivingEntity.getSlotForItemStack(stack)}
     *         decide
     */
    @Nullable
    default EquipmentSlot getEquipmentSlot() {
        return self().m_41720_().getEquipmentSlot(self());
    }

    /**
     * Can this Item disable a shield
     *
     * @param shield   The shield in question
     * @param entity   The LivingEntity holding the shield
     * @param attacker The LivingEntity holding the ItemStack
     * @return True if this ItemStack can disable the shield in question.
     */
    default boolean canDisableShield(ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return self().m_41720_().canDisableShield(self(), shield, entity, attacker);
    }

    /**
     * Called when a entity tries to play the 'swing' animation.
     *
     * @param entity The entity swinging the item.
     * @return True to cancel any further processing by EntityLiving
     */
    default boolean onEntitySwing(LivingEntity entity) {
        return self().m_41720_().onEntitySwing(self(), entity);
    }

    /**
     * Called when an entity stops using an item item for any reason.
     *
     * @param entity The entity using the item, typically a player
     * @param count  The amount of time in tick the item has been used for continuously
     */
    default void onStopUsing(LivingEntity entity, int count) {
        self().m_41720_().onStopUsing(self(), entity, count);
    }

    /**
     * Retrieves the normal 'lifespan' of this item when it is dropped on the ground
     * as a EntityItem. This is in ticks, standard result is 6000, or 5 mins.
     *
     * @param level     The level the entity is in
     * @return The normal lifespan in ticks.
     */
    default int getEntityLifespan(Level level) {
        return self().m_41720_().getEntityLifespan(self(), level);
    }

    /**
     * Called by the default implemetation of EntityItem's onUpdate method, allowing
     * for cleaner control over the update of the item without having to write a
     * subclass.
     *
     * @param entity The entity Item
     * @return Return true to skip any further update code.
     */
    default boolean onEntityItemUpdate(ItemEntity entity) {
        return self().m_41720_().onEntityItemUpdate(self(), entity);
    }

    /**
     * Called to tick this items in a entitie's inventory, the slotIndex may be -1 for non player entities as they may not have a slot index.
     * See {@link EntityEquipment#tick(Entity)}
     */
    default void inventoryTick(Level level, Entity entity, @Nullable EquipmentSlot slot, int slotIndex) {
        self().m_41720_().inventoryTick(self(), level, entity, slot, slotIndex);
    }

    /**
     * Called every tick from {@code Horse#playGallopSound(SoundEvent)} on the item in the
     * armor slot.
     *
     * @param level the level the horse is in
     * @param horse the horse wearing this armor
     */
    default void onHorseArmorTick(Level level, Mob horse) {
        self().m_41720_().onHorseArmorTick(self(), level, horse);
    }

    /**
     * Determines if the specific ItemStack can be placed in the specified armor
     * slot, for the entity.
     *
     * @param armorType Armor slot to be verified.
     * @param entity    The entity trying to equip the armor
     * @return True if the given ItemStack can be inserted in the slot
     */
    default boolean canEquip(EquipmentSlot armorType, Entity entity) {
        return self().m_41720_().canEquip(self(), armorType, entity);
    }

    /**
     * Allow or forbid the specific book/item combination as an anvil enchant
     *
     * @param book  The book
     * @return if the enchantment is allowed
     */
    default boolean isBookEnchantable(ItemStack book) {
        return self().m_41720_().isBookEnchantable(self(), book);
    }


    /**
     * Called when a player drops the item into the world, returning false from this
     * will prevent the item from being removed from the players inventory and
     * spawning in the world
     *
     * @param player The player that dropped the item
     */
    default boolean onDroppedByPlayer(Player player) {
        return self().m_41720_().onDroppedByPlayer(self(), player);
    }

    /**
     * Allow the item one last chance to modify its name used for the tool highlight
     * useful for adding something extra that can't be removed by a user in the
     * displayed name, such as a mode of operation.
     *
     * @param displayName the name that will be displayed unless it is changed in
     *                    this method.
     */
    default Component getHighlightTip(Component displayName) {
        return self().m_41720_().getHighlightTip(self(), displayName);
    }

    /**
     *
     * Should this item, when held, allow sneak-clicks to pass through to the underlying block?
     *
     * @param level The level
     * @param pos Block position in level
     * @param player The Player that is wielding the item
     */
    default boolean doesSneakBypassUse(net.minecraft.world.level.LevelReader level, BlockPos pos, Player player) {
        return self().m_41619_() || self().m_41720_().doesSneakBypassUse(self(), level, pos, player);
    }

    /**
     * Called by Piglins when checking to see if they will give an item or something in exchange for this item.
     *
     * @return True if this item can be used as "currency" by piglins
     */
    default boolean isPiglinCurrency() {
        return self().m_41720_().isPiglinCurrency(self());
    }

    /**
     * Called by Piglins to check if a given item prevents hostility on sight. If this is true the Piglins will be neutral to the entity wearing this item, and will not
     * attack on sight. Note: This does not prevent Piglins from becoming hostile due to other actions, nor does it make Piglins that are already hostile stop being so.
     *
     * @param wearer The entity wearing this ItemStack
     *
     * @return True if piglins are neutral to players wearing this item in an armor slot
     */
    default boolean makesPiglinsNeutral(LivingEntity wearer) {
        return self().m_41720_().makesPiglinsNeutral(self(), wearer);
    }

    /**
     * Whether this Item can be used to hide player from enderman/Creaking.
     *
     * @param player The player watching the enderman
     * @param monster The monster you're trying to hide from
     * @return true if this Item can be used.
     */
    default boolean isMonsterDisguise(Player player, Monster monster) {
        return self().m_41720_().isMonsterDisguise(self(), player, monster);
    }

    /**
     * Used to determine if the player can use Elytra flight.
     * This is called Client and Server side.
     *
     * @param entity The entity trying to fly.
     * @return True if the entity can use Elytra flight.
     */
    default boolean canElytraFly(LivingEntity entity) {
        return self().m_41720_().canElytraFly(self(), entity);
    }

    /**
     * Used to determine if the player can continue Elytra flight,
     * this is called each tick, and can be used to apply ItemStack damage,
     * consume Energy, or what have you.
     * For example the Vanilla implementation of this, applies damage to the
     * ItemStack every 20 ticks.
     *
     * @param entity      The entity currently in Elytra flight.
     * @param flightTicks The number of ticks the entity has been Elytra flying for.
     * @return True if the entity should continue Elytra flight or False to stop.
     */
    default boolean elytraFlightTick(LivingEntity entity, int flightTicks) {
        return self().m_41720_().elytraFlightTick(self(), entity, flightTicks);
    }

    /**
     * Called by the powdered snow block to check if a living entity wearing this can walk on the snow, granting the same behavior as leather boots.
     * Only affects items worn in the boots slot.
     *
     * @param wearer The entity wearing this ItemStack
     *
     * @return True if the entity can walk on powdered snow
     */
    default boolean canWalkOnPowderedSnow(LivingEntity wearer) {
        return self().m_41720_().canWalkOnPowderedSnow(self(), wearer);
    }

    /**
     * Get a bounding box ({@link AABB}) of a sweep attack.
     *
     * @param player the performing the attack the attack.
     * @param target the entity targeted by the attack.
     * @return the bounding box.
     */
    @NotNull
    default AABB getSweepHitBox(@NotNull Player player, @NotNull Entity target) {
        return self().m_41720_().getSweepHitBox(self(), player, target);
    }

    /**
     * Called when this item is to be damaged, such as use a tool being used or a shield blocking damage. The damage
     * parameter has not yet been processed, so enchantments are not taken into account.
     *
     * @param damage   The amount of damage the item will take before processing
     * @param level    The level where the damage is taking place
     * @param player   The player holding the item
     * @param canBreak If the item can break from this damage instance ({@code true} if this is called from
     *                 {@link ItemStack#hurtAndBreak(int, ServerLevel, ServerPlayer, Consumer)}, {@code false} if from
     *                 {@link ItemStack#hurtWithoutBreaking(int, Player)})
     * @param onBroken The callback for when an item is broken (use this if you plan on cancelling damage that will
     *                 break an item)
     * @return The amount of damage the item should take
     *
     * @apiNote If the item stack is not {@linkplain ItemStack#isDamageableItem() damageable} or the player
     * {@linkplain Player#hasInfiniteMaterials() has infinite materials}, this method will not be called.
     * @see IForgeItem#damageItem(ItemStack, int, ServerLevel, ServerPlayer, boolean, Consumer)
     */
    default int damageItem(int damage, ServerLevel level, @Nullable ServerPlayer player, boolean canBreak, Consumer<Item> onBroken) {
        return self().m_41720_().damageItem(self(), damage, level, player, canBreak, onBroken);
    }

    /**
     * Called when an item entity for this stack is destroyed. Note: The {@link ItemStack} can be retrieved from the item entity.
     *
     * @param itemEntity   The item entity that was destroyed.
     * @param damageSource Damage source that caused the item entity to "die".
     */
    default void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        self().m_41720_().onDestroyed(itemEntity, damageSource);
    }

    /**
     * Whether this stack should be excluded (if possible) when selecting the target hotbar slot of a "pick" action.
     * By default, this returns true for enchanted stacks.
     *
     * @see Inventory#getSuitableHotbarSlot()
     * @param player the player performing the picking
     * @param inventorySlot the inventory slot of the item being up for replacement
     * @return true to leave this stack in the hotbar if possible
     */
    default boolean isNotReplaceableByPickAction(Player player, int inventorySlot) {
        return self().m_41720_().isNotReplaceableByPickAction(self(), player, inventorySlot);
    }

    /**
     * {@return true if the given ItemStack can be put into a grindstone to be repaired and/or stripped of its enchantments}
     */
    default boolean canGrindstoneRepair() {
        return self().m_41720_().canGrindstoneRepair(self());
    }
}
