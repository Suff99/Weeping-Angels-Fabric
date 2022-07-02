package craig.software.mc.angels;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeepingAngels implements ModInitializer {

	public static String MODID = "weeping_angels";

	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		Items.onInitialize();
	}
}
