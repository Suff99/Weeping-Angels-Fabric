package me.suff.mc.angels.common.entity;

import me.suff.mc.angels.WeepingAngels;
import me.suff.mc.angels.common.objects.WASounds;
import me.suff.mc.angels.common.objects.WASources;
import me.suff.mc.angels.enums.WeepingAngelPose;
import me.suff.mc.angels.enums.WeepingAngelVariants;
import me.suff.mc.angels.util.AngelUtils;
import me.suff.mc.angels.util.Constants;
import me.suff.mc.angels.util.PlayerUtils;
import me.suff.mc.angels.util.WAConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;

import javax.annotation.Nullable;
import java.util.Objects;

/* Created by Craig on 18/02/2021 */
public class WeepingAngelEntity extends QuantumLockBaseEntity {

    private static final TrackedData<String> CURRENT_POSE = DataTracker.registerData(WeepingAngelEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> VARIENT = DataTracker.registerData(WeepingAngelEntity.class, TrackedDataHandlerRegistry.STRING);
    private long timeSincePlayedSound = 0;

    public WeepingAngelEntity(World worldIn) {
        super(worldIn, WeepingAngels.WEEPING_ANGEL);
        goalSelector.add(5, new WanderAroundGoal(this, 1.0D));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 50.0F));
    }


    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().
                add(EntityAttributes.GENERIC_ATTACK_DAMAGE, WAConfig.AngelBehaviour.attackDamage.getValue()).
                add(EntityAttributes.GENERIC_MAX_HEALTH, 50D).
                add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 9999999.0D).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F).
                add(EntityAttributes.GENERIC_ARMOR, 2.0D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getEntityData().define(CURRENT_POSE, WeepingAngelPose.getRandomPose(AngelUtils.RAND).name());
        getEntityData().define(VARIENT, AngelUtils.randomVarient().name());
    }

    public String getVarient() {
        return getEntityData().get(VARIENT);
    }

    public void setVarient(WeepingAngelVariants varient) {
        getEntityData().set(VARIENT, varient.name());
    }

    public String getAngelPose() {
        return getEntityData().get(CURRENT_POSE);
    }

    public void setPose(WeepingAngelPose weepingAngelPose) {
        getEntityData().set(CURRENT_POSE, weepingAngelPose.name());
    }

    @Override
    public void tick() {
        super.tick();
        if (getSeenTime() == 0 || world.isAir(blockPosition().below())) {
            setAiDisabled(false);
        }
        if (age % 500 == 0 && getTarget() == null && getSeenTime() == 0) {
            setPose(Objects.requireNonNull(WeepingAngelPose.HIDING));
        }
        if (WAConfig.WorldConfig.breakBlocks.getValue() && isSeen()) {
            replaceBlocks(getBoundingBox().expand(WAConfig.WorldConfig.breakRange.getValue()));
        }
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return WASounds.ANGEL_DEATH;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // We need to be compatible with the kill command
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.damage(source, amount);
        }

        //Pickaxe only!
        if (source.getEntity() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) source.getAttacker();
            ItemStack stack = PlayerUtils.getItemFromActive(living);
            boolean isPickaxe = stack.getItem() instanceof PickaxeItem;
            boolean handEmpty = stack.getItem() == Items.AIR;
            if (isPickaxe) {
                super.damage(source, amount);

                stack.damage(random.nextInt(25), living, livingEntity -> {
                    playSound(WASounds.ANGEL_MOCKING, 1, random.nextFloat());
                    livingEntity.sendToolBreakStatus(Hand.MAIN_HAND);
                });

            } else {
                if (handEmpty) {
                    living.damage(WASources.PUNCH_STONE, 3);
                    if (random.nextInt(100) < 20) {
                        playSound(WASounds.ANGEL_MOCKING, 1, random.nextFloat());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void takeKnockback(float f, double d, double e) {
        //No
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.getMaterial().isLiquid() && world.random.nextInt(5) == 4) {
            BlockState blockState = this.world.getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = blockState.isOf(Blocks.SNOW) ? blockState.getSoundGroup() : state.getSoundGroup();
            this.playSound(WASounds.STONE_SCRAP, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
        }
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putString(Constants.CURRENT_POSE, getAngelPose());
        tag.putString(Constants.VARIANT, getVarient());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        setVarient(WeepingAngelVariants.getVariant(tag.getString(Constants.VARIANT)));
        setPose(WeepingAngelPose.getPose(tag.getString(Constants.CURRENT_POSE)));
    }

    @Override
    public void invokeSeen(PlayerEntity player) {
        super.invokeSeen(player);
        if (age > 30 && player instanceof ServerPlayerEntity && getSeenTime() == 1 && getPrevPos().asLong() != getBlockPos().asLong()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            setPrevPos(getBlockPos());
            boolean canPlaySound = !player.isCreative() && getTimeSincePlayedSound() == 0 || System.currentTimeMillis() - getTimeSincePlayedSound() >= 20000;
            // Play Sound
            if (canPlaySound) {
                if (WAConfig.AngelBehaviour.playSeenSounds.getValue() && player.distanceTo(this) < 15) {
                    setTimeSincePlayedSound(System.currentTimeMillis());
                    serverPlayerEntity.networkHandler.sendPacket(new PlaySoundFromEntityS2CPacket(WASounds.ANGEL_SEEN, SoundCategory.HOSTILE, this, 0.1F, 0.3F));
                }
            }
            setPose(WeepingAngelPose.getRandomPose(AngelUtils.RAND));
        }
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        playSound(WASounds.ANGEL_AMBIENT, 0.5F, 1.0F);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_STONE_HIT;
    }

    public long getTimeSincePlayedSound() {
        return timeSincePlayedSound;
    }

    public void setTimeSincePlayedSound(long timeSincePlayedSound) {
        this.timeSincePlayedSound = timeSincePlayedSound;
    }

    @Override
    public void onKilledOther(ServerWorld serverWorld, LivingEntity livingEntity) {
        super.onKilledOther(serverWorld, livingEntity);
        playSound(WASounds.ANGEL_NECK_SNAP, 1, 1);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
        if (!isSeen() && age > 100) {
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

                //Hurt
                if (random.nextBoolean()) {
                    serverPlayerEntity.damage(WASources.BREAK_NECK, WAConfig.AngelBehaviour.attackDamage.getValue());
                    return;
                }

                //Teleport
                @Nullable MinecraftServer server = getEntityWorld().getServer();
                if (server != null) {
                    ServerWorld teleportWorld = AngelUtils.getRandomDimension(server);
                    BlockPos pos = AngelUtils.getGoodY(teleportWorld, getBlockPos().add(random.nextInt(WAConfig.AngelBehaviour.teleportRange.getValue()), 0, random.nextInt(WAConfig.AngelBehaviour.teleportRange.getValue())));
                    serverPlayerEntity.networkHandler.sendPacket(new PlaySoundFromEntityS2CPacket(WASounds.TELEPORT, SoundCategory.HOSTILE, this, 0.1F, 1.0F));
                    serverPlayerEntity.teleport(teleportWorld, pos.getX(), pos.getY(), pos.getZ(), player.yaw, player.pitch);
                }
            }
        }
    }


    @Override
    protected Identifier getLootTableId() {
        return super.getLootTableId();
    }

    @Override
    protected void updatePostDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            hurtTime = 0;
            this.remove();
        }
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()), this.getParticleX(1.0D), this.getBodyY(1.0D), this.getParticleZ(1.0D), d0, d1, d2);
        }
    }

    //DESTROY LIGHT BLOCKS
    private void replaceBlocks(Box box) {
        if (world.isClient() || age % 100 != 0) return;
        ServerWorld serverWorld = (ServerWorld) world;

        if (world.getLightLevel(getBlockPos()) == 0) {
            return;
        }

        for (BlockPos pos : BlockPos.iterate(new BlockPos(box.maxX, box.maxY, box.maxZ), new BlockPos(box.minX, box.minY, box.minZ))) {
            BlockState blockState = serverWorld.getBlockState(pos);

            if (blockState.getBlock() == Blocks.LAVA) {
                continue;
            }

            if (blockState.getBlock() == Blocks.TORCH || blockState.getBlock() == Blocks.REDSTONE_TORCH || blockState.getBlock() == Blocks.GLOWSTONE) {
                AngelUtils.playBreakEvent(this, pos, Blocks.AIR.getDefaultState());
                return;
            }

            if (blockState.getBlock() == Blocks.REDSTONE_LAMP) {
                if (blockState.get(RedstoneLampBlock.LIT)) {
                    AngelUtils.playBreakEvent(this, pos, blockState.with(RedstoneLampBlock.LIT, false));
                    return;
                }
            }


            if (blockState.getBlock() instanceof NetherPortalBlock || blockState.getBlock() instanceof EndPortalBlock) {
                if (getHealth() < getMaxHealth()) {
                    heal(0.5F);
                    Vec3d start = getPos();
                    Vec3d end = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                    Vec3d path = start.subtract(end);
                    for (int i = 0; i < 10; ++i) {
                        double percent = i / 10.0;
                        serverWorld.spawnParticles(ParticleTypes.PORTAL, pos.getX() + 0.5 + path.getX() * percent, pos.getY() + 1.3 + path.getY() * percent, pos.getZ() + 0.5 + path.z * percent, 20, 0, 0, 0, 0);
                    }
                    return;
                }
            }

            if (blockState.getLuminance() > 0 && !(blockState.getBlock() instanceof NetherPortalBlock) && !(blockState.getBlock() instanceof EndPortalBlock)) {
                AngelUtils.playBreakEvent(this, pos, Blocks.AIR.getDefaultState());
                return;
            }
        }
    }


}
