package com.example.composeextensions

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

@PublishedApi
internal inline fun <reified T : ViewModel> createAssistedViewModel(
    arguments: Bundle? = null,
    owner: SavedStateRegistryOwner,
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = object : AbstractSavedStateViewModelFactory(owner, arguments){
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) =
        viewModelProducer(handle) as T

}


@Composable
inline fun <reified T: ViewModel> assistedViewModel(
    arguments: Bundle? = null,
    crossinline viewModelProducer: (SavedStateHandle) -> T
): T =
    viewModel(factory = createAssistedViewModel(
        arguments = arguments,
        owner = LocalSavedStateRegistryOwner.current
    ){
        viewModelProducer(it)
    })



@Composable
inline fun <reified T : ViewModel> assistedInjectable(crossinline produce: AssistedHiltInjectables.(handle : SavedStateHandle) -> T): T =
    assistedViewModel(viewModelProducer = {
        (LocalContext.current as AssistedHiltInjectables).produce(it)
    })