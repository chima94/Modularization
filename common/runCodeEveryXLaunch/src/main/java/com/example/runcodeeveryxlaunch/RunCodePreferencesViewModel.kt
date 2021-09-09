package com.example.runcodeeveryxlaunch

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



const val LAUNCHES_UNTIL_RUN_NAME = "launches_name"
const val COUNT_PREF_KEY_NAME = "countPreKey"

class RunCodePreferencesViewModel @AssistedInject constructor(
    private val dataStore: DataStore<Preferences>,
    @Assisted(COUNT_PREF_KEY_NAME) private val countPrefKeyName: String,
    @Assisted(LAUNCHES_UNTIL_RUN_NAME)private val LAUNCHES_UNTIL_RUN: Int
) : ViewModel(){


    @AssistedFactory
    interface RunCodePreferencesViewModelFactory{
        fun create(
            @Assisted(COUNT_PREF_KEY_NAME) countPrefKeyName: String,
            @Assisted(LAUNCHES_UNTIL_RUN_NAME) LAUNCHES_UNTIL_RUN: Int
        ): RunCodePreferencesViewModel
    }

    private companion object{
        private const val dateFirstLauncherPref = "dateFirLaunched"
    }

    private val countPrefKey = longPreferencesKey(countPrefKeyName)
    private val dateFirstLaunchKey = longPreferencesKey(dateFirstLauncherPref)

    private val codeToRunChannel = Channel<Boolean>(Channel.BUFFERED)
    val runCode = codeToRunChannel.receiveAsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        runCode()
    }

    private fun runCode(){
        viewModelScope.launch {
            dataStore.edit {

                //increment launch counter
                val launchCount = it[countPrefKey] ?: 0
                it[countPrefKey] = launchCount + 1

                var dateFirstLaunch = it[dateFirstLaunchKey] ?: 0
                if(dateFirstLaunch == 0L){
                    dateFirstLaunch = System.currentTimeMillis()
                    it[dateFirstLaunchKey] = dateFirstLaunch
                }

                //wait at least n days before opening
                if(launchCount >= LAUNCHES_UNTIL_RUN){
                    if(System.currentTimeMillis() >= dateFirstLaunch){
                        codeToRunChannel.send(true)

                        //reset launch count and date of first launch
                        it[countPrefKey] = 0
                        it[dateFirstLaunchKey] = 0
                    }
                }
            }
        }
    }
}