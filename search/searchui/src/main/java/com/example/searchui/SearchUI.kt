package com.example.searchui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navigator.NavigatorViewModel
import com.example.radiobuttoncomponents.RadioButtonWithText
import com.example.searchdata.SearchViewModel
import com.example.searchresultdestination.SearchResultDestination
import com.example.shape.BottomSheetShapes
import com.example.toaster.ToasterViewModel
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch
import java.util.Locale.filter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchUI(){

    val navigatorViewModel: NavigatorViewModel = hiltViewModel()
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    var searchInFieldsCheckedPosition by rememberSaveable { mutableStateOf(0)}
    var searchWithMaskWord by rememberSaveable{ mutableStateOf(false)}
    val viewModel = hiltViewModel<SearchViewModel>()

    val zIndex = if(state.targetValue == ModalBottomSheetValue.Hidden){
        0f
    }else{
        2f
    }

    ModalBottomSheetLayout(
        modifier = Modifier
            .navigationBarsPadding()
            .zIndex(zIndex),
        sheetState = state,
        sheetShape = BottomSheetShapes.large,
        sheetContent = {
            LazyColumn{
                item{
                    Text(
                        text = stringResource(com.example.strings.R.string.search_in_field),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                        color = Color.White
                    )
                }

                itemsIndexed(viewModel.searchInFieldEntries){index, item ->
                    RadioButtonWithText(
                        text = item.title,
                        isChecked = searchInFieldsCheckedPosition == index,
                        onRadioButtonClicked = {
                            searchInFieldsCheckedPosition = index
                        }
                    )
                }

                item {
                    Text(stringResource(id = com.example.strings.R.string.mask_word), modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                        color = Color.White
                    )
                }

                item{
                    RadioButtonWithText(
                        text = com.example.strings.R.string.search_with_mask_word,
                        isChecked = searchWithMaskWord,
                        onRadioButtonClicked = {
                            searchWithMaskWord = !searchWithMaskWord
                        }
                    )
                }

                item { 
                    Spacer(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(bottom = 44.dp)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)) {

                SearchInput(){inputText ->
                    navigatorViewModel.navigate(SearchResultDestination.createSearchRoute(
                        inputText.trim(),
                        searchInFieldsCheckedPosition,
                        searchWithMaskWord
                    ))
                }
                SearchInputExplained()
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 74.dp)
            ){
                FloatingActionButton(
                    onClick = { scope.launch { state.show() }}
                ) {
                    Icon(
                        Icons.Filled.FilterList,
                        contentDescription = stringResource(com.example.strings.R.string.filter),
                        tint = Color.White
                    )
                }
            }
        }
    }
}



@Composable
fun SearchInputExplained(){
    Text(
        text = stringResource(com.example.strings.R.string.search_text),
        fontSize = 12.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp, end = 24.dp)
            .animateContentSize()
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchInput(
    onInputText: (inputText: String) -> Unit = {}
){
    val viewModel = hiltViewModel<ToasterViewModel>()
    val keyboardController = LocalSoftwareKeyboardController.current
    var inputText by rememberSaveable{ mutableStateOf("")}
    val invalidInput = inputText.isBlank() || inputText.length < 3
    
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        isError = invalidInput,
        label = { Text(text = stringResource(com.example.strings.R.string.search))},
        value = inputText ,
        onValueChange = {inputText = it},
        keyboardOptions = KeyboardOptions(
            KeyboardCapitalization.Words, autoCorrect = false,
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if(invalidInput){
                viewModel.shortToast(com.example.strings.R.string.empty_or_short_input)
                return@KeyboardActions
            }
            keyboardController?.hide()
            onInputText(inputText)
        })
    )
}