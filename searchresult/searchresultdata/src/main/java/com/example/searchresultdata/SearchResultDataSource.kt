package com.example.searchresultdata

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookmodel.Book
import com.example.dispatchers.IoDispatcher
import com.example.serverconstants.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher

class SearchResultDataSource @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted(COLUM_QUERY) private val searchQuery: String,
    @Assisted(FIELDS_QUERY_CONST)private val searchInFieldPosition : Int,
    @Assisted(SORT_QUERY) private val sortQuery: String,
    @Assisted(SEARCH_WITH_MASK) private val maskWord: Boolean,
    @Assisted(SORT_TYPE) private val sortType : String,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PagingSource<Int, Book>(){

    @AssistedFactory
    interface SearchResultDataSourceFactory {
        fun create(
            @Assisted(COLUM_QUERY) searchQuery: String,
            @Assisted(FIELDS_QUERY_CONST) searchInFieldsPosition: Int,
            @Assisted(SORT_QUERY) sortQuery: String,
            @Assisted(SEARCH_WITH_MASK) maskWord: Boolean,
            @Assisted(SORT_TYPE) sortType: String
        ): SearchResultDataSource
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        TODO("Not yet implemented")
    }

}