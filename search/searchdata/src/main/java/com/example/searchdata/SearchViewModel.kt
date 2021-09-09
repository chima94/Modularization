package com.example.searchdata

import android.app.Application
import android.util.Pair
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(application: Application): AndroidViewModel(application){

    val searchInFieldEntries
        get() = listOf(
            RadioButtonEntries(com.example.strings.R.string.default_column),
            RadioButtonEntries(com.example.strings.R.string.title),
            RadioButtonEntries(com.example.strings.R.string.author),
            RadioButtonEntries(com.example.strings.R.string.series),
            RadioButtonEntries(com.example.strings.R.string.publisher),
            RadioButtonEntries(com.example.strings.R.string.year),
            RadioButtonEntries(com.example.strings.R.string.isbn),
            RadioButtonEntries(com.example.strings.R.string.language),
            RadioButtonEntries(com.example.strings.R.string.md5),
            RadioButtonEntries(com.example.strings.R.string.tags),
            RadioButtonEntries(com.example.strings.R.string.extension)
        )

    val sortList
        get() = listOf(
           Pair(0, com.example.strings.R.string.default_sort),
            Pair(1, com.example.strings.R.string.year_asc),
            Pair(2, com.example.strings.R.string.year_desc),
            Pair(3, com.example.strings.R.string.size_asc),
            Pair(4, com.example.strings.R.string.size_desc),
            Pair(5, com.example.strings.R.string.author_asc),
            Pair(6, com.example.strings.R.string.author_desc),
            Pair(7, com.example.strings.R.string.title_asc),
            Pair(8, com.example.strings.R.string.title_desc),
            Pair(9, com.example.strings.R.string.extension_asc),
            Pair(10, com.example.strings.R.string.extension_desc),
            Pair(11, com.example.strings.R.string.publisher_asc),
            Pair(12, com.example.strings.R.string.publisher_desc)
        )
}