import VendAddons.commands.ClientCommandBase
import VendAddons.utils.TextUtils
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SkillsCommand : ClientCommandBase("vskills") {

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            TextUtils.info("§cUsage: /vskills <username>", false)
            return
        }

        val username = args[0]
        val apiUrl = "https://api.vaje.uk/skyblock/skills/$username"

        Thread {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val reader = InputStreamReader(connection.inputStream)

                // Create JsonParser instance
                val parser = JsonParser()
                val json = parser.parse(reader).asJsonObject
                reader.close()

                val skills = listOf(
                    "alchemy", "carpentry", "combat", "enchanting", "farming",
                    "fishing", "foaraging", "mining", "runecraft", "social", "taming"
                )

                TextUtils.info("§6§l[Vend] Skills for $username:", false)

                for (skill in skills) {
                    val lvlKey = "${skill}_lvl"
                    if (json.has(lvlKey)) {
                        val level = json.get(lvlKey).asInt
                        TextUtils.info("§e${skill.replaceFirstChar { it.uppercase() }}: §bLevel $level", false)
                    }
                }

            } catch (e: Exception) {
                TextUtils.info("§cFailed to fetch skills for $username", false)
                e.printStackTrace()
            }
        }.start()
    }
}
