package com.example.settingdata.contracts

interface DefaultPrefsStateChangeContract {
    suspend fun changeTheme(isDarkThemeEnabled: Boolean)
    suspend fun changeVPNWarning(isVPNWarningEnabled: Boolean)
}