package net.azisaba.playerLimit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PlayerLimitCommand implements TabExecutor {
    private static final List<String> commands = Arrays.asList("set", "reset", "reload");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "> " + ChatColor.GREEN + "/playerlimit set <new value>");
            sender.sendMessage(ChatColor.RED + "> " + ChatColor.GREEN + "/playerlimit reset");
            sender.sendMessage(ChatColor.RED + "> " + ChatColor.GREEN + "/playerlimit reload");
            return true;
        }
        switch (args[0]) {
            case "reload":
                PlayerLimit.getInstance().reload();
                sender.sendMessage(ChatColor.GREEN + "設定を再読み込みしました。");
                break;
            case "reset":
                PlayerLimit.getInstance().maxPlayers = -1;
                PlayerLimit.getInstance().saveConfigAsync();
                sender.sendMessage(ChatColor.GREEN + "人数制限を無制限に設定しました。");
                break;
            case "set":
                if (args.length > 1) {
                    try {
                        int newValue = Integer.parseInt(args[1]);
                        PlayerLimit.getInstance().maxPlayers = newValue;
                        PlayerLimit.getInstance().saveConfigAsync();
                        String s = newValue == -1 ? "無制限" : (newValue + "人");
                        sender.sendMessage(ChatColor.GREEN + "人数制限を" + s + "に設定しました。");
                        return true;
                    } catch (NumberFormatException ignore) {
                    }
                }
                sender.sendMessage(ChatColor.RED + "数値を指定してください。");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("playerlimit.command")) return Collections.emptyList();
        if (args.length == 1) return filterCommands(args[0]);
        return Collections.emptyList();
    }

    private static List<String> filterCommands(String s) {
        return PlayerLimitCommand.commands.stream().filter(s1 -> s1.toLowerCase(Locale.ROOT).startsWith(s.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
    }
}
