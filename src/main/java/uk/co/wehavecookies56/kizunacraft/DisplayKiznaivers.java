package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toby on 17/05/2016.
 */
public class DisplayKiznaivers implements ICommand {

    private List<String> aliases;

    public DisplayKiznaivers() {
        this.aliases = new ArrayList<String>();
        this.aliases.add("kiznaivers");
        this.aliases.add("showkiznaivers");
        this.aliases.add("boundto");
        this.aliases.add("boundby");
    }

    @Override
    public String getCommandName() {
        return "kiznaivers";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Displays a list of people your wounds are bound by. Green means that the player is online, Grey means that the player is offline.";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (player.getCapability(Kiznaivers.KIZNAIVERS, null).hasImplant()) {
            if (!player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().isEmpty()) {
                sender.addChatMessage(new TextComponentString("You're bound by wounds with:"));
                for (int i = 0; i < player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().size(); i++) {
                    TextFormatting colour = TextFormatting.DARK_GREEN;
                    if (player.getServer().getPlayerList().getPlayerByUsername(player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i)) == null)
                        colour = TextFormatting.GRAY;
                    sender.addChatMessage(new TextComponentString(colour + "- " + player.getCapability(Kiznaivers.KIZNAIVERS, null).getKiznaivers().get(i)));
                }
            } else {
                sender.addChatMessage(new TextComponentString("You're not bound by your wounds with anyone"));
            }
        } else {
            sender.addChatMessage(new TextComponentString("You are not a Kiznaiver"));
        }

    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
