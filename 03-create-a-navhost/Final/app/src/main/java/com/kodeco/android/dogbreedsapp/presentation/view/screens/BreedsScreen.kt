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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kodeco.android.dogbreedsapp.presentation.models.BreedAction
import com.kodeco.android.dogbreedsapp.presentation.models.BreedsUiState
import com.kodeco.android.dogbreedsapp.presentation.view.components.BreedsListComponent
import com.kodeco.android.dogbreedsapp.presentation.view.components.ErrorComponent
import com.kodeco.android.dogbreedsapp.presentation.viewModel.BreedsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.kodeco.android.dogbreedsapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedsScreen(
  breedsViewModel: BreedsViewModel = koinViewModel(),
  navigateToDetailScreen: (Int) -> Unit = {},
  navigateToAboutScreen: () -> Unit = {}
) {
  val scope = rememberCoroutineScope()
  LaunchedEffect(key1 = true) {
    breedsViewModel.uiActions.send(BreedAction.GetBreeds)

  }

  LaunchedEffect(key1 = true) {
    breedsViewModel.handleUiEvents()

  }
  val uiState = breedsViewModel.breedUiState.collectAsStateWithLifecycle().value

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .clearAndSetSemantics { contentDescription = "BreedsToolbar" },
            text = "Dog Breeds",
            textAlign = TextAlign.Start,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace
          )
        },
        actions = {
          IconButton(onClick = navigateToAboutScreen,
          modifier = Modifier.clearAndSetSemantics { contentDescription = "aboutIcon" }) {
            Icon(painter = painterResource(id = R.drawable.ic_about), contentDescription = null)
          }
        })
    },
  ) { contentPadding ->

    when (uiState) {
      is BreedsUiState.Loading -> {
        LinearProgressIndicator()
      }

      is BreedsUiState.Error -> {
        ErrorComponent(
          message = uiState.message,
          retry = {
            scope.launch {
              breedsViewModel.uiActions.send(BreedAction.Retry)
            }
          },
        )
      }

      is BreedsUiState.Data -> {
        BreedsListComponent(
          breeds = uiState.breeds,
          modifier = Modifier
            .padding(contentPadding),
          navigateToDetailScreen = navigateToDetailScreen,
          onBreedDisliked = { breed ->
            val updatedBreed = breed.copy(isDisliked = true, isLiked = false)
            scope.launch {
              breedsViewModel.uiActions.send(BreedAction.DislikeBreed(updatedBreed = updatedBreed))
            }
          },
          onBreedLiked = { breed ->

            val updatedBreed = breed.copy(isLiked = true, isDisliked = false)
            scope.launch {
              breedsViewModel.uiActions.send(BreedAction.LikeBreed(updatedBreed = updatedBreed))
            }
          },
        )
      }

      is BreedsUiState.Empty -> {
        ErrorComponent(message = uiState.message,
          retry = {
            scope.launch {
              breedsViewModel.uiActions.send(BreedAction.Retry)
            }
          }
        )
      }

    }
  }
}
