<#-- @formatter:off -->
<#include "../mcitems.ftl">

package ${package}.entity;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class) public class ${name}EntityProjectile extends AbstractArrow implements ItemSupplier {

	public static final ItemStack PROJECTILE_ITEM = ${mappedMCItemToItemStackCode(data.rangedAttackItem)};

	public ${name}EntityProjectile(EntityType<? extends ${name}EntityProjectile> type, Level world) {
		super(type, world);
	}

	public ${name}EntityProjectile(EntityType<? extends ${name}EntityProjectile> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world, PROJECTILE_ITEM, null);
	}

	public ${name}EntityProjectile(EntityType<? extends ${name}EntityProjectile> type, LivingEntity entity, Level world) {
		super(type, entity, world, PROJECTILE_ITEM, null);
	}

	@Override protected void doPostHurtEffects(LivingEntity livingEntity) {
		super.doPostHurtEffects(livingEntity);
		livingEntity.setArrowCount(livingEntity.getArrowCount() - 1);
	}

	@Override @OnlyIn(Dist.CLIENT) public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override protected ItemStack getDefaultPickupItem() {
		return ${mappedMCItemToItemStackCode(data.rangedAttackItem)};
	}
}
<#-- @formatter:on -->