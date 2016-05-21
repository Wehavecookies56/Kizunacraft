package uk.co.wehavecookies56.kizunacraft.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import uk.co.wehavecookies56.kizunacraft.GuiKiznaivers;
import uk.co.wehavecookies56.kizunacraft.Kizunacraft;

/**
 * Created by Toby on 17/05/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiKiznaivers());
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Kizunacraft.kiznaiverImplant, 0, new ModelResourceLocation("kizunacraft:" + Kizunacraft.kiznaiverImplant.getUnlocalizedName().substring(5), "inventory"));
    }

    @Override
    public EntityPlayer getPlayerEntity (MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext (MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
    }
}
