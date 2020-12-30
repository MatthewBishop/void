package rs.dusk.network.codec.game.decode

import io.netty.channel.ChannelHandlerContext
import rs.dusk.buffer.read.Reader
import rs.dusk.network.codec.Decoder

class CutsceneActionDecoder : Decoder(0) {

    override fun decode(context: ChannelHandlerContext, packet: Reader) {
        handler?.cutsceneAction(context)
    }

}