package world.gregs.voidps.engine.client.instruction.handle

import world.gregs.voidps.engine.client.instruction.InstructionHandler
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.chat.ignore.AddIgnore
import world.gregs.voidps.network.client.instruction.IgnoreAdd

class IgnoreAddHandler : InstructionHandler<IgnoreAdd>() {

    override fun validate(player: Player, instruction: IgnoreAdd) {
        player.emit(AddIgnore(instruction.name))
    }

}