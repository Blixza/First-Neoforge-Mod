package net.blixza.mymod.enchantment;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

public class ModWeightedStateProvider extends WeightedStateProvider {
    public static final MapCodec<ModWeightedStateProvider> CODEC = SimpleWeightedRandomList.wrappedCodec(BlockState.CODEC)
            .comapFlatMap(ModWeightedStateProvider::create, provider -> provider.weightedList)
            .fieldOf("entries");
    private final SimpleWeightedRandomList<BlockState> weightedList;

    private static DataResult<ModWeightedStateProvider> create(SimpleWeightedRandomList<BlockState> weightedList) {
        return weightedList.isEmpty() ? DataResult.error(() -> "WeightedStateProvider with no states") : DataResult.success(new ModWeightedStateProvider(weightedList));
    }

    public ModWeightedStateProvider(SimpleWeightedRandomList<BlockState> weightedList) {
        super(weightedList);
        this.weightedList = weightedList;
    }

    public ModWeightedStateProvider(SimpleWeightedRandomList.Builder<BlockState> builder) {
        this(builder.build());
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
    }

    @Override
    public BlockState getState(RandomSource random, BlockPos pos) {
        return this.weightedList.getRandomValue(random).orElseThrow(IllegalStateException::new);
    }
}
