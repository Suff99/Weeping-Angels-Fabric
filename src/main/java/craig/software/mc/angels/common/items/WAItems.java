package craig.software.mc.angels.common.items;

import craig.software.mc.angels.WeepingAngels;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WAItems {
    public static final Item TIMEY_WIMEY_DETECTOR = new DetectorItem();
    public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.build(new ResourceLocation(WeepingAngels.MODID, "general"), () -> new ItemStack(WAItems.TIMEY_WIMEY_DETECTOR));
    public static final Item CHRONODYNE_GENERATOR = new Item(new Item.Properties().tab(WAItems.ITEM_GROUP));
    public static final Item ANGEL_SPAWNER = new Item(new Item.Properties().tab(WAItems.ITEM_GROUP));
    public static final Item KONTRON_INGOT = new Item(new Item.Properties().tab(WAItems.ITEM_GROUP));
    public static final Item CHISEL = new Item(new Item.Properties().tab(WAItems.ITEM_GROUP));
    public static final Item SALLY = new Item(new Item.Properties().tab(WAItems.ITEM_GROUP));
    public static final Item TIME_PREVAILS = new Item(new Item.Properties().tab(WAItems.ITEM_GROUP));

    public static void onInitialize() {
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "timey_wimey_detector"), TIMEY_WIMEY_DETECTOR);
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "chronodyne_generator"), CHRONODYNE_GENERATOR);
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "weeping_angel"), ANGEL_SPAWNER);
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "kontron_ingot"), KONTRON_INGOT);
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "chisel"), CHISEL);
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "music_disc_sally"), SALLY);
        Registry.register(Registry.ITEM, new ResourceLocation(WeepingAngels.MODID, "music_disc_time_prevails"), TIME_PREVAILS);
    }
}
