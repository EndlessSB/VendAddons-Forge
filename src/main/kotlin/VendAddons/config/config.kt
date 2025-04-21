package VendAddons.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

object config {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile = File("./config/VendAddons/config.json")

    private val defaultSettings: MutableMap<String, Boolean> = mutableMapOf(
        "PartyFinderLookup" to false,
        "SlayerXPOverlay" to false,
        "SlayerKillsPerHour" to false,
        "FishingXPOverlay" to false,
        "RareDropsOverlay" to false
    )

    private var settings: MutableMap<String, Boolean> = defaultSettings.toMutableMap()

    // Function to get the default settings
    fun getDefaultSettings(): MutableMap<String, Boolean> {
        return defaultSettings
    }

    object check {
        fun get(option: String): Boolean? {
            return settings[option]
        }

        fun getOrDefault(option: String, default: Boolean): Boolean {
            return settings[option] ?: default
        }
    }

    object update {
        fun set(option: String, value: Boolean) {
            settings[option] = value
            saveConfig()
        }
    }

    fun init() {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            saveConfig()
        } else {
            loadConfig()
        }
    }

    private fun loadConfig() {
        val type = object : TypeToken<Map<String, Boolean>>() {}.type
        val loaded: Map<String, Boolean> = gson.fromJson(configFile.readText(), type)

        // Merge loaded settings with defaults (in case new options were added later)
        settings = defaultSettings.toMutableMap()
        for ((key, value) in loaded) {
            settings[key] = value
        }
    }

    private fun saveConfig() {
        val json = gson.toJson(settings)
        configFile.writeText(json)
    }
}
