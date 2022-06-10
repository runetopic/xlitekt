package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand

/**
 * @author Justin Kenney
 */

private val coordinatesDescription = "Displays the Player's x-coordinate, z-coordinate, and level."
onCommand("coords", "coordinates", description = coordinatesDescription).use {

    // TODO check if player is null or other conditions? Command levels?

    this.message { "x: ${location.x}, z: ${location.z}, level: ${location.level}" }
}

private val locationDescription = "Displays the Player's Location information."
onCommand("location").use {
    // TODO check if player is null or other conditions? Command levels?

    // TODO format message
    message { location.toString() }
}
