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

    //Main
    public static int maxKiznaivers;
    public static boolean keepOnDeath;

    //Client
    public static boolean showGui;

    private static Configuration config;
    private static Configuration clientConfig;

    public static void init (File file) {
        config = new Configuration(file);

        config.load();
        loadConfigValues();

        MinecraftForge.EVENT_BUS.register(new ConfigChange());
    }

    public static void initClientConfig(File file) {
        clientConfig = new Configuration(file);

        clientConfig.load();
        loadClientConfigValues();
        MinecraftForge.EVENT_BUS.register(new ConfigChangeClient());
    }

    private static void loadClientConfigValues() {
        showGui = clientConfig.get(Configuration.CATEGORY_CLIENT, "show hud elements", true, "Toggles the elements added to the hud by Kizunacraft").getBoolean();

        if (clientConfig.hasChanged()) clientConfig.save();
    }

    private static void loadConfigValues() {
        maxKiznaivers = config.get(Configuration.CATEGORY_GENERAL, "maximum kiznaivers", 7, "Sets the max number of Kiznaivers (Default 7)", 0, Integer.MAX_VALUE).getInt();
        keepOnDeath = config.get(Configuration.CATEGORY_GENERAL, "keep kizuna system implant on death", false, "Toggles whether the kizuna implant will drop on death").getBoolean();

        if (config.hasChanged()) config.save();
    }

    public static class ConfigChange {
        @SubscribeEvent
        public void onConfigChange(ConfigChangedEvent event) {
            if (event.getModID().equals("kizunacraft")) loadConfigValues();
        }
    }

    public static class ConfigChangeClient {
        @SubscribeEvent
        public void onConfigChange(ConfigChangedEvent event) {
            if (event.getModID().equals("kizunacraft")) loadClientConfigValues();
        }
    }

}
