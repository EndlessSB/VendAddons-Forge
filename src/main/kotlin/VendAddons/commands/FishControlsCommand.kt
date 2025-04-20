package VendAddons.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import VendAddons.Features.Fishing.FishControls

class FishControlsCommand : ClientCommandBase("fishcontrols") {

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        FishControls.toggle()
    }
}
