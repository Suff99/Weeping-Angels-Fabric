package me.suff.mc.angels.common.block;

import net.minecraft.block.OreBlock;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneOreBlock;

import java.util.Random;

/* Created by Craig on 19/02/2021 */
public class WAOreBlock extends Block {
    public WAOreBlock(ItemProperties settings) {
        super(settings);
    }

    @Override
    protected int getExperienceWhenMined(Random random) {
        return random.nextInt(4);
    }


}
