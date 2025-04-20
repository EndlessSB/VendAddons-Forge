package VendAddons.Features.Dungeones

import VendAddons.utils.TextUtils
import com.google.gson.JsonParser
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.util.EnumChatFormatting
import scala.tools.nsc.transform.patmat.Logic

object PartyFinderLookup {

    fun lookup(username: String) {
        val catainfourl = "https://api.vaje.uk/skyblock/catainfo/$username"

        Thread {
            // Declare the vars outside the try blocks so they're accessible later
            var cata = -1
            var non_mm_comps = -1
            var mm_total_comps = -1
            var mp = -1
            var secrets = -1

            try {
                val url = URL(catainfourl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val reader = InputStreamReader(connection.inputStream)
                val parser = JsonParser()
                val json = parser.parse(reader).asJsonObject
                reader.close()

                cata = json.get("catacombs_lvl").asInt
                non_mm_comps = json.get("Catacombs_Total_comps").asInt
                mm_total_comps = json.get("Matermode_Total_comps").asInt
                secrets = json.get("secrets").asInt
            } catch (e: Exception) {
                TextUtils.info("§cFailed to fetch Catainfo for $username", false)
                e.printStackTrace()
            }

            try {
                val url = URL("https://api.vaje.uk/skyblock/mp/$username")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val reader = InputStreamReader(connection.inputStream)
                val parser = JsonParser()
                val json = parser.parse(reader).asJsonObject
                reader.close()

                mp = json.get("magical_power").asInt
            } catch (e: Exception) {
                TextUtils.info("§cFailed to Fetch MP for $username")
                e.printStackTrace()
            }
            TextUtils.info("Dungeon Stats: $username")
            TextUtils.info("§cCata Lvl: $cata")
            TextUtils.hover("Collections", "§bNon MM: $non_mm_comps | MM: $mm_total_comps")
            TextUtils.info("§2Secrets: $secrets")
            TextUtils.info("§dMagical Power: $mp")
            TextUtils.clickableButton("§c[KICK]", "p kick $username", "§cKick $username")
        }.start()
    }
}
