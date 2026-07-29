package ${package}.block.display;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ${name}DisplayItem extends BlockItem implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public ${name}DisplayItem(Block block, net.minecraft.world.item.Item.Properties settings) {
		super(block, settings);
	}

	private PlayState predicate(AnimationTest<${name}DisplayItem> state) {
		<#if data.animateBlockItem>
		return state.setAndContinue(RawAnimation.begin().thenLoop("0"));
		<#else>
		return PlayState.STOP;
		</#if>
	}

	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private ${name}DisplayItemRenderer renderer;

			@Override
			public GeoItemRenderer<?> getGeoItemRenderer() {
				if (this.renderer == null)
					this.renderer = new ${name}DisplayItemRenderer();
				return this.renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>("controller", 0, this::predicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
