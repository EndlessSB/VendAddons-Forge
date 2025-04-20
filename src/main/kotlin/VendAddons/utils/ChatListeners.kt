package VendAddons.utils

import VendAddons.Features.Dungeones.PartyFinderLookup
import VendAddons.Features.Slayers.SlayerOverlay
import VendAddons.config.config
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatListeners {

    private val partyfinderregex = Regex("Party Finder > ([^\\s]+) joined the dungeon group!")

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        val message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.unformattedText)

        if (message == "  SLAYER QUEST COMPLETE!") {
            SlayerOverlay.addSessionKill()
        }

        val partyfindermsg = partyfinderregex.find(message)
        if (partyfindermsg != null) {
            if (!(config.check.get("PartyFinderLookup") ?: true)) return
            val username = partyfindermsg.groupValues[1]
            TextUtils.info("§aLooking Up stats of player: $username", true)
            PartyFinderLookup.lookup(username)
        }
    }

}