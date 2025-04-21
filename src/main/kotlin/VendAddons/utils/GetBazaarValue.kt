package VendAddons.utils

import com.google.gson.JsonParser
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import VendAddons.utils.TextUtils

object GetBazaarValue {
    fun getValue(item: String, callback: (String?) -> Unit) {
        val apiUrl = "https://api.vaje.uk/skyblock/bazaar/$item"

        Thread {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val reader = InputStreamReader(connection.inputStream)

                val parser = JsonParser()
                val json = parser.parse(reader).asJsonObject
                reader.close()

                val price = json.get("instaSellPrice").toString()
                callback(price)
            } catch (e: Exception) {
                TextUtils.info("§4Failed to get bazaar value for $item")
                callback(null)
            }
        }.start()
    }
}