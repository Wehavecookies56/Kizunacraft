package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import uk.co.wehavecookies56.kizunacraft.network.KiznaiverSync;
import uk.co.wehavecookies56.kizunacraft.network.PacketDispatcher;

import java.util.List;

/**
 * Created by Toby on 17/05/2016.
 */
public class KizunaImplant extends Item {

    public KizunaImplant() {
        this.setUnlocalizedName("kiznaiverimplant");
        this.setCreativeTab(Kizunacraft.kizunacraft);
        this.setRegistryName("kiznaiverimplant");
        this.setCreativeTab(Kizunacraft.kizunacraft);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            if (!player.getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
                //func_190920_e -> setStackSize
                player.getActiveItemStack().func_190920_e(0);
                player.inventory.removeStackFromSlot(player.inventory.currentItem);
                player.getCapability(Kiznaivers.KIZNAIVERS, null).setHasImplant(true);
                player.addChatMessage(new TextComponentString("You have connected yourself to the Kizuna system and have become a Kiznaiver, you can now be bound by your wounds"));
                player.addChatMessage(new TextComponentString("Interact with others using the Kizuna system to be bound by their wounds"));
                PacketDispatcher.sendTo(new KiznaiverSync(player.getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) player);
            } else {
                player.addChatMessage(new TextComponentString("You are already a Kiznaiver"));
                PacketDispatcher.sendTo(new KiznaiverSync(player.getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) player);
            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (playerIn.getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
            tooltip.add("You are already a Kiznaiver");
        }
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

}
