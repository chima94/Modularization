package com.example.searchresultui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.backbuttoncomponent.BackButton
import com.example.color.primaryVariant
import com.example.errorcomponent.ErrorMessage
import com.example.errorcomponent.ErrorWithRetry
import com.example.paging.PagingUiProviderViewModel
import com.example.paging.appendState
import com.example.paging.ui.PagingUIProvider
import com.example.radiobuttoncomponents.RadioButtonWithText
import com.example.radiobuttoncomponents.RadioButtonWithTextNotClickable
import com.example.searchdata.SearchViewModel
import com.example.searchresultdata.SearchResultHandleDataViewModel
import com.example.shape.BottomSheetShapes
import com.example.shape.Shapes
import com.funkymuse.composed.core.lazylist.lastVisibleIndexState
import com.funkymuse.composed.core.rememberBooleanDefaultFalse
import com.funkymuse.composed.core.rememberIntSaveableDefaultZero
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchResultUI(){

    val searchResultViewModel: SearchResultHandleDataViewModel = hiltViewModel()
    val pagingUIUIProvider: PagingUiProviderViewModel = hiltViewModel()

    var checkedSortPosition by rememberIntSaveableDefaultZero()
    var filtersVisible by rememberBooleanDefaultFalse()

    var searchInFieldCheckedPosition by rememberSaveable{ mutableStateOf(searchResultViewModel.searchInFieldsCheckedPosition)}
    var searchWithMaskWord by rememberSaveable { mutableStateOf(searchResultViewModel.searchWithMaskWord)}

    var progressVisibility by rememberBooleanDefaultFalse()

    val pageItems = searchResultViewModel.bookData.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    progressVisibility = pagingUIUIProvider.progressBarVisibility(pageItems)

    filtersVisible = !pagingUIUIProvider.isDataEmptyWithError(pageItems) && !progressVisibility
    pagingUIUIProvider.onPaginationReachedError(pageItems.appendState, com.example.strings.R.string.no_more_books_by_query_to_load)

    val retry = {
        searchResultViewModel.refresh()
        pageItems.refresh()
    }

    ScaffoldWithBackFiltersAndContent(
        checkedSortPosition = checkedSortPosition,
        searchInFieldsCheckedPosition = searchInFieldCheckedPosition,
        searchWithMaskWord = searchWithMaskWord,
        filterVisible = filtersVisible,
        onBackClicked = { searchResultViewModel.navigateUp() },
        onSortPositionClicked = {
            checkedSortPosition = it
            searchResultViewModel.sortByPosition(it)
            pageItems.refresh()
        },
        onSearchInFieldsCheckedPosition ={
            searchInFieldCheckedPosition = it
            searchResultViewModel.searchInFieldsByPosition(it)
            pageItems.refresh()
        },
        onSearchWithMaskWord = {
            searchWithMaskWord = it
            searchResultViewModel.searchWithMaskedWord(it)
            pageItems.refresh()
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            AnimatedVisibility(
                visible = progressVisibility,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .wrapContentSize()
                    .padding(top = 8.dp)
                    .zIndex(2f)
            ){
                CircularProgressIndicator()
            }

            pagingUIUIProvider.OnError(
                scope = scope,
                pagingItems = pageItems,
                noInternetUI = {
                    ErrorMessage(text = com.example.strings.R.string.no_books_loaded_no_connect)
                },
                errorUI = {
                    ErrorWithRetry(text = com.example.strings.R.string.no_books_loaded_search)
                    retry()
                }
            )

            val columnState = rememberLazyListState()
            val lastVisibleIndex by columnState.lastVisibleIndexState()

            AnimatedVisibility(
                visible = lastVisibleIndex != null && lastVisibleIndex ?: 0 > 20,
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(bottom = 22.dp, end = 4.dp)
                    .zIndex(2f)
            ) {

            }
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
private fun backButtonLogic(
    bottomSheetState: BottomSheetState,
    scope: CoroutineScope,
    dropDownMenuExpanded: Boolean,
    onDropDownSetFalse: () -> Unit,
    onBackClicked: ()-> Unit
){
    when{
        bottomSheetState.isExpanded ->{
            scope.launch { bottomSheetState.collapse() }
        }
        dropDownMenuExpanded -> onDropDownSetFalse()
        else -> onBackClicked()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScaffoldWithBackFiltersAndContent(
    checkedSortPosition: Int,
    searchInFieldsCheckedPosition: Int,
    searchWithMaskWord: Boolean,
    filterVisible: Boolean,
    onBackClicked: () -> Unit,
    onSortPositionClicked: (Int) -> Unit,
    onSearchInFieldsCheckedPosition: (Int) -> Unit,
    onSearchWithMaskWord: (Boolean) -> Unit,
    content: @Composable (PaddingValues) -> Unit
){

    val bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val scope = rememberCoroutineScope()
    val searchViewModel = hiltViewModel<SearchViewModel>()

    var dropDownMenuExpanded by remember { mutableStateOf(false)}
    val collapseDropDownMenu = {dropDownMenuExpanded = false}

    val onBack = {
        backButtonLogic(bottomSheetState, scope, dropDownMenuExpanded, collapseDropDownMenu, onBackClicked)
    }

    BackHandler {
        onBack()
    }

    BottomSheetScaffold(
        sheetContent = {
            LazyColumn{
                item {
                    Text(
                        text = stringResource(id = com.example.strings.R.string.search_in_field),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    )
                }
                itemsIndexed(searchViewModel.searchInFieldEntries){ index, item ->
                    RadioButtonWithText(
                        text = item.title,
                        isChecked = searchInFieldsCheckedPosition == index,
                        onRadioButtonClicked = {
                            onSearchInFieldsCheckedPosition(index)
                            scope.launch { bottomSheetState.collapse() }
                        }
                    )
                }
                item {
                    Text(
                        text = stringResource(id = com.example.strings.R.string.mask_word),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    )
                }

                item {
                    RadioButtonWithText(
                        text = com.example.strings.R.string.search_with_mask_word,
                        isChecked = searchWithMaskWord,
                        onRadioButtonClicked = {
                            onSearchWithMaskWord(!searchWithMaskWord)
                        })
                }

                item {
                    Spacer(modifier = Modifier.padding(bottom = 64.dp))

                }
            }
        },
        sheetPeekHeight = 0.dp,
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetShape = BottomSheetShapes.large,
        topBar = {
            TopAppBar(backgroundColor = primaryVariant, modifier = Modifier.statusBarsPadding()) {
                Box(modifier = Modifier.fillMaxSize()){
                    BackButton(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp),
                        onClick = {
                            onBack()
                        }
                    )
                    if(filterVisible){
                        Button(
                            onClick = {
                                dropDownMenuExpanded = !dropDownMenuExpanded
                                scope.launch {
                                    if(!bottomSheetState.isCollapsed){
                                        bottomSheetState.collapse()
                                    }
                                }
                            },
                            shape = Shapes.large,
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ){
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = stringResource(id = com.example.strings.R.string.title_favorites)
                            )
                        }
                        DropdownMenu(
                            expanded = dropDownMenuExpanded,
                            modifier = Modifier.fillMaxWidth(),
                            offset = DpOffset(32.dp, 32.dp),
                            onDismissRequest = collapseDropDownMenu
                        ) {
                            searchViewModel.sortList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                    onSortPositionClicked(it.first)
                                    collapseDropDownMenu()
                                }) {
                                    RadioButtonWithTextNotClickable(
                                        text = it.second,
                                        isChecked = checkedSortPosition == it.first
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ){
        Box {
            if(bottomSheetState.isExpanded){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            scope.launch { bottomSheetState.collapse() }
                        }
                        .background(brush = SolidColor(Color.Black), alpha = 0.5f)
                        .zIndex(0.5f)

                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(if (bottomSheetState.isExpanded) 0.2f else 0f)
            ){
                content(it)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 22.dp)
                    .zIndex(0.3f)
            ){
                if(filterVisible){
                    FloatingActionButton(
                        modifier = Modifier
                            .navigationBarsPadding(),
                        onClick = { scope.launch { bottomSheetState.expand() }}
                    ) {
                        Icon(
                            Icons.Filled.FilterList,
                            contentDescription = stringResource(id = com.example.strings.R.string.filter),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}