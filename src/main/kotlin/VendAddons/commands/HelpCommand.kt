package VendAddons.commands

import VendAddons.utils.TextUtils
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class HelpCommand : ClientCommandBase("vhelp") {
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        TextUtils.info("§6§l [Vend] Help: (Page 1 of 1)", false)
        TextUtils.info("  §7/fishcontrols This toggle fishing controls [also controllable by a settable hotkey].", false)
    }
}