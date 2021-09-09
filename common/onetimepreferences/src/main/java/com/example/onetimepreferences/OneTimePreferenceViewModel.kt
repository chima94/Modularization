package com.example.onetimepreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


private const val SETTINGS_PREF_NAME = "one_time_pref_name"

class OneTimePreferenceViewModel @AssistedInject constructor(
    @Assisted(SETTINGS_PREF_NAME) private val name: String,
    private val dataStore: DataStore<Preferences>
): ViewModel(){

    @AssistedFactory
    interface OneTimePreferenceViewModelFactory{
        fun create(@Assisted(SETTINGS_PREF_NAME) name: String): OneTimePreferenceViewModel
    }

    private val booleanKey = booleanPreferencesKey(name)

    val isEventFired: StateFlow<Boolean> =
        dataStore.data.map { it[booleanKey] ?: false }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)



    fun setEventIsFired(){
        setEventCase(true)
    }


    fun setEventIsNotFired(){
        setEventCase(false)
    }

    private fun setEventCase(condition : Boolean){
        viewModelScope.launch {
            dataStore.edit {
                it[booleanKey] = condition
            }
        }
    }
}