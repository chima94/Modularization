package com.example.settingdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settingdata.contracts.DefaultPrefContracts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val defaultPreferences: DefaultPreferences
): ViewModel(), DefaultPrefContracts{

    override val darkTheme = produceState(defaultPreferences.darkTheme)
    override val vpnWarning = produceState(defaultPreferences.vpnWarning)


    private fun produceState(flow: Flow<Boolean>) =
        flow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            false
        )


    fun changeTheme(isDarkThemeEnabled: Boolean){
        viewModelScope.launch { defaultPreferences.changeTheme(isDarkThemeEnabled) }
    }

    fun changeVPNWarning(isVPNWarningEnabled: Boolean){
        viewModelScope.launch { defaultPreferences.changeVPNWarning(isVPNWarningEnabled) }
    }
}