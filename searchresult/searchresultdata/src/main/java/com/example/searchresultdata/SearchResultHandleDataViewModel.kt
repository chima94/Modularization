package com.example.searchresultdata

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dispatchers.IoDispatcher
import com.example.navigator.Navigator
import com.example.paging.data.PagingDataProvider
import com.example.paging.data.PagingDataSourceHandle
import com.example.paging.stateHandleArgument
import com.example.paging.stateHandleDelegate
import com.example.searchresultdestination.SearchResultDestination.SEARCH_IN_FIELDS_PARAM
import com.example.searchresultdestination.SearchResultDestination.SEARCH_PARAM
import com.example.searchresultdestination.SearchResultDestination.SEARCH_WITH_MASK_WORD_PARAM
import com.example.serverconstants.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class SearchResultHandleDataViewModel @Inject constructor(
    application: Application,
    override val savedStateHandle: SavedStateHandle,
    dataProvider: PagingDataProvider,
    private val navigator: Navigator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val searchResultDataSourceFactory: SearchResultDataSource.SearchResultDataSourceFactory
): AndroidViewModel(application), PagingDataSourceHandle, Navigator by navigator{

    private companion object{
        private const val SEARCH_IN_FIELDS_CHECKED_POSITION_KEY = "searchInFieldsCheckedPosition"
        private const val SEARCH_WITH_MASKED_WORD_KEY = "searchWithMaskWord"
        private const val SORT_QUERY_KEY = "sortQuery"
        private const val SORT_TYPE_KEY = "sortType"
    }

    private val searchResultDataSource
        get() = searchResultDataSourceFactory.create(
            searchQuery ?: "",
            searchInFieldsPosition ?: searchInFieldsCheckedPosition,
            sortQuery ?: "",
            maskWord ?: searchWithMaskWord,
            sortType ?: ""
        )

    val bookData = dataProvider.providePagingData(viewModelScope, ioDispatcher){searchResultDataSource}

    private val searchQuery: String? by stateHandleDelegate(SEARCH_PARAM)
    val searchInFieldsCheckedPosition by stateHandleArgument(SEARCH_IN_FIELDS_PARAM, 0)
    val searchWithMaskWord by stateHandleArgument(SEARCH_WITH_MASK_WORD_PARAM, false)

    private var searchInFieldsPosition by stateHandleDelegate<Int>(
        SEARCH_IN_FIELDS_CHECKED_POSITION_KEY
    )

    private var maskWord by stateHandleDelegate<Boolean>(SEARCH_WITH_MASKED_WORD_KEY)

    private var sortType by stateHandleDelegate<String>(SORT_TYPE_KEY)

    private var sortQuery by stateHandleDelegate<String>(SORT_QUERY_KEY)

    private fun resetOnSort() {
        sortQuery = ""
        sortType = ""
    }

    private fun sortByYearDESC() {
        resetOnSort()
        sortQuery = SORT_YEAR_CONST
        sortType = SORT_TYPE_DESC
    }

    private fun sortByYearASC() {
        resetOnSort()
        sortQuery = SORT_YEAR_CONST
        sortType = SORT_TYPE_ASC
    }

    private fun sortByDefault() {
        resetOnSort()
    }

    private fun sortBySizeDESC() {
        resetOnSort()
        sortQuery = SORT_SIZE
        sortType = SORT_TYPE_DESC
    }

    private fun sortBySizeASC() {
        resetOnSort()
        sortQuery = SORT_SIZE
        sortType = SORT_TYPE_ASC
    }

    fun refresh() {
        resetOnSort()
    }


    fun sortByPosition(position: Int) {
        when (position) {
            0 -> sortByDefault()
            1 -> sortByYearASC()
            2 -> sortByYearDESC()
            3 -> sortBySizeASC()
            4 -> sortBySizeDESC()
            5 -> sortByAuthorASC()
            6 -> sortByAuthorDESC()
            7 -> sortByTitleASC()
            8 -> sortByTitleDESC()
            9 -> sortByExtensionASC()
            10 -> sortByExtensionDESC()
            11 -> sortByPublisherASC()
            12 -> sortByPublisherDESC()
        }
    }

    private fun sortByPublisherASC() {
        resetOnSort()
        sortQuery = SORT_PUBLISHER
        sortType = SORT_TYPE_ASC
    }

    private fun sortByPublisherDESC() {
        resetOnSort()
        sortQuery = SORT_PUBLISHER
        sortType = SORT_TYPE_DESC
    }

    private fun sortByExtensionASC() {
        resetOnSort()
        sortQuery = SORT_EXTENSION
        sortType = SORT_TYPE_ASC
    }

    private fun sortByExtensionDESC() {
        resetOnSort()
        sortQuery = SORT_EXTENSION
        sortType = SORT_TYPE_DESC
    }

    private fun sortByTitleASC() {
        resetOnSort()
        sortQuery = SORT_TITLE
        sortType = SORT_TYPE_ASC
    }

    private fun sortByTitleDESC() {
        resetOnSort()
        sortQuery = SORT_TITLE
        sortType = SORT_TYPE_DESC
    }

    private fun sortByAuthorDESC() {
        resetOnSort()
        sortQuery = SORT_AUTHOR
        sortType = SORT_TYPE_DESC
    }


    private fun sortByAuthorASC() {
        resetOnSort()
        sortQuery = SORT_AUTHOR
        sortType = SORT_TYPE_ASC
    }

    fun searchWithMaskedWord(maskedWord: Boolean) {
        resetOnSort()
        maskWord = maskedWord
    }

    fun searchInFieldsByPosition(position: Int) {
        resetOnSort()
        searchInFieldsPosition = position
    }

}