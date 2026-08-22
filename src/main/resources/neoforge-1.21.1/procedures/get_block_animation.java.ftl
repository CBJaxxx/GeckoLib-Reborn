<#include "mcelements.ftl">
(new Object() {
	public int getValue(LevelAccessor world, BlockPos pos) {
		BlockState _bs = world.getBlockState(pos);
		if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp)
			return _bs.getValue(_integerProp);
		return 0;
	}
}.getValue(world, ${toBlockPos(input$x,input$y,input$z)}))
