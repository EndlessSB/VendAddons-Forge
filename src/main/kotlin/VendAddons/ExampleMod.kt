package VendAddons

import VendAddons.commands.FeatureToggleCommand
import VendAddons.commands.SkillsCommand
import VendAddons.Features.Fishing.FishControls
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import VendAddons.commands.*
import VendAddons.utils.TextUtils
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard
import VendAddons.config.config
import VendAddons.Features.Slayers.SlayerOverlay
import VendAddons.commands.StatsCommand
import VendAddons.Features.Fishing.FishingXPOverlay
import VendAddons.utils.ChatListeners
import VendAddons.Features.Misc.RareDropsOverlay
import VendAddons.Graphics.ConfigGuiScreen
import VendAddons.commands.ConfigMenuCommand

@Mod(modid = "VendAddons", useMetadata = true)
class ExampleMod {


    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        try {
            val resource: net.minecraft.client.resources.IResource = Minecraft.getMinecraft().getResourceManager()
                .getResource(net.minecraft.util.ResourceLocation("test:test.txt"))
            org.apache.commons.io.IOUtils.copy(resource.getInputStream(), java.lang.System.out)
        } catch (e: java.io.IOException) {
            throw java.lang.RuntimeException(e)
        }
    }

    @Mod.EventHandler
    fun preinit(event: FMLPreInitializationEvent) { // This will occur on preinit
        // Initialize Config
        config.init()

        // Register Keybind
        FarmControlsKeybind = KeyBinding("key.vendaddons.FarmControls", Keyboard.KEY_L, "key.categories.misc")
        ClientRegistry.registerKeyBinding(FarmControlsKeybind)

        // Register ClientCommandHandler easily
        val cch = ClientCommandHandler.instance

        // Register Feature Toggle Command
        cch.registerCommand(FeatureToggleCommand())

        // Register the help command
        cch.registerCommand(HelpCommand())
        // Register Fish Controls
        cch.registerCommand(FishControlsCommand())
        // API COMMANDS
        cch.registerCommand(SkillsCommand())

        cch.registerCommand(SlayerOverlayCommand())

        cch.registerCommand(StatsCommand())

        cch.registerCommand(ConfigMenuCommand())
    }



    @Mod.EventHandler // More stuff I used from dulkir [I am learning kotlin still so it's been a bit of a guide to me]
    fun onInit(event: FMLInitializationEvent) {
        // REGISTER Classes and such HERE
        val mcBus = MinecraftForge.EVENT_BUS
        mcBus.register(this)

        mcBus.register(TextUtils)

        mcBus.register(FishControls)

        mcBus.register(SlayerOverlay)

        mcBus.register(ChatListeners)

        mcBus.register(FishingXPOverlay)

        mcBus.register(RareDropsOverlay)

    }

    companion object {
        val mc: Minecraft = Minecraft.getMinecraft()

        lateinit var FarmControlsKeybind: KeyBinding

        var prefix = "§6§l[Vend] "
    }
}
