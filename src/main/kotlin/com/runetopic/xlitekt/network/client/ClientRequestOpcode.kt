package com.runetopic.xlitekt.network.client

object ClientRequestOpcode {
    const val HIGH_PRIORITY_OPCODE = 0
    const val LOW_PRIORITY_OPCODE = 1
    const val CONNECTION_LOGGED_IN_OPCODE = 2
    const val CONNECTION_LOGGED_OUT_OPCODE = 3
    const val ENCRYPTION_OPCODE = 4
    const val HANDSHAKE_LOGIN_OPCODE = 14
    const val HANDSHAKE_JS5_OPCODE = 15
    const val LOGIN_REQUEST_OPCODE = 16
}
