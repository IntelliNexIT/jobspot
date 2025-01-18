package com.intellinex.jobspot.utils.states

data class MessageState(
    val isEnteringToken: Boolean = true,
    val remoteToken: String = "",
    val messageText: String = ""
)
