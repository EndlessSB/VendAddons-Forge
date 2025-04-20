package VendAddons.Features.Slayers

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.util.EnumChatFormatting
import VendAddons.utils.TextUtils
import VendAddons.config.config
import VendAddons.Features.Slayers.SlayerOverlay

object SlayerOverlayChatListener {

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        val message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.unformattedText)

        if (message == "  SLAYER QUEST COMPLETE!") {
            SlayerOverlay.addSessionKill()
        }

    }
}
