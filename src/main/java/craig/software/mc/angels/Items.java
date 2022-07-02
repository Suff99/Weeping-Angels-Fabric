package craig.software.mc.angels;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(WeepingAngels.MODID, "general"), () -> new ItemStack(Blocks.COBBLESTONE));
    public static final Item TIMEY_WIMEY_DETECTOR = new Item(new Item.Settings().group(Items.ITEM_GROUP));
    public static final Item CHRONODYNE_GENERATOR = new Item(new Item.Settings().group(Items.ITEM_GROUP));
    public static final Item ANGEL_SPAWNER = new Item(new Item.Settings().group(Items.ITEM_GROUP));
    public static final Item KONTRON_INGOT = new Item(new Item.Settings().group(Items.ITEM_GROUP));
    public static final Item CHISEL = new Item(new Item.Settings().group(Items.ITEM_GROUP));
    public static final Item SALLY = new Item(new Item.Settings().group(Items.ITEM_GROUP));
    public static final Item TIME_PREVAILS = new Item(new Item.Settings().group(Items.ITEM_GROUP));

    public static void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "timey_wimey_detector"), TIMEY_WIMEY_DETECTOR);
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "chronodyne_generator"), CHRONODYNE_GENERATOR);
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "weeping_angel"), ANGEL_SPAWNER);
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "kontron_ingot"), KONTRON_INGOT);
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "chisel"), CHISEL);
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "music_disc_sally"), SALLY);
        Registry.register(Registry.ITEM, new Identifier(WeepingAngels.MODID, "music_disc_time_prevails"), TIME_PREVAILS);

    }
}
