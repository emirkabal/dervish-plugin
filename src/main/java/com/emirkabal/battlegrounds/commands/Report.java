package com.emirkabal.battlegrounds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;

public class Report implements CommandExecutor {

    String webhookUrl = Core.cfg.getString("report-webhook-url");
    final String[] validReasons = {"cheat", "bug", "abuse", "other"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }

        Player p = (Player) sender;

        if (args.length < 2) {
            p.sendMessage("/report <[playername]> <[reason]>");
            return false;
        }

        if (webhookUrl == null || webhookUrl.isEmpty()) {
            p.sendMessage("report-webhook-url is not set in the config.");
            return false;
        }

        Player targetPlayer = p.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            p.sendMessage("Player not found.");
            return false;

        }

        if (p.getName().equalsIgnoreCase(targetPlayer.getName())) {
            p.sendMessage("You cannot report yourself.");
            return false;
        }
        String reason = args[1].toLowerCase();
        boolean isValidReason = false;

        for (String validReason : validReasons) {
            if (reason.equals(validReason)) {
                isValidReason = true;
                break;
            }
        }
        if (!isValidReason) {
            p.sendMessage("Invalid reason. Valid reasons are: cheat, bug, abuse, other.");
            return false;
        }

        StringBuilder message = new StringBuilder("Report from " + p.getName() + " to " + targetPlayer.getName() + ": " + reason);
        for (int i = 2; i < args.length; i++) {
            message.append(" ").append(args[i]);
        }

        String jsonPayload = "{\"content\": \"" + message.toString() + "\"}";
        try {
            java.net.URL url = new java.net.URL(webhookUrl);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            java.io.OutputStream os = conn.getOutputStream();
            os.write(jsonPayload.getBytes("UTF-8"));
            os.close();
            conn.getResponseCode();
        } catch (Exception e) {
            p.sendMessage("An unexpected error occurred. Please try again later.");
            System.out.println("Error sending report: " + e.getMessage());
        }

        p.sendMessage("Successfully reported §e" + targetPlayer.getName() + "§r for §e" + reason + "§8.");

        return true;
    }
}
