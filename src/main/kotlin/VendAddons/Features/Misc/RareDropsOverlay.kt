package VendAddons.Features.Misc

import VendAddons.config.config
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object RareDropsOverlay {
    private var lastDropText = ""
    private var displayUntil = 0L  // Time in ms to stop showing text

    fun announceDrop(dropName: String, profit: String? = null) {
        val formatted = if (profit != null && profit.isNotBlank()) {
            "§6Vend > $dropName §6[$profit]"
        } else {
            "§6Vend > $dropName"
        }

        lastDropText = formatted
        displayUntil = System.currentTimeMillis() + 5000 // Show for 5 seconds
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Text) {
        val enabled = config.check.getOrDefault("RareDropsOverlay", false)
        if (System.currentTimeMillis() > displayUntil || lastDropText.isBlank()) return
        if (!enabled) return

        val mc = Minecraft.getMinecraft()
        val sr = ScaledResolution(mc)

        val fontRenderer = mc.fontRendererObj
        val textWidth = fontRenderer.getStringWidth(lastDropText)
        val textHeight = 9  // Default Minecraft font height

        val x = (sr.scaledWidth - textWidth) / 2f
        val y = (sr.scaledHeight - textHeight) / 2f

        fontRenderer.drawStringWithShadow(lastDropText, x, y, 0xFFFFFF)
    }
}
