package VendAddons.Features.Slayers

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
import VendAddons.ExampleMod

object SlayerOverlay {
    private var activeSlayer: String? = null
    private val xpMap: MutableMap<String, String> = mutableMapOf()
    private var lastUsername: String? = null

    private var sessionKills = 0
    private var sessionStart: Long? = null

    init {
        // Timer to refresh Slayer XP every 60 seconds
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

    fun setActiveSlayer(slayer: String, username: String? = null) {
        activeSlayer = slayer.lowercase()
        sessionKills = 0
        sessionStart = null

        val name = username ?: Minecraft.getMinecraft().session?.username
        if (name != null) {
            lastUsername = name
            lookup(name)
        }
    }

    fun lookup(username: String) {
        val url = URL("https://api.vaje.uk/skyblock/slayer?name=$username")
        if (!(config.check.getOrDefault("SlayerXpOverlay", false))) return
        Thread {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val reader = InputStreamReader(connection.inputStream)
                val json = JsonParser().parse(reader).asJsonObject
                reader.close()

                xpMap["blaze"] = json.get("blaze_xp")?.asString ?: "0"
                xpMap["enderman"] = json.get("enderman_xp")?.asString ?: "0"
                xpMap["spider"] = json.get("spider_xp")?.asString ?: "0"
                xpMap["vampire"] = json.get("vampire_xp")?.asString ?: "0"
                xpMap["wolf"] = json.get("wolf_xp")?.asString ?: "0"
                xpMap["zombie"] = json.get("zombie_xp")?.asString ?: "0"


            } catch (e: Exception) {
                TextUtils.info("§cFailed to fetch Slayer XP for $username", false)
                e.printStackTrace()
            }
        }.start()
    }

    fun addSessionKill() {
        val slayer = activeSlayer ?: return

        sessionKills++
        if (sessionStart == null) sessionStart = System.currentTimeMillis()

        val baseXP = when (slayer.lowercase()) {
            "zombie" -> 1500
            "vampire" -> 150
            else -> 500
        }

        val oldXP = xpMap[slayer.lowercase()]?.replace(",", "")?.toIntOrNull() ?: 0
        xpMap[slayer.lowercase()] = (oldXP + baseXP).toString()
    }

    private fun getHourlyKills(): String {
        if (sessionStart == null || sessionKills == 0) return "0.0"
        val elapsed = (System.currentTimeMillis() - sessionStart!!) / 1000.0 / 3600.0
        return String.format("%.1f", sessionKills / elapsed)
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Text) {
        val slayerEnabled = config.check.getOrDefault("SlayerXPOverlay", false)
        val killsPerHourEnabled = config.check.getOrDefault("SlayerKillsPerHour", false)
        if (!slayerEnabled) return

        val mc = Minecraft.getMinecraft()
        val x = 10f
        var y = 150f

        val displayText = when {
            activeSlayer == null -> "§7Please run §e/vslayeroverlay <slayer>"
            else -> {
                val xp = xpMap[activeSlayer]?.replace(",", "")?.toIntOrNull()?.let { "%,d".format(it) } ?: "0"
                "§6Vend > ${activeSlayer!!.replaceFirstChar { it.uppercaseChar() }} Slayer XP: §b$xp"
            }
        }

        mc.fontRendererObj.drawString(displayText, x, y, 0xFFFFFF, true)

        if (killsPerHourEnabled && sessionKills > 0 && activeSlayer != null) {
            y += 12f
            val rateText = "§7Slayer kills/hour: §a${getHourlyKills()}"
            mc.fontRendererObj.drawString(rateText, x, y, 0xFFFFFF, true)
        }
    }
}
