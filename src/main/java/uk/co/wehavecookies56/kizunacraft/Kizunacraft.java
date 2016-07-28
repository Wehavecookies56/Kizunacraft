package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;
import uk.co.wehavecookies56.kizunacraft.network.ConfigSync;
import uk.co.wehavecookies56.kizunacraft.network.KiznaiverSync;
import uk.co.wehavecookies56.kizunacraft.network.PacketDispatcher;
import uk.co.wehavecookies56.kizunacraft.proxies.CommonProxy;

/**
 * Created by Toby on 16/05/2016.
 */
@Mod(modid = "kizunacraft", name = "Kizunacraft", version = "1.4", updateJSON = "https://raw.githubusercontent.com/Wehavecookies56/Kizunacraft/master/update.json")
public class Kizunacraft {

    public static Item kiznaiverImplant;
    public static CreativeTabs kizunacraft;

    @SidedProxy(clientSide = "uk.co.wehavecookies56.kizunacraft.proxies.ClientProxy", serverSide = "uk.co.wehavecookies56.kizunacraft.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        PacketDispatcher.registerPackets();
        kizunacraft = new CreativeTabs("kizunacraft") {
            @Override
            public Item getTabIconItem() {
                return kiznaiverImplant;
            }
        };
        kiznaiverImplant = new KizunaImplant();
        GameRegistry.register(kiznaiverImplant);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.addShapedRecipe(new ItemStack(kiznaiverImplant),
            "RRR",
            "CDC",
            "RRR",
            'R', Items.REDSTONE, 'C', Blocks.CACTUS, 'D', Items.DIAMOND
        );
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Kiznaivers());
        Kiznaivers.register();
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("kk")) {
            FMLLog.log("kizunacraft", Level.INFO, "Hey kk why isn't there a 1.9 version released yet?");
        }
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new DisplayKiznaivers());
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            PacketDispatcher.sendTo(new KiznaiverSync(event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) event.getEntity());
            PacketDispatcher.sendTo(new ConfigSync(ConfigHandler.maxKiznaivers), (EntityPlayerMP) event.getEntity());
        }
    }

    @SubscribeEvent
    public void death(LivingDropsEvent event) {
        if (!event.getEntity().worldObj.isRemote) {
            if (event.getEntity() instanceof EntityPlayer) {
                if (event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
                    if (!ConfigHandler.keepOnDeath) {
                        event.getDrops().add(new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(kiznaiverImplant)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && !ConfigHandler.keepOnDeath) {
            for (int i = 0; i < event.getOriginal().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size(); i++) {
                EntityPlayer kiznaiver = event.getOriginal().getServer().getPlayerList().getPlayerByUsername(event.getOriginal().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i));
                if (kiznaiver != null) {
                    kiznaiver.addChatComponentMessage(new TextComponentString("You're no longer bound by " + event.getOriginal().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i) + "'s wounds"));
                    if (kiznaiver.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().contains(event.getOriginal().getDisplayNameString()))
                        if (kiznaiver.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().indexOf(event.getOriginal().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i)) != -1)
                            kiznaiver.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().remove(kiznaiver.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().indexOf(event.getOriginal().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i)));
                }
            }
        } else {
            EntityPlayer original = event.getOriginal();
            EntityPlayer current = event.getEntityPlayer();
            Kiznaivers.IKiznaivers originalKiznaivers = original.getCapability(Kiznaivers.KIZNAIVERS, null);
            Kiznaivers.IKiznaivers currentKiznaivers = current.getCapability(Kiznaivers.KIZNAIVERS, null);
            currentKiznaivers.getKiznaivers().addAll(originalKiznaivers.getKiznaivers());
            currentKiznaivers.setHasImplant(originalKiznaivers.hasImplant());
        }
        PacketDispatcher.sendTo(new KiznaiverSync(event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) event.getEntity());
    }

    @SubscribeEvent
    public void interact(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand().equals(EnumHand.MAIN_HAND)) {
            if (!event.getWorld().isRemote) {
                if (((EntityPlayer) event.getEntity()).getHeldItemMainhand() == null) {
                    if (event.getTarget() instanceof EntityPlayer) {
                        if (event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant() && event.getTarget().getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
                            if (event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().contains(((EntityPlayer) event.getTarget()).getDisplayNameString())) {
                                ((EntityPlayerMP) event.getEntity()).addChatComponentMessage(new TextComponentString("You are already bound by " + ((EntityPlayerMP) event.getTarget()).getDisplayNameString() + "'s wounds"));
                            } else {
                                if (event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size() < ConfigHandler.maxKiznaivers) {
                                    if (event.getTarget().getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size() < ConfigHandler.maxKiznaivers) {
                                        event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null).bindKiznaiver(((EntityPlayer) event.getTarget()).getDisplayNameString());
                                        event.getTarget().getCapability(Kiznaivers.KIZNAIVERS, null).bindKiznaiver(((EntityPlayer) event.getEntity()).getDisplayNameString());
                                        PacketDispatcher.sendTo(new KiznaiverSync(event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) event.getEntity());
                                        PacketDispatcher.sendTo(new KiznaiverSync(event.getTarget().getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) event.getTarget());
                                        ((EntityPlayerMP) event.getEntity()).addChatComponentMessage(new TextComponentString("You have been bound by " + ((EntityPlayerMP) event.getTarget()).getDisplayNameString() + "'s wounds"));
                                        ((EntityPlayerMP) event.getTarget()).addChatComponentMessage(new TextComponentString("You have been bound by " + ((EntityPlayerMP) event.getEntity()).getDisplayNameString() + "'s wounds"));
                                    } else {
                                        ((EntityPlayerMP) event.getEntity()).addChatComponentMessage(new TextComponentString(((EntityPlayer) event.getTarget()).getDisplayNameString() + "has reached the limit of Kiznaivers that can be bound at the same time"));
                                    }
                                } else {
                                    ((EntityPlayerMP) event.getEntity()).addChatComponentMessage(new TextComponentString("You have reached the limit of Kiznaivers that can be bound at the same time"));
                                }
                            }
                        } else if (event.getEntity().getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
                            event.getEntity().addChatMessage(new TextComponentString("This person is not a Kiznaiver therefore, they cannot be bound by your wounds."));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void interact(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand().equals(EnumHand.MAIN_HAND)) {
            if (!event.getWorld().isRemote) {
                if ((event.getEntityPlayer()).getHeldItemMainhand() == null) {
                    if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CACTUS) {
                        if (event.getEntityPlayer().getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
                            EntityPlayer player = event.getEntityPlayer();
                            player.getCapability(Kiznaivers.KIZNAIVERS, null).setHasImplant(false);
                            player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().clear();
                            PacketDispatcher.sendTo(new KiznaiverSync(event.getEntityPlayer().getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) event.getEntityPlayer());
                            event.getWorld().spawnEntityInWorld(new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(kiznaiverImplant)));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        DamageSource kiznaiverDamage = new KiznaiverDamage("kizunacraft.kiznaiverDamage", event.getSource().getSourceOfDamage());
        if (event.getSource().getDamageType() != kiznaiverDamage.getDamageType()) {
            if (!event.getEntity().worldObj.isRemote) {
                if (event.getEntity() instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) event.getEntity();
                    PacketDispatcher.sendTo(new KiznaiverSync(player.getCapability(Kiznaivers.KIZNAIVERS, null)), (EntityPlayerMP) player);
                    if (!player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().isEmpty()) {
                        int count = player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size();
                        for (int i = 0; i < player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size(); i++) {
                            EntityPlayer kiznaiver = player.getServer().getPlayerList().getPlayerByUsername(player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i));
                            if (kiznaiver == null)
                                count -= 1;
                            else
                                kiznaiver.attackEntityFrom(kiznaiverDamage, event.getAmount() / (count + 1));
                        }
                        if (count > 0)
                            event.setAmount(event.getAmount() / (count + 1));
                    }
                }
            }
        }
    }
}
