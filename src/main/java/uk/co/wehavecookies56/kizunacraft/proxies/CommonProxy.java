package uk.co.wehavecookies56.kizunacraft.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import uk.co.wehavecookies56.kizunacraft.ConfigHandler;

import java.io.File;

/**
 * Created by Toby on 17/05/2016.
 */
public class CommonProxy {

    public void preInit (FMLPreInitializationEvent event) {
        ConfigHandler.init(new File(event.getModConfigurationDirectory().getPath() + File.separator + "Kizunacraft" + File.separator + "main.cfg"));
    }

    public void init (FMLInitializationEvent event) {

    }

    public EntityPlayer getPlayerEntity (MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public IThreadListener getThreadFromContext (MessageContext ctx) {
        return ctx.getServerHandler().playerEntity.getServer();
    }

}
