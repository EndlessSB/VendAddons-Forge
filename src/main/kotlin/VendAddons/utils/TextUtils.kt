package VendAddons.utils

import VendAddons.ExampleMod
import net.minecraft.util.ChatComponentText

object TextUtils {
    fun info(text: String, prefix: Boolean = true) {
        if (ExampleMod.mc.thePlayer == null) return

        val textPrefix = if (prefix) "§6§l[Vend] " else ""
        ExampleMod.mc.thePlayer.addChatMessage(ChatComponentText("$textPrefix$text§r"))
    }

    fun toggledMessage(message: String, state: Boolean) {
        val stateText = if (state) "§aON" else "§cOFF"
        info("§9Toggled $message §8[$stateText§8]§r")
    }

    fun sendPartyChatMessage(message: String) {
        sendMessage("/pc $message")
    }
    fun runCommand(command: String) {
        sendMessage(("/$command"))
    }

    fun sendMessage(message: String) {
        ExampleMod.mc.thePlayer.sendChatMessage(message)
    }
}