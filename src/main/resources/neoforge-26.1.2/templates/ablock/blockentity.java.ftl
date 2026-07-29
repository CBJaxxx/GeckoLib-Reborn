<#-- @formatter:off -->
package ${package}.block.entity;

<#assign regname = data.getModElement().getRegistryName()>

import javax.annotation.Nullable;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;

public class ${name}TileEntity extends RandomizableContainerBlockEntity implements GeoBlockEntity, WorldlyContainer {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(${data.inventorySize}, ItemStack.EMPTY);
	<#if data.hasBlockstates()>
	public int blockstateNew = this.getBlockState().getValue(${name}Block.BLOCKSTATE);
	private int blockstateOld = this.getBlockState().getValue(${name}Block.BLOCKSTATE);
	</#if>

	public ${name}TileEntity(BlockPos pos, BlockState state) {
		super(${JavaModName}BlockEntities.${(regname)?upper_case}.get(), pos, state);
	}

	private PlayState predicate(AnimationTest<${name}TileEntity> state) {
		<#if data.hasBlockstates()>
		blockstateNew = this.getBlockState().getValue(${name}Block.BLOCKSTATE);
		if (blockstateOld != blockstateNew) {
			state.controller().reset();
			blockstateOld = blockstateNew;
			return PlayState.STOP;
		}
		</#if>
		String animationprocedure = ("" + this.getBlockState().getValue(${name}Block.ANIMATION));
		if (animationprocedure.equals("0")) {
			return state.setAndContinue(RawAnimation.begin().thenLoop(animationprocedure));
		}
		return PlayState.STOP;
	}

	String prevAnim = "0";
	private PlayState procedurePredicate(AnimationTest<${name}TileEntity> state) {
		String animationprocedure = ("" + this.getBlockState().getValue(${name}Block.ANIMATION));
		if ((!animationprocedure.equals("0") && state.controller().hasAnimationFinished())
				|| (!animationprocedure.equals(prevAnim) && !animationprocedure.equals("0"))) {
			if (!animationprocedure.equals(prevAnim))
				state.controller().reset();
			state.controller().setAnimation(RawAnimation.begin().thenPlay(animationprocedure));
			if (state.controller().hasAnimationFinished()) {
				if (this.getBlockState().getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp)
					level.setBlock(this.getBlockPos(), this.getBlockState().setValue(_integerProp, 0), 3);
				state.controller().reset();
			}
		} else if (animationprocedure.equals("0")) {
			prevAnim = "0";
			return PlayState.STOP;
		}
		prevAnim = animationprocedure;
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>("controller", 0, this::predicate));
		data.add(new AnimationController<>("procedurecontroller", 0, this::procedurePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override public void loadAdditional(ValueInput valueInput) {
		super.loadAdditional(valueInput);

		if (!this.tryLoadLootTable(valueInput))
			this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

		ContainerHelper.loadAllItems(valueInput, this.stacks);

		<#if data.hasEnergyStorage>
		valueInput.child("energyStorage").ifPresent(input -> energyStorage.deserialize(input));
		</#if>

		<#if data.isFluidTank>
		valueInput.child("fluidTank").ifPresent(input -> fluidTank.deserialize(input));
		</#if>
	}

	@Override public void saveAdditional(ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);

		if (!this.trySaveLootTable(valueOutput))
			ContainerHelper.saveAllItems(valueOutput, this.stacks);

		<#if data.hasEnergyStorage>
		energyStorage.serialize(valueOutput.child("energyStorage"));
		</#if>

		<#if data.isFluidTank>
		fluidTank.serialize(valueOutput.child("fluidTank"));
		</#if>
	}

	@Override public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return this.saveWithFullMetadata(lookupProvider);
	}

	@Override public int getContainerSize() {
		return stacks.size();
	}

	@Override public boolean isEmpty() {
		for (ItemStack itemstack : this.stacks)
			if (!itemstack.isEmpty())
				return false;
		return true;
	}

	@Override public Component getDefaultName() {
		return Component.literal("${registryname}");
	}

	@Override public int getMaxStackSize() {
		return ${data.inventoryStackSize};
	}

	@Override public AbstractContainerMenu createMenu(int id, Inventory inventory) {
		<#if !data.guiBoundTo?has_content || data.guiBoundTo == "<NONE>" || !(data.guiBoundTo)?has_content>
		return ChestMenu.threeRows(id, inventory);
		<#else>
		return new ${data.guiBoundTo}Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
		</#if>
	}

	@Override public Component getDisplayName() {
		return Component.literal("${data.name}");
	}

	@Override protected NonNullList<ItemStack> getItems() {
		return this.stacks;
	}

	@Override protected void setItems(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	@Override public boolean canPlaceItem(int index, ItemStack stack) {
		<#list data.inventoryOutSlotIDs as id>
		if (index == ${id})
			return false;
		</#list>
		return true;
	}

	<#-- START: WorldlyContainer -->
	@Override public int[] getSlotsForFace(Direction side) {
		return IntStream.range(0, this.getContainerSize()).toArray();
	}

	@Override public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	@Override public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		<#list data.inventoryInSlotIDs as id>
		if (index == ${id})
			return false;
		</#list>
		return true;
	}
	<#-- END: WorldlyContainer -->

	<#if data.hasEnergyStorage>
	private final EnergyStorage energyStorage = new EnergyStorage(${data.energyCapacity}, ${data.energyMaxReceive}, ${data.energyMaxExtract}, ${data.energyInitial}) {
		@Override public int receiveEnergy(int maxReceive, boolean simulate) {
			int retval = super.receiveEnergy(maxReceive, simulate);
			if(!simulate) {
				setChanged();
				level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
			}
			return retval;
		}

		@Override public int extractEnergy(int maxExtract, boolean simulate) {
			int retval = super.extractEnergy(maxExtract, simulate);
			if(!simulate) {
				setChanged();
				level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
			}
			return retval;
		}
	};

	public EnergyStorage getEnergyStorage() {
		return energyStorage;
	}
	</#if>

	<#if data.isFluidTank>
	private final FluidTank fluidTank = new FluidTank(${data.fluidCapacity}
		<#if data.fluidRestrictions?has_content>, fs -> {
			<#list data.fluidRestrictions as fluidRestriction>
				<#if fluidRestriction.getUnmappedValue().startsWith("CUSTOM:")>
				if(fs.getFluid() == ${JavaModName}Fluids.<#if fluidRestriction.getUnmappedValue().endsWith(":Flowing")>FLOWING_</#if>${generator.getRegistryNameForModElement(fluidRestriction.getUnmappedValue()?remove_beginning("CUSTOM:")?remove_ending(":Flowing"))?upper_case}.get()) return true;
				<#else>
				if(fs.getFluid() == Fluids.${fluidRestriction}) return true;
				</#if>
			</#list>
			return false;
		}
		</#if>
	) {
		@Override protected void onContentsChanged() {
			super.onContentsChanged();
			setChanged();
			level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
		}
	};

	public FluidTank getFluidTank() {
		return fluidTank;
	}
	</#if>
}
<#-- @formatter:on -->
