package VendAddons.Graphics

import VendAddons.config.config
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import org.lwjgl.opengl.GL11

class ConfigGuiScreen : GuiScreen() {
    private val buttons = mutableListOf<GuiButton>()
    private val buttonStates = mutableListOf<Boolean>() // To store the state of checkboxes

    override fun initGui() {
        // Clear existing buttons
        buttonList.clear()

        // Set up background color
        this.drawDefaultBackground()

        // Add buttons for each config option (simulating checkboxes)
        val settings = config.getDefaultSettings() // This will now work as expected
        settings.entries.forEachIndexed { index, entry ->
            val settingName = entry.key
            val defaultValue = entry.value
            val button = GuiButton(index, width / 2 - 100, 50 + (index * 35), I18n.format("vendaddons.config.$settingName"))
            buttonList.add(button)

            // Store the state (default or loaded)
            buttonStates.add(config.check.getOrDefault(settingName, defaultValue))
        }

        // Add a "Done" button styled in a more "cheat menu" style
        buttonList.add(GuiButton(100, width / 2 - 100, height - 50, I18n.format("gui.done")))
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.id == 100) {
            // Save settings when "Done" button is pressed
            buttonStates.forEachIndexed { index, state ->
                val option = config.getDefaultSettings().keys.toList()[index]
                if (state != config.check.getOrDefault(option, false)) {
                    config.update.set(option, state)  // Ensure proper type matching
                }
            }
            // Return to the previous screen
            mc.displayGuiScreen(null)
        } else {
            // Toggle the checkbox state when a config button is clicked
            val buttonIndex = button.id
            val currentState = buttonStates[buttonIndex]
            buttonStates[buttonIndex] = !currentState  // Toggle the state

            // Update the button text based on the new state
            val newLabel = if (buttonStates[buttonIndex]) {
                I18n.format("vendaddons.config.enabled")
            } else {
                I18n.format("vendaddons.config.disabled")
            }
            button.displayString = newLabel
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        // Set a dark background with some transparency
        GL11.glPushMatrix()
        drawRect(0, 0, width, height, 0x80000000.toInt()) // Semi-transparent black background

        // Draw title
        drawCenteredString(fontRendererObj, I18n.format("vendaddons.config.title"), width / 2, 20, 0xFFFFFF)

        super.drawScreen(mouseX, mouseY, partialTicks)

        // Draw button borders (optional)
        buttonList.forEach { button ->
            if (button is GuiButton) {
                drawBorderedRect(button.xPosition - 2, button.yPosition - 2, button.xPosition + button.width + 2, button.yPosition + button.height + 2, 1, 0x50000000, 0xFFFFFF)
            }
        }

        GL11.glPopMatrix()
    }

    private fun drawBorderedRect(x: Int, y: Int, x2: Int, y2: Int, borderWidth: Int, borderColor: Int, fillColor: Int) {
        drawRect(x, y, x2, y2, fillColor)
        drawRect(x, y, x + borderWidth, y2, borderColor)
        drawRect(x, y, x2, y + borderWidth, borderColor)
        drawRect(x2 - borderWidth, y, x2, y2, borderColor)
        drawRect(x, y2 - borderWidth, x2, y2, borderColor)
    }
}
