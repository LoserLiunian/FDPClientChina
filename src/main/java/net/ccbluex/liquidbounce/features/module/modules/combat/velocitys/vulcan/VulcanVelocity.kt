/*
 * FDPClientChina Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClientChina/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.vulcan

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minecraft.network.play.client.C0FPacketConfirmTransaction

class VulcanVelocity : VelocityMode("Vulcan") {
    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is C0FPacketConfirmTransaction) {
            val transUID = (packet.uid).toInt()
            if (transUID >= -31767 && transUID <= -30769) {
                event.cancelEvent()
            }
        }
    }
    
    override fun onVelocityPacket(event: PacketEvent) {
        event.cancelEvent()
    }
}
