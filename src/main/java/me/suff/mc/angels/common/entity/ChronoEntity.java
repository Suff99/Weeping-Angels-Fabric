package me.suff.mc.angels.common.entity;

import me.suff.mc.angels.WeepingAngels;
import me.suff.mc.angels.common.objects.WAItems;
import me.suff.mc.angels.util.EntitySpawnPacket;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrowableProjectile;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.EntityHitResult;

/* Created by Craig on 21/02/2021 */
public class ChronoEntity extends ThrowableProjectile {
    public ChronoEntity(EntityType< ? extends ThrowableProjectile > entityType, World world) {
        super(entityType, world);
    }

    public ChronoEntity(EntityType< ? extends ThrowableProjectile > entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public ChronoEntity(EntityType< ? extends ThrowableProjectile > entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    public static void spawnPortal(World world, BlockPos blockPos) {
        if (!world.isClient()) {
            PortalEntity portalEntity = new PortalEntity(WeepingAngels.PORTAL, world);
            portalEntity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            world.spawnEntity(portalEntity);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return WAItems.CHRONODYNE_GENERATOR;
    }



    /*@Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        BlockState state = world.getBlockState(blockHitResult.getBlockPos());
        if (!this.world.isClient) {
            if(!state.isAir()) {
                world.setBlockState(getBlockPos(), WABlocks.CHRONODYNE_GENERATOR.getDefaultState());
            }
          //  this.remove();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            if(hitResult.getType() == HitResult.Type.BLOCK){
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockState hitState = world.getBlockState(blockHitResult.getBlockPos());
                if(hitState.isAir() || hitState.getMaterial().isReplaceable()) {
                    world.setBlockState(blockHitResult.getBlockPos(), WABlocks.CHRONODYNE_GENERATOR.getDefaultState());
                }
            }
            this.remove();
        }
    }*/

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (entityHitResult.getEntity() instanceof WeepingAngelEntity) {
            ((WeepingAngelEntity) entityHitResult.getEntity()).damage(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        // spawnPortal(world, entityHitResult.getEntity().getBlockPos());
        if (entityHitResult.getEntity() instanceof WeepingAngelEntity) {
            entityHitResult.getEntity().damage(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
        }
    }

    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, WeepingAngels.spawnPacket);
    }
}
