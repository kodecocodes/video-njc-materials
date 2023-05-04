/*
* Copyright (c) 2023 Razeware LLC
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
* distribute, sublicense, create a derivative work, and/or sell copies of the
* Software in any work that is designed, intended, or marketed for pedagogical or
* instructional purposes related to programming, coding, application development,
* or information technology.  Permission for such use, copying, modification,
* merger, publication, distribution, sublicensing, creation of derivative works,
* or sale is expressly withheld.
*
* This project and source code may use libraries or frameworks that are
* released under various Open-Source licenses. Use of those libraries and
* frameworks are governed by their own individual licenses.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package com.kodeco.android.dogbreedsapp.presentation.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kodeco.android.dogbreedsapp.presentation.models.BreedAction
import com.kodeco.android.dogbreedsapp.presentation.models.BreedsUiState
import com.kodeco.android.dogbreedsapp.presentation.view.components.BreedsListComponent
import com.kodeco.android.dogbreedsapp.presentation.view.components.ErrorComponent
import com.kodeco.android.dogbreedsapp.presentation.viewModel.BreedsViewModel
import com.kodeco.android.dogbreedsapp.presentation.viewModel.DislikedBreedsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DislikedBreedsScreen(
  dislikedBreedsViewModel: DislikedBreedsViewModel = koinViewModel(),
      navigateToDetailScreen: (Int) -> Unit
) {

  LaunchedEffect(key1 = true) {
    dislikedBreedsViewModel.uiActions.send(BreedAction.GetBreeds)
  }

  LaunchedEffect(key1 = true) {
    dislikedBreedsViewModel.handleUiEvents()
  }
  val uiState = dislikedBreedsViewModel.dislikeBreedsUiState.collectAsStateWithLifecycle().value
  val scope = rememberCoroutineScope()

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag("toolbar"),
        title = {
          Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Disliked Breeds",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace
          )
        })
    },
  ) { innerPadding ->
    when (uiState) {
      is BreedsUiState.Loading -> {
        LinearProgressIndicator()
      }

      is BreedsUiState.Error -> {}
      is BreedsUiState.Data -> {
        BreedsListComponent(
          breeds = uiState.breeds, modifier = Modifier.padding(innerPadding),
          onBreedDisliked = { breed ->

            val updatedBreed = breed.copy(isDisliked = true, isLiked = false)
            scope.launch {
              dislikedBreedsViewModel.uiActions.send(BreedAction.DislikeBreed(updatedBreed = updatedBreed))
            }
          },
          onBreedLiked = { breed ->
            val updatedBreed = breed.copy(isLiked = true, isDisliked = false)
            scope.launch {
              dislikedBreedsViewModel.uiActions.send(BreedAction.LikeBreed(updatedBreed = updatedBreed))
            }
          },
          navigateToDetailScreen = navigateToDetailScreen
        )
      }

      is BreedsUiState.Empty -> {
        Box(
          modifier = Modifier.fillMaxSize().padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = uiState.message, style = TextStyle(
              fontFamily = FontFamily.Monospace,
              fontSize = 18.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.DarkGray,
              textAlign = TextAlign.Center
            )
          )
        }
      }

    }
  }
}

