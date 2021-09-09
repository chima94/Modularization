package com.example.settingdata.contracts

import kotlinx.coroutines.flow.Flow

interface DefaultPrefContracts {
    val darkTheme: Flow<Boolean>
    val vpnWarning: Flow<Boolean>

    companion object{
        const val DARK_THEME_KEY = "dark_theme"
        const val VPN_WARNING_KEY = "vpn_warning"
    }
}