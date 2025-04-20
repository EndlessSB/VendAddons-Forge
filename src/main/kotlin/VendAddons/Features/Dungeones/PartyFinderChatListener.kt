package VendAddons.Features.Dungeones

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.util.EnumChatFormatting
import VendAddons.utils.TextUtils
import VendAddons.config.config

object PartyFinderChatListener {

    private val regex = Regex("Party Finder > ([^\\s]+) joined the dungeon group!")

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!(config.check.get("PartyFinderLookup") ?: true)) return
        val message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.unformattedText)

        val match = regex.find(message)
        if (match != null) {
            val username = match.groupValues[1]
            TextUtils.info("§aLooking Up stats of player: $username", true)
            PartyFinderLookup.lookup(username)
        }
    }
}
