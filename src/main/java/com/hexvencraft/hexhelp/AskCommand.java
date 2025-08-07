package com.hexvencraft.hexhelp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AskCommand implements CommandExecutor {

    private final HexHelpPlugin plugin;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public AskCommand(HexHelpPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify a command to ask about. Usage: /ask <command>");
            return true;
        }

        Player player = (Player) sender;
        String apiKey = plugin.getConfig().getString("gemini-api-key");
        String commandToAsk = args[0];

        if (apiKey == null || apiKey.equals("YOUR_API_KEY_HERE")) {
            player.sendMessage(ChatColor.RED + "The AI Help system is not configured correctly by the server owner.");
            return true;
        }

        player.sendMessage(ChatColor.GRAY + "Asking the HexvenCraft AI about /" + commandToAsk + "...");

        // To prevent server lag, we must run the web request on a separate thread.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // 1. Construct the prompt for the AI
                String prompt = "You are a helpful and friendly Minecraft server assistant for HexvenCraft. " +
                        "A player is asking for help with the command '/" + commandToAsk + "'. " +
                        "Provide a concise, one- or two-sentence description of what this command does and how to use it. " +
                        "Be cheerful and simple. Do not engage in conversation.";

                // 2. Construct the request body for the Gemini API
                String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

                // 3. Create the HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                // 4. Send the request and get the response
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 5. Parse the response to get the generated text
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                String aiResponse = jsonResponse.getAsJsonArray("candidates")
                                                .get(0).getAsJsonObject()
                                                .getAsJsonObject("content")
                                                .getAsJsonArray("parts")
                                                .get(0).getAsJsonObject()
                                                .get("text").getAsString();

                // 6. Send the response back to the player on the main server thread
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Hex AI: " + ChatColor.WHITE + aiResponse.trim());
                });

            } catch (Exception e) {
                // If anything goes wrong, inform the player on the main thread
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.sendMessage(ChatColor.RED + "Sorry, I was unable to get an answer at this time.");
                    plugin.getLogger().warning("Error with Gemini API call: " + e.getMessage());
                });
            }
        });

        return true;
    }
}
