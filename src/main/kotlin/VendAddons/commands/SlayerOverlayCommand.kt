package VendAddons.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import VendAddons.Features.Slayers.SlayerOverlay
import VendAddons.utils.TextUtils

class SlayerOverlayCommand : ClientCommandBase("vslayeroverlay") {
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            TextUtils.info("§cUsage: /vslayeroverlay <slayer>", false)
            return
        }

        val slayer = args[0].lowercase()
        if (slayer !in listOf("zombie", "spider", "wolf", "enderman", "blaze", "vampire")) {
            TextUtils.info("§cInvalid slayer type: $slayer", false)
            return
        }

        SlayerOverlay.setActiveSlayer(slayer)
        TextUtils.info("§aTracking §b$slayer §aSlayer XP.", false)
    }
}
