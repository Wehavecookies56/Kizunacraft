package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Toby on 21/05/2016.
 */
public class GuiKiznaivers extends GuiScreen {

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent event) {
        if (ConfigHandler.showGui) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                if (player.getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
                    Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Kizunacraft.kiznaiverImplant), 0, 0);
                    if (!player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().isEmpty()) {
                        for (int i = 0; i < player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size(); i++) {
                            drawString(Minecraft.getMinecraft().fontRendererObj, player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i), event.getResolution().getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i)), Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * i, 0xFFFFFF);
                        }
                        //EntityPlayer otherPlayer = player.getServer().getPlayerList().getPlayerByUsername(player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(0));
                        //if (otherPlayer != null)
                        //drawString(Minecraft.getMinecraft().fontRendererObj, otherPlayer.getDisplayNameString(), 0, 0, 0xFFFFFF);
                    /*for (int i = 0; i < player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size(); i++) {
                        EntityPlayerMP otherPlayer =  player.getServer().getPlayerList().getPlayerByUsername(player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i));
                        int colour = 0x00AA00;
                        if (otherPlayer == null)
                            colour = 0xAAAAAA;
                        drawString(Minecraft.getMinecraft().fontRendererObj, otherPlayer.getDisplayNameString(), width - Minecraft.getMinecraft().fontRendererObj.getStringWidth(otherPlayer.getDisplayNameString()), height - (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * i), colour);
                    }*/
                    }
                }
            }
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
