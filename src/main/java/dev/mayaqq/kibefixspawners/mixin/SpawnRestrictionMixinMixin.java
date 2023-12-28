package dev.mayaqq.kibefixspawners.mixin;


import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SpawnRestriction.class, priority = 1500)
public class SpawnRestrictionMixinMixin {

    @Unique
    private static SpawnReason spawnReasonTemp;

    @Inject(method = "canSpawn", at = @At("HEAD"))
    private static <T extends Entity> void getInfo(EntityType<T> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        spawnReasonTemp = spawnReason;
    }

    @TargetHandler(
            mixin = "io.github.lucaargolo.kibe.mixin.SpawnRestrictionMixin",
            name = "headCanSpawn"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/github/lucaargolo/kibe/blocks/bigtorch/BigTorchBlockEntity$Companion;isChunkSuppressed(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/math/ChunkPos;)Z"
            )
    )
    private static boolean allowWhenSpawner(boolean old) {
        return old && spawnReasonTemp != SpawnReason.SPAWNER;
    }
}
