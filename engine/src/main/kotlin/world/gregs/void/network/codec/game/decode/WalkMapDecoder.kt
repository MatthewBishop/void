package world.gregs.void.network.codec.game.decode

import io.netty.channel.ChannelHandlerContext
import world.gregs.void.buffer.Endian
import world.gregs.void.buffer.Modifier
import world.gregs.void.buffer.read.Reader
import world.gregs.void.network.codec.Decoder

class WalkMapDecoder : Decoder(5) {

    override fun decode(context: ChannelHandlerContext, packet: Reader) {
        handler?.walk(
            context = context,
            x = packet.readShort(Modifier.ADD, Endian.LITTLE),
            y = packet.readShort(Modifier.ADD, Endian.LITTLE),
            running = packet.readBoolean()
        )
    }

}