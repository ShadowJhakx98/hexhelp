package com.hexvencraft.hexhelp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Header
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Welcome to HexvenCraft!");
        sender.sendMessage(ChatColor.GRAY + "Here are some useful commands to get you started:");
        sender.sendMessage("");

        // Navigation Commands
        sender.sendMessage(ChatColor.GOLD + "Navigation:");
        sender.sendMessage(ChatColor.YELLOW + "/hub" + ChatColor.WHITE + " - Return to the main hub.");
        sender.sendMessage(ChatColor.YELLOW + "/survival" + ChatColor.WHITE + " - Join the Survival server.");
        sender.sendMessage(ChatColor.YELLOW + "/creative" + ChatColor.WHITE + " - Join the Creative server.");
        sender.sendMessage("");

        // Gameplay Commands
        sender.sendMessage(ChatColor.GOLD + "Gameplay:");
        sender.sendMessage(ChatColor.YELLOW + "/sethome" + ChatColor.WHITE + " - Set your home location.");
        sender.sendMessage(ChatColor.YELLOW + "/home" + ChatColor.WHITE + " - Teleport to your home.");
        sender.sendMessage(ChatColor.YELLOW + "/ask <command>" + ChatColor.WHITE + " - Ask our AI for command help.");
        sender.sendMessage("");

        // Footer
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------");

        return true;
    }
}
