/*
 * FDPClientChina Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClientChina/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.vanilla

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.server.S12PacketEntityVelocity

class CancelVelocity : VelocityMode("Cancel") {
    val cancelHorizontal = BoolValue("CancelHorizontalVelocity", true)
    val cancelVertical = BoolValue("CancelVerticalVelocity", true)
    
    override fun onVelocityPacket(event: PacketEvent) {
        event.cancelEvent()
        val packet = event.packet
        if (packet is S12PacketEntityVelocity) {
            if (!cancelVertical.get()) mc.thePlayer.motionY = packet.getMotionY().toDouble() / 8000.0
            if (!cancelHorizontal.get()) {
                mc.thePlayer.motionX = packet.getMotionX().toDouble() / 8000.0
                mc.thePlayer.motionZ = packet.getMotionZ().toDouble() / 8000.0
            }
        }
    }
}
