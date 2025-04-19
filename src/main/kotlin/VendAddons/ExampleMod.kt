package VendAddons

import SkillsCommand
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraft.client.renderer.GlStateManager
import VendAddons.commands.*
import VendAddons.utils.TextUtils
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge

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

        println("Dirt: ${Blocks.dirt.unlocalizedName}")
        // Below is a demonstration of an access-transformed class access.
        println("Color State: " + GlStateManager.Color());
    }



    @Mod.EventHandler
    fun preinnit(event: FMLPreInitializationEvent) { // This will occur on preinit
        val cch = ClientCommandHandler.instance
        // Register the help command
        cch.registerCommand(HelpCommand())
        // Register Fish Controls
        cch.registerCommand(FishCommand())
        // API COMMANDS
        cch.registerCommand(SkillsCommand())
    }

    @Mod.EventHandler // More stuff I used from dulkir [I am learning kotlin still so it's been a bit of a guide to me]
    fun onInit(event: FMLInitializationEvent) {
        // REGISTER Classes and such HERE
        val mcBus = MinecraftForge.EVENT_BUS
        mcBus.register(this)

        mcBus.register(TextUtils)
    }

    companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
    }
}
