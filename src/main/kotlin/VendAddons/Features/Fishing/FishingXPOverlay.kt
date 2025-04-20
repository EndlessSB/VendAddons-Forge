package VendAddons.Features.Fishing

import VendAddons.utils.TextUtils
import VendAddons.config.config
import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Timer
import java.util.TimerTask

object FishingXPOverlay {
    var fishingXp = ""
    private var lastUsername = ""

    init {
        // Timer to refresh Fishing XP every 60 seconds
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val username = Minecraft.getMinecraft().session?.username
                if (username != null) {
                    lastUsername = username
                    lookup(username)
                }
            }
        }, 0, 60000)
    }

    fun lookup(username: String) {
        if (!config.check.getOrDefault("FishingXPOverlay", false)) return

        val url = URL("https://api.vaje.uk/skyblock/skills/$username")
        Thread {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val reader = InputStreamReader(connection.inputStream)
                val json = JsonParser().parse(reader).asJsonObject
                reader.close()

                // Extract fishing XP as a clean, comma-formatted string
                fishingXp = json.get("fishing_xp")?.asString ?: "0"

            } catch (e: Exception) {
                TextUtils.info("§cFailed to fetch Fishing XP for $username", false)
                e.printStackTrace()
            }
        }.start()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Text) {
        if (!config.check.getOrDefault("FishingXPOverlay", false)) return

        val mc = Minecraft.getMinecraft()
        val x = 10f
        val y = 180f

        if (fishingXp.isNotEmpty()) {
            val text = "§6Vend > §bFishing XP: §a$fishingXp"
            mc.fontRendererObj.drawString(text, x, y, 0xFFFFFF, true)
        }
    }
}
