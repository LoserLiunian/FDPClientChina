/*
 * FDPClientChina Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClientChina/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.other

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minecraft.network.play.server.S12PacketEntityVelocity

class HypixelVelocity : VelocityMode("Hypixel") {

    private var offGroundTicks = 0

    override fun onVelocityPacket(event: PacketEvent) {
        val packet = event.packet
        if(packet is S12PacketEntityVelocity) {
            event.cancelEvent()
            if (offGroundTicks < 5) mc.thePlayer.motionY = packet.getMotionY().toDouble() / 8000.0
        }
    }

    override fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.onGround) {
            offGroundTicks = 0
        } else {
            offGroundTicks ++
        }
    }
}
