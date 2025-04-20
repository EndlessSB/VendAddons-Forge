package VendAddons.utils

import VendAddons.ExampleMod
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.event.HoverEvent
import net.minecraft.event.HoverEvent.Action

object TextUtils {
    fun info(text: String, prefix: Boolean = true) {
        if (ExampleMod.mc.thePlayer == null) return

        val textPrefix = if (prefix) "§6§l[Vend] " else ""
        ExampleMod.mc.thePlayer.addChatMessage(ChatComponentText("$textPrefix$text§r"))
    }

    fun hover(displayText: String, hoverText: String, prefix: Boolean = true) {
        if (ExampleMod.mc.thePlayer == null) return

        val textPrefix = if (prefix) "§6§l[Vend] " else ""

        val component = ChatComponentText("$textPrefix$displayText")
        val style = ChatStyle()
        style.chatHoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            ChatComponentText(hoverText)
        )
        component.chatStyle = style

        ExampleMod.mc.thePlayer.addChatMessage(component)
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
    fun clickableButton(displayText: String, command: String, hoverText: String = "", prefix: Boolean = true) {
        if (ExampleMod.mc.thePlayer == null) return

        val textPrefix = if (prefix) "§6§l[Vend] " else ""

        val component = ChatComponentText("$textPrefix§e$displayText")
        val style = ChatStyle()

        style.chatClickEvent = net.minecraft.event.ClickEvent(
            net.minecraft.event.ClickEvent.Action.RUN_COMMAND,
            "/$command"
        )

        if (hoverText.isNotEmpty()) {
            style.chatHoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ChatComponentText(hoverText)
            )
        }

        component.chatStyle = style

        ExampleMod.mc.thePlayer.addChatMessage(component)
    }

}