package uk.co.wehavecookies56.kizunacraft.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Toby on 17/05/2016.
 */
public class CommonProxy {

    public void init () {

    }

    public EntityPlayer getPlayerEntity (MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public IThreadListener getThreadFromContext (MessageContext ctx) {
        return ctx.getServerHandler().playerEntity.getServer();
    }

}
