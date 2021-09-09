package com.example.settingdata

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.settingdata.contracts.DefaultPrefContracts
import com.example.settingdata.contracts.DefaultPrefContracts.Companion.DARK_THEME_KEY
import com.example.settingdata.contracts.DefaultPrefContracts.Companion.VPN_WARNING_KEY
import com.example.settingdata.contracts.DefaultPrefsStateChangeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultPreferences @Inject constructor(
    private val datastore: DataStore<Preferences>
) : DefaultPrefContracts, DefaultPrefsStateChangeContract{

    private val darkThemeKey = booleanPreferencesKey(DARK_THEME_KEY)
    private val vpnWarningKey = booleanPreferencesKey(VPN_WARNING_KEY)

    //region vpn
    override val vpnWarning: Flow<Boolean> = datastore.data.map {
        it[vpnWarningKey] ?: true
    }
    override suspend fun changeVPNWarning(isVPNWarningEnabled: Boolean) {
        datastore.edit { it[vpnWarningKey] = isVPNWarningEnabled }
    }


    //region darktheme
    override val darkTheme = datastore.data.map { it[darkThemeKey] ?: false }

    override suspend fun changeTheme(isDarkThemeEnabled: Boolean) {
        datastore.edit { it[darkThemeKey] = isDarkThemeEnabled }
    }
}