package com.runetopic.xlitekt.game

import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.util.resource.loadAllMapSquares
import org.koin.dsl.module

val gameModule = module {
    single { loadAllMapSquares() }
    single { World() }
    single { Game202() }
}

class Game202() : Game

interface Game
