package uk.co.wehavecookies56.kizunacraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Created by Toby on 20/05/2016.
 */
public class ConfigHandler {

    public static int maxKiznaivers;

    private static Configuration config;

    public static void init (File file) {
        config = new Configuration(file);

        config.load();
        loadConfigValues();

        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }

    private static void loadConfigValues() {
        maxKiznaivers = config.get(Configuration.CATEGORY_GENERAL, "maximum kiznaivers", 7, "Use this to change the max number of Kiznaivers (Default 7)", 0, Integer.MAX_VALUE).getInt();


        if (config.hasChanged()) config.save();
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent event) {
        if (event.getModID().equals("kizunacraft")) loadConfigValues();
    }

}
