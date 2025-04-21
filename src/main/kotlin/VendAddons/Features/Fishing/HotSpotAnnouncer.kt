package VendAddons.Features.Fishing

import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import VendAddons.utils.TextUtils
import VendAddons.config.config
import net.minecraft.entity.item.EntityArmorStand
import java.util.concurrent.ConcurrentHashMap

object HotSpotAnnouncer {
    private val detectedHotspots = ConcurrentHashMap<String, Long>()
    private const val EXPIRATION_MS = 5 * 60 * 1000L // 5 minutes in milliseconds

    fun init() {
        val enabled = config.check.getOrDefault("HotSpotAnnouncer", false) as? Boolean ?: false
        if (!enabled) return
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        val mc = Minecraft.getMinecraft()
        val world = mc.theWorld ?: return
        val now = System.currentTimeMillis()

        // Clean up expired entries
        detectedHotspots.entries.removeIf { now - it.value > EXPIRATION_MS }

        // Collect all visible, named armor stands (holograms)
        val holograms = world.loadedEntityList
            .filterIsInstance<EntityArmorStand>()
            .filter { !it.isInvisible && it.customNameTag.isNotBlank() }

        // Group by block position (x/y/z rounded to integers)
        val grouped = holograms.groupBy {
            BlockPos(it.posX.toInt(), it.posY.toInt(), it.posZ.toInt())
        }

        for ((blockPos, group) in grouped) {
            val strippedTags = group.map { StringUtils.stripControlCodes(it.customNameTag) }

            val hasHotspot = strippedTags.any { it.uppercase().contains("HOTSPOT") }
            val bonusLine = strippedTags.firstOrNull { !it.uppercase().contains("HOTSPOT") }

            if (hasHotspot && bonusLine != null) {
                val coords = formatCoords(blockPos)

                if (!detectedHotspots.containsKey(coords)) {
                    detectedHotspots[coords] = now

                    mc.ingameGUI.displayTitle("§eHOTSPOT DETECTED!", "", 10, 40, 10)

                    val msg = "[§dVEND§r](§aNEW-HOTSPOT!§r)[§bCOORDS:$coords§r][§6Bonus:$bonusLine§r]"
                    TextUtils.sendPartyChatMessage(msg)
                }
            }
        }
    }

    private fun formatCoords(pos: BlockPos): String {
        return "${pos.x}, ${pos.y}, ${pos.z}"
    }
}
