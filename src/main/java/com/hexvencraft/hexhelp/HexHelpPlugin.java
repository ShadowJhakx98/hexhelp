package com.hexvencraft.hexhelp;

import org.bukkit.plugin.java.JavaPlugin;

public class HexHelpPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Setup config.yml
        this.saveDefaultConfig();

        // Register our commands
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("ask").setExecutor(new AskCommand(this)); // Register the new command

        getLogger().info("HexHelp has been enabled with AI support!");
    }
}
