package VendAddons.commands

import VendAddons.commands.ClientCommandBase
import VendAddons.utils.TextUtils
import com.google.gson.JsonParser
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class StatsCommand : ClientCommandBase("vstats") {
    companion object {
        val skillLevels = mutableMapOf<String, Int>()
        var mp = -1
        var cata = -1
        var non_mm_comps = -1
        var mm_total_comps = -1
        private val SlayerxpMap: MutableMap<String, String> = mutableMapOf()
        var secrets = -1
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            TextUtils.info("§cUsage: /vstats <username>", false)
            return
        }

        val username = args[0]
        TextUtils.info("§7Fetching stats for §b$username§7...")

        // Reset old data
        skillLevels.clear()
        SlayerxpMap.clear()
        mp = -1
        cata = -1
        non_mm_comps = -1
        mm_total_comps = -1
        secrets = -1

        Thread {
            try {
                // Threads for each fetch
                val skillThread = Thread {
                    try {
                        val url = URL("https://api.vaje.uk/skyblock/skills/$username")
                        val reader = InputStreamReader(url.openConnection().getInputStream())
                        val json = JsonParser().parse(reader).asJsonObject
                        reader.close()

                        val skills = listOf("alchemy", "carpentry", "combat", "enchanting", "farming", "fishing", "foraging", "mining", "runecraft", "social", "taming")
                        for (skill in skills) {
                            val lvlKey = "${skill}_lvl"
                            if (json.has(lvlKey)) {
                                skillLevels[skill] = json.get(lvlKey).asInt
                            }
                        }
                    } catch (e: Exception) {
                        TextUtils.info("§cFailed to fetch skills for $username", false)
                        e.printStackTrace()
                    }
                }

                val slayerThread = Thread {
                    try {
                        val url = URL("https://api.vaje.uk/skyblock/slayer?name=$username")
                        val reader = InputStreamReader(url.openConnection().getInputStream())
                        val json = JsonParser().parse(reader).asJsonObject
                        reader.close()

                        SlayerxpMap["blaze"] = json.get("blaze_xp")?.asString ?: "0"
                        SlayerxpMap["enderman"] = json.get("enderman_xp")?.asString ?: "0"
                        SlayerxpMap["spider"] = json.get("spider_xp")?.asString ?: "0"
                        SlayerxpMap["vampire"] = json.get("vampire_xp")?.asString ?: "0"
                        SlayerxpMap["wolf"] = json.get("wolf_xp")?.asString ?: "0"
                        SlayerxpMap["zombie"] = json.get("zombie_xp")?.asString ?: "0"
                    } catch (e: Exception) {
                        TextUtils.info("§cFailed to fetch Slayer XP for $username", false)
                        e.printStackTrace()
                    }
                }

                val mpThread = Thread {
                    try {
                        val url = URL("https://api.vaje.uk/skyblock/mp/$username")
                        val reader = InputStreamReader(url.openConnection().getInputStream())
                        val json = JsonParser().parse(reader).asJsonObject
                        reader.close()
                        mp = json.get("magical_power").asInt
                    } catch (e: Exception) {
                        TextUtils.info("§cFailed to fetch Magical Power for $username", false)
                        e.printStackTrace()
                    }
                }

                val cataThread = Thread {
                    try {
                        val url = URL("https://api.vaje.uk/skyblock/catainfo/$username")
                        val reader = InputStreamReader(url.openConnection().getInputStream())
                        val json = JsonParser().parse(reader).asJsonObject
                        reader.close()

                        cata = json.get("catacombs_lvl").asInt
                        non_mm_comps = json.get("Catacombs_Total_comps").asInt
                        mm_total_comps = json.get("Matermode_Total_comps").asInt
                        secrets = json.get("secrets").asInt
                    } catch (e: Exception) {
                        TextUtils.info("§cFailed to fetch Catacombs Info for $username", false)
                        e.printStackTrace()
                    }
                }

                // Start all threads
                listOf(skillThread, slayerThread, mpThread, cataThread).forEach { it.start() }
                // Wait for all to complete
                listOf(skillThread, slayerThread, mpThread, cataThread).forEach { it.join() }

                // Show results
                TextUtils.info("§aStats for §b$username")
                TextUtils.hover("§6Catacombs: §e$cata", "MM Comps: $mm_total_comps | Non MM Comps: $non_mm_comps | Secrets: $secrets")

                val skillText = StringBuilder("§eSkill Levels:\n")
                for ((skill, level) in skillLevels) {
                    skillText.append("§a${skill.replaceFirstChar { it.uppercase() }}: §f$level\n")
                }
                TextUtils.hover("§eSkills", skillText.toString().trim())

                val slayerText = StringBuilder("§6Slayer XP:\n")
                for ((slayer, xp) in SlayerxpMap) {
                    slayerText.append("§a${slayer.replaceFirstChar { it.uppercase() }}: §f$xp\n")
                }
                TextUtils.hover("§6Slayers", slayerText.toString().trim())

                TextUtils.info("§dMagical Power: $mp")

            } catch (e: Exception) {
                TextUtils.info("§cAn error occurred while fetching data.", false)
                e.printStackTrace()
            }
        }.start()
    }
}
