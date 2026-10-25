package net.azisaba.playerLimit

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class PlayerLimitCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val plugin = PlayerLimit.instance
        if (args.isEmpty()) {
            sender.sendGreen("現在の最大人数: ${formatLimit(plugin.maxPlayers)}")
            for (usage in listOf("set <new value>", "reset", "reload")) {
                sender.sendMessage(
                    Component.text("> ", NamedTextColor.RED)
                        .append(Component.text("/playerlimit $usage", NamedTextColor.GREEN))
                )
            }
            return true
        }
        when (args[0]) {
            "reload" -> {
                plugin.reload()
                sender.sendGreen("設定を再読み込みしました。")
                sender.sendGreen("現在の最大人数は${formatLimit(plugin.maxPlayers)}です。")
            }
            "reset" -> {
                plugin.maxPlayers = -1
                plugin.saveConfigAsync()
                sender.sendGreen("人数制限を無制限に設定しました。")
            }
            "set" -> {
                val newValue = args.getOrNull(1)?.toIntOrNull()
                if (newValue == null) {
                    sender.sendMessage(Component.text("数値を指定してください。", NamedTextColor.RED))
                    return true
                }
                plugin.maxPlayers = newValue
                plugin.saveConfigAsync()
                sender.sendGreen("人数制限を${formatLimit(newValue)}に設定しました。")
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        if (!sender.hasPermission("playerlimit.command")) return emptyList()
        if (args.size == 1) return COMMANDS.filter { it.startsWith(args[0], ignoreCase = true) }
        return emptyList()
    }

    private fun CommandSender.sendGreen(text: String) = sendMessage(Component.text(text, NamedTextColor.GREEN))

    private fun formatLimit(value: Int) = if (value == -1) "無制限" else "${value}人"

    private companion object {
        val COMMANDS = listOf("set", "reset", "reload")
    }
}
