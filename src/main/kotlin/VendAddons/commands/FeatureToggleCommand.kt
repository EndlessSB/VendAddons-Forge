package VendAddons.commands

import VendAddons.commands.ClientCommandBase
import VendAddons.utils.TextUtils
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import VendAddons.config.config


class FeatureToggleCommand : ClientCommandBase("vtoggle") {

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            TextUtils.info("§cUsage: /vtoggle <feature>", false)
            return
        }

        val feature = args[0]

        val current = config.check.get(feature)
        if (current == null) {
            TextUtils.info("§cUnknown feature: $feature", false)
            return
        }

        val newValue = !current
        config.update.set(feature, newValue)

        TextUtils.info("§aToggled §e$feature§a to §b$newValue", false)
    }
}
