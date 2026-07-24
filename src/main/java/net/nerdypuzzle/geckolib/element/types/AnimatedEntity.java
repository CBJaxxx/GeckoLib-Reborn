package net.nerdypuzzle.geckolib.element.types;

import net.mcreator.blockly.data.BlocklyLoader;
import net.mcreator.blockly.data.BlocklyXML;
import net.mcreator.blockly.java.BlocklyToJava;
import net.mcreator.element.BaseType;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.*;
import net.mcreator.element.parts.procedure.NumberProcedure;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.types.interfaces.ICommonType;
import net.mcreator.element.types.interfaces.IEntityWithModel;
import net.mcreator.element.types.interfaces.IMCItemProvider;
import net.mcreator.element.types.interfaces.ITabContainedElement;
import net.mcreator.generator.GeneratorFlavor;
import net.mcreator.generator.blockly.BlocklyBlockCodeGenerator;
import net.mcreator.generator.blockly.ProceduralBlockCodeGenerator;
import net.mcreator.generator.template.IAdditionalTemplateDataProvider;
import net.mcreator.io.FileIO;
import net.mcreator.io.ResourcePointer;
import net.mcreator.minecraft.MCItem;
import net.mcreator.minecraft.MinecraftImageGenerator;
import net.mcreator.ui.blockly.BlocklyEditorType;
import net.mcreator.ui.init.ImageMakerTexturesCache;
import net.mcreator.ui.minecraft.states.PropertyDataWithValue;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.FilenameUtilsPatched;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.resources.Model;
import net.mcreator.workspace.resources.Texture;
import net.nerdypuzzle.geckolib.registry.PluginElementTypes;

import javax.annotation.Nullable;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.module.ModuleDescriptor;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class AnimatedEntity extends GeneratableElement
        implements IEntityWithModel, ITabContainedElement, ICommonType, IMCItemProvider {

	/**
	 * Default AI Blockly workspace with the non-deletable starter (aitasks_container)
	 * plus a basic goal set — matches MCreator LivingEntity.XML_BASE.
	 */
	public static final String XML_BASE = """
			<xml xmlns="https://developers.google.com/blockly/xml">
			<block type="aitasks_container" deletable="false" x="40" y="40"><next>
			<block type="attack_on_collide"><field name="speed">1.2</field><field name="longmemory">FALSE</field><field name="condition">null,null</field><next>
			<block type="wander"><field name="speed">1</field><field name="condition">null,null</field><next>
			<block type="attack_action"><field name="callhelp">FALSE</field><field name="condition">null,null</field><next>
			<block type="look_around"><field name="condition">null,null</field><next>
			<block type="swim_in_water"><field name="condition">null,null</field></block></next>
			</block></next></block></next></block></next></block></next></block></xml>""";

    public String mobName;
    public String mobLabel;

    public String mobModelName;
    public String mobModelTexture;
    public String mobModelGlowTexture;
    public NumberProcedure visualScale;
    public NumberProcedure boundingBoxScale;
    public Procedure solidBoundingBox;
    public List<PropertyDataWithValue<?>> entityDataEntries;

    public double modelWidth, modelHeight, modelShadowSize;
    public double mountedYOffset;
    public double stepHeight;

    public boolean hasSpawnEgg;
    public Color spawnEggBaseColor;
    public Color spawnEggDotColor;
    public TabEntry creativeTab;
    public List<TabEntry> creativeTabs;

    public boolean isBoss;
    public String bossBarColor;
    public String bossBarType;

    public MItemBlock equipmentMainHand;
    public MItemBlock equipmentOffHand;
    public MItemBlock equipmentHelmet;
    public MItemBlock equipmentBody;
    public MItemBlock equipmentLeggings;
    public MItemBlock equipmentBoots;

    public String mobBehaviourType;
    public String mobCreatureType;
    public int attackStrength;
    public double attackKnockback;
    public double knockbackResistance;
    public double movementSpeed;
    public double armorBaseValue;
    public int trackingRange;
    public int followRange;
    public int health;
    public int xpAmount;
    public boolean waterMob;
    public boolean flyingMob;

    public String guiBoundTo;
    public int inventorySize;
    public int inventoryStackSize;
    public int deathTime;
    public int lerp;

    public boolean disableCollisions;

    public boolean ridable;
    public boolean canControlForward;
    public boolean canControlStrafe;

    public boolean immuneToFire;
    public boolean immuneToArrows;
    public boolean immuneToFallDamage;
    public boolean immuneToCactus;
    public boolean immuneToDrowning;
    public boolean immuneToLightning;
    public boolean immuneToPotions;
    public boolean immuneToPlayer;
    public boolean immuneToExplosion;
    public boolean immuneToTrident;
    public boolean immuneToAnvil;
    public boolean immuneToWither;
    public boolean immuneToDragonBreath;

    public MItemBlock mobDrop;

    public Sound livingSound;
    public Sound hurtSound;
    public Sound deathSound;
    public Sound stepSound;
    public Sound raidCelebrationSound;

    public Procedure onStruckByLightning;
    public Procedure whenMobFalls;
    public Procedure whenMobDies;
    public Procedure whenMobIsHurt;
    public Procedure onRightClickedOn;
    public Procedure whenThisMobKillsAnother;
    public Procedure onMobTickUpdate;
    public Procedure onPlayerCollidesWith;
    public Procedure onInitialSpawn;
    public Procedure finishedDying;

    public boolean hasAI;
    public String aiBase;
	@BlocklyXML(name = "aitasks", defaultXML = AnimatedEntity.XML_BASE)
	public String aixml;

    public String model;
    public String groupName;


    //animation fields
    public String animation1;
    public String animation2;
    public String animation3;
    public String animation4;
    public String animation5;
    public String animation6;
    public String animation7;
    public String animation8;
    public String animation9;
    public String animation10;

    //animation checkboxes
    public boolean enable2;
    public boolean enable3;
    public boolean enable4;
    public boolean enable5;
    public boolean enable6;
    public boolean enable7;
    public boolean enable8;
    public boolean enable9;
    public boolean enable10;
    //


    public boolean breedable;
    public boolean tameable;
    public boolean disableDeathRotation;
    public boolean headMovement;
    public boolean eyeHeight;
    public List<MItemBlock> breedTriggerItems;

    public boolean ranged;
    public MItemBlock rangedAttackItem;
    public String rangedItemType;
    public int rangedAttackInterval;
    public double rangedAttackRadius;
    public double height;
    public Number attackRate;
    public int[] raidSpawnsCount;

    public boolean spawnThisMob;
    public boolean doesDespawnWhenIdle;
    public Procedure spawningCondition;
    public int spawningProbability;
    public MobSpawnType mobSpawningType;
    public int minNumberOfMobsPerGroup;
    public int maxNumberOfMobsPerGroup;
    public List<BiomeEntry> restrictionBiomes;
    public boolean spawnInDungeons;

    private AnimatedEntity() {
        this(null);
    }

    public AnimatedEntity(ModElement element) {
        super(element);

        this.modelShadowSize = 0.5;
        this.mobCreatureType = "UNDEFINED";
        this.mobModelTexture = new String("");
        this.trackingRange = 64;
        this.rangedItemType = "Default item";
        this.rangedAttackInterval = 20;
        this.rangedAttackRadius = 10;

        this.followRange = 16;

        this.inventorySize = 9;
        this.inventoryStackSize = 64;

        this.stepHeight = 0.6;

        this.entityDataEntries = new ArrayList<>();

        this.raidSpawnsCount = new int[] {4, 3, 3, 4, 4, 4, 2};

        this.creativeTabs = new ArrayList<>();
        this.attackRate = 7;
    }

    @Override
    public Model getEntityModel() {
        return null;
    }

    public Collection<BaseType> getBaseTypesProvided() {
        return this.hasSpawnEgg ? List.of(BaseType.ITEM, BaseType.ENTITY) : List.of(BaseType.ENTITY);
    }

    public boolean hasGlowTexture() {
        // Only treat as glow when a dedicated glow map is set (not the same as the body texture).
        // Using the body texture as a glow layer re-renders it with eyes/emissive and looks broken.
        return mobModelGlowTexture != null && !mobModelGlowTexture.isEmpty()
                && (mobModelTexture == null || !mobModelGlowTexture.equals(mobModelTexture));
    }

    @Override public List<TabEntry> getCreativeTabs() {
        if (creativeTab != null)
            if (!creativeTab.isEmpty())
                return List.of(creativeTab);
        return creativeTabs;
    }

    @Override public BufferedImage generateModElementPicture() {
        return MinecraftImageGenerator.Preview.generateMobPreviewPicture(
                Texture.fromName(getModElement().getWorkspace(), TextureType.ENTITY,
                        FilenameUtilsPatched.removeExtension(mobModelTexture)).getTextureIcon(getModElement().getWorkspace()).getImage(), spawnEggBaseColor, spawnEggDotColor,
                hasSpawnEgg);
    }

    public boolean hasDrop() {
        return !mobDrop.isEmpty();
    }

    public boolean hasCustomProjectile() {
        return ranged && "Default item".equals(rangedItemType) && !rangedAttackItem.isEmpty();
    }

    public List<MCItem> providedMCItems() {
        return this.hasSpawnEgg ? List.of(new MCItem.Custom(this.getModElement(), "spawn_egg", "item", "Spawn egg")) : Collections.emptyList();
    }

    @Override public List<MCItem> getCreativeTabItems() {
        return providedMCItems();
    }

    @Override public ImageIcon getIconForMCItem(Workspace workspace, String suffix) {
        if ("spawn_egg".equals(suffix)) {
            return MinecraftImageGenerator.generateSpawnEggIcon(spawnEggBaseColor, spawnEggDotColor);
        }
        return null;
    }

    /**
     * MC 1.21.5+ / 26.1 no longer tints spawn eggs via template_spawn_egg.
     * Match stock LivingEntity: write a generated item texture used by models/item + items client model JSON.
     */
    @Override public void finalizeModElementGeneration() {
        if (!hasSpawnEgg)
            return;
        try {
            if (ModuleDescriptor.Version.parse(getModElement().getGeneratorConfiguration().getGeneratorMinecraftVersion())
                    .compareTo(ModuleDescriptor.Version.parse("1.21.5")) < 0)
                return;
            if (getModElement().getGeneratorConfiguration().getGeneratorFlavor().getGamePlatform()
                    != GeneratorFlavor.GamePlatform.JAVAEDITION)
                return;
        } catch (Exception ignored) {
            // If version parse fails, still generate for safety on modern NeoForge generators.
        }

        File spawnEggTextureFile = getModElement().getFolderManager()
                .getTextureFile(getModElement().getRegistryName() + "_spawn_egg_generated", TextureType.ITEM);
        ImageIcon spawnEgg = ImageUtils.drawOver(
                ImageUtils.colorize(ImageMakerTexturesCache.CACHE.get(
                        new ResourcePointer("templates/textures/texturemaker/egg_base.png")), spawnEggBaseColor, true),
                ImageUtils.colorize(ImageMakerTexturesCache.CACHE.get(
                        new ResourcePointer("templates/textures/texturemaker/egg_accent.png")), spawnEggDotColor, true));
        FileIO.writeImageToPNGFile(ImageUtils.toBufferedImage(spawnEgg.getImage()), spawnEggTextureFile);
    }

    @Override public @Nullable IAdditionalTemplateDataProvider getAdditionalTemplateData() {
        return additionalData -> {
            BlocklyBlockCodeGenerator blocklyBlockCodeGenerator = new BlocklyBlockCodeGenerator(
                    BlocklyLoader.INSTANCE.getBlockLoader(BlocklyEditorType.AI_TASK).getDefinedBlocks(),
                    getModElement().getGenerator().getGeneratorStats().getBlocklyBlocks(BlocklyEditorType.AI_TASK),
                    this.getModElement().getGenerator()
                            .getTemplateGeneratorFromName(BlocklyEditorType.AI_TASK.registryName()),
                    additionalData).setTemplateExtension(
                    this.getModElement().getGeneratorConfiguration().getGeneratorFlavor().getBaseLanguage().name()
                            .toLowerCase(Locale.ENGLISH));
			String aiWorkspaceXml = (this.aixml != null && !this.aixml.isBlank()) ? this.aixml : XML_BASE;
            BlocklyToJava blocklyToJava = new BlocklyToJava(this.getModElement().getWorkspace(), this.getModElement(),
                    BlocklyEditorType.AI_TASK, aiWorkspaceXml, this.getModElement().getGenerator()
                    .getTemplateGeneratorFromName(BlocklyEditorType.AI_TASK.registryName()),
                    new ProceduralBlockCodeGenerator(blocklyBlockCodeGenerator));

            List<?> unmodifiableAIBases = (List<?>) getModElement().getWorkspace().getGenerator()
                    .getGeneratorConfiguration().getDefinitionsProvider()
                    .getModElementDefinition(PluginElementTypes.ANIMATEDENTITY).get("unmodifiable_ai_bases");
            additionalData.put("aicode", unmodifiableAIBases != null && !unmodifiableAIBases.contains(aiBase) ?
                    blocklyToJava.getGeneratedCode() :
                    "");
            additionalData.put("aiblocks", blocklyToJava.getUsedBlocks());
        };
    }

}
