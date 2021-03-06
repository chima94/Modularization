package com.example.paging.data

import androidx.paging.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@ViewModelScoped
class PagingDataProvider @Inject constructor(){


    val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = true)

    inline fun <T : Any> providePagingData(
        viewModelScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        crossinline function: () -> PagingSource<Int, T>
    ): Flow<PagingData<T>> = Pager(pagingConfig) { function() }.flow.flowOn(dispatcher).cachedIn(viewModelScope)
}