package VendAddons.Features.Fishing

import VendAddons.ExampleMod
import VendAddons.utils.TextUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

object FishControls {

    private var fishControls: Boolean = false

    private var originalUseItemKey: Int = -1
    private var originalJumpKey: Int = -1
    private var originalSensitivity: Float = Minecraft.getMinecraft().gameSettings.mouseSensitivity

    fun toggle() {
        val mc = Minecraft.getMinecraft()
        val settings = mc.gameSettings

        val useItemKey = settings.keyBindUseItem  // usually right-click
        val jumpKey = settings.keyBindJump        // usually spacebar

        if (!fishControls) {
            // Backup current state
            originalUseItemKey = useItemKey.keyCode
            originalJumpKey = jumpKey.keyCode
            originalSensitivity = settings.mouseSensitivity

            // Assign useItem to SPACE, and jump to RIGHT CLICK
            useItemKey.keyCode = Keyboard.KEY_SPACE
            jumpKey.keyCode = -100 + Mouse.getButtonIndex("BUTTON1") // Right click = -99

            KeyBinding.resetKeyBindingArrayAndHash()
            settings.mouseSensitivity = 0.0f
            fishControls = true

            TextUtils.info("§6§l [Vend] FishControls Enabled", false)
        } else {
            // Restore previous state
            useItemKey.keyCode = originalUseItemKey
            jumpKey.keyCode = originalJumpKey

            KeyBinding.resetKeyBindingArrayAndHash()
            settings.mouseSensitivity = originalSensitivity
            fishControls = false

            TextUtils.info("§6§l [Vend] FishControls Disabled", false)
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (ExampleMod.Companion.FarmControlsKeybind.isPressed) {
            toggle()
        }
    }
}