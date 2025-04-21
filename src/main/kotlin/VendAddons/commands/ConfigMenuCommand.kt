package VendAddons.commands

import VendAddons.utils.TextUtils
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import VendAddons.Graphics.ConfigGuiScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

class ConfigMenuCommand : ClientCommandBase("vend") {
    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        // Schedule the task to run on the main thread
        println("Attempting to open ConfigGuiScreen...")  // Add debug logs
        Minecraft.getMinecraft().addScheduledTask {
            println("Opening ConfigGuiScreen now.")
            Minecraft.getMinecraft().displayGuiScreen(ConfigGuiScreen())
        }
    }

}
