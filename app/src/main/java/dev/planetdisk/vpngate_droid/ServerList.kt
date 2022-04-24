package dev.planetdisk.vpngate_droid

data class ServerList (
    val hostName: String,
    val ipAddr: String,
    val ping: String,
    val country: String,
    val operator: String,
    val openVpnConfig: String
)