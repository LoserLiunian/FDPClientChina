/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMinecraft/FDPClientChina/
 */
package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.FDPClientChina
import net.ccbluex.liquidbounce.features.module.modules.client.Target.animalValue
import net.ccbluex.liquidbounce.features.module.modules.client.Target.deadValue
import net.ccbluex.liquidbounce.features.module.modules.client.Target.invisibleValue
import net.ccbluex.liquidbounce.features.module.modules.client.Target.mobValue
import net.ccbluex.liquidbounce.features.module.modules.client.Target.playerValue
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot.isBot
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams
import net.ccbluex.liquidbounce.utils.extensions.toRadiansD
import net.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.entity.monster.EntityGolem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.Vec3
import kotlin.math.cos
import kotlin.math.sin

object EntityUtils : MinecraftInstance() {
    fun isSelected(entity: Entity, canAttackCheck: Boolean): Boolean {
        if (entity is EntityLivingBase && (deadValue.get() || entity.isEntityAlive()) && entity !== mc.thePlayer) {
            if (invisibleValue.get() || !entity.isInvisible()) {
                if (playerValue.get() && entity is EntityPlayer) {
                    if (canAttackCheck) {
                        if (isBot(entity)) {
                            return false
                        }

                        if (isFriend(entity)) {
                            return false
                        }

                        if (entity.isSpectator) {
                            return false
                        }

                        if (entity.isPlayerSleeping) {
                            return false
                        }

                        if (!FDPClientChina.combatManager.isFocusEntity(entity)) {
                            return false
                        }

                        val teams = FDPClientChina.moduleManager.getModule(Teams::class.java)
                        return !teams!!.state || !teams.isInYourTeam(entity)
                    }

                    return true
                }
                return mobValue.get() && isMob(entity) || animalValue.get() && isAnimal(entity)
            }
        }
        return false
    }

    fun canRayCast(entity: Entity): Boolean {
        if (entity is EntityLivingBase) {
            if (entity is EntityPlayer) {
                val teams = FDPClientChina.moduleManager.getModule(Teams::class.java)
                return !teams!!.state || !teams.isInYourTeam(entity)
            } else {
                return mobValue.get() && isMob(entity) || animalValue.get() && isAnimal(entity)
            }
        }
        return false
    }
    fun isFriend(entity: Entity): Boolean {
        return entity is EntityPlayer && entity.getName() != null && FDPClientChina.fileManager.friendsConfig.isFriend(stripColor(entity.getName()))
    }

    fun isFriend(entity: String): Boolean {
        return FDPClientChina.fileManager.friendsConfig.isFriend(entity)
    }

    fun isAnimal(entity: Entity): Boolean {
        return entity is EntityAnimal || entity is EntitySquid || entity is EntityGolem || entity is EntityVillager || entity is EntityBat
    }

    fun isMob(entity: Entity): Boolean {
        return entity is EntityMob || entity is EntitySlime || entity is EntityGhast || entity is EntityDragon
    }

    fun isLookingOnEntities(entity: Entity, maxAngleDifference: Double): Boolean {
        val player = mc.thePlayer ?: return false
        val playerRotation = player.rotationYawHead
        val playerPitch = player.rotationPitch

        val maxAngleDifferenceRadians = Math.toRadians(maxAngleDifference)

        val lookVec = Vec3(
            -sin(playerRotation.toRadiansD()),
            -sin(playerPitch.toRadiansD()),
            cos(playerRotation.toRadiansD())
        ).normalize()

        val playerPos = player.positionVector.addVector(0.0, player.eyeHeight.toDouble(), 0.0)
        val entityPos = entity.positionVector.addVector(0.0, entity.eyeHeight.toDouble(), 0.0)

        val directionToEntity = entityPos.subtract(playerPos).normalize()
        val dotProductThreshold = lookVec.dotProduct(directionToEntity)

        return dotProductThreshold > cos(maxAngleDifferenceRadians)
    }
}