package net.blixza.mymod.block;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.block.custom.*;
import net.blixza.mymod.item.ModItems;
import net.blixza.mymod.sound.ModSounds;
import net.blixza.mymod.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MyMod.MOD_ID);

    public static final DeferredBlock<Block> BISMUTH_BLOCK = registerBlock("bismuth_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> BISMUTH_ORE = registerBlock("bismuth_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> BISMUTH_DEEPSLATE_ORE = registerBlock("bismuth_deepslate_ore",
            () -> new DropExperienceBlock(UniformInt.of(3, 6), BlockBehaviour.Properties.of()
                    .strength(3f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> BISMUTH_END_ORE = registerBlock("bismuth_end_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> BISMUTH_NETHER_ORE = registerBlock("bismuth_nether_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MAGIC_BLOCK = registerBlock("magic_block",
            () -> new MagicBlock(BlockBehaviour.Properties.of().strength(2f).noLootTable().sound(ModSounds.MAGIC_BLOCK_SOUNDS)));

    public static final DeferredBlock<Block> BISMUTH_LAMP = registerBlock("bismuth_lamp",
            () -> new BismuthLampBlock(BlockBehaviour.Properties.of().strength(2f)
                    .requiresCorrectToolForDrops().lightLevel(state -> state.getValue(BismuthLampBlock.CLICKED) ? 15 : 0)));

    public static final DeferredBlock<Block> RADISH_CROP = BLOCKS.register("radish_crop",
            () -> new RadishCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEETROOTS)));

    public static final DeferredBlock<Block> GOJI_BERRY_BUSH = BLOCKS.register("goji_berry_bush",
            () -> new GojiBerryBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));

    public static final DeferredBlock<StairBlock> BISMUTH_STAIRS = registerBlock("bismuth_stairs",
            () -> new StairBlock(ModBlocks.BISMUTH_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BISMUTH_SLAB = registerBlock("bismuth_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<PressurePlateBlock> BISMUTH_PRESSURE_PLATE = registerBlock("bismuth_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<ButtonBlock> BISMUTH_BUTTON = registerBlock("bismuth_button",
            () -> new ButtonBlock(BlockSetType.IRON, 20, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().noCollission()));

    public static final DeferredBlock<FenceBlock> BISMUTH_FENCE = registerBlock("bismuth_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceGateBlock> BISMUTH_FENCE_GATE = registerBlock("bismuth_fence_gate",
            () -> new FenceGateBlock(WoodType.ACACIA, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> BISMUTH_WALL = registerBlock("bismuth_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<DoorBlock> BISMUTH_DOOR = registerBlock("bismuth_door",
            () -> new DoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().noOcclusion()) {
                @Override
                protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
                    state = state.cycle(OPEN);
                    level.setBlock(pos, state, 10);
                    level.playSound(player, pos, SoundEvents.WOODEN_DOOR_OPEN, SoundSource.BLOCKS, 1f, 1f);
                    level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            });
    public static final DeferredBlock<TrapDoorBlock> BISMUTH_TRAPDOOR = registerBlock("bismuth_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().noOcclusion()) {
                @Override
                protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
                    this.toggle(state, level, pos, player);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }

                private void toggle(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
                    BlockState blockstate = state.cycle(OPEN);
                    level.setBlock(pos, blockstate, 2);
                    if (blockstate.getValue(WATERLOGGED)) {
                        level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                    }

                    this.playSound(player, level, pos, blockstate.getValue(OPEN));
                }
            });

    public static final DeferredBlock<Block> BLOODWOOD_LOG = registerBlock("bloodwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> BLOODWOOD_WOOD = registerBlock("bloodwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredBlock<Block> STRIPPED_BLOODWOOD_LOG = registerBlock("stripped_bloodwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredBlock<Block> STRIPPED_BLOODWOOD_WOOD = registerBlock("stripped_bloodwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredBlock<Block> BLOODWOOD_PLANKS = registerBlock("bloodwood_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final DeferredBlock<Block> BLOODWOOD_LEAVES = registerBlock("bloodwood_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final DeferredBlock<Block> BLOODWOOD_SAPLING = registerBlock("bloodwood_sapling",
            () -> new ModSaplingBlock(ModTreeGrowers.BLOODWOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), () -> Blocks.SCAFFOLDING));

    public static final DeferredBlock<Block> CHAIR = registerBlock("chair",
            () -> new ChairBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<Block> PEDESTAL = registerBlock("pedestal",
            () -> new PedestalBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<Block> GROWTH_CHAMBER = registerBlock("growth_chamber",
            () -> new GrowthChamberBlock(BlockBehaviour.Properties.of()));

    private static <T extends Block>DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
