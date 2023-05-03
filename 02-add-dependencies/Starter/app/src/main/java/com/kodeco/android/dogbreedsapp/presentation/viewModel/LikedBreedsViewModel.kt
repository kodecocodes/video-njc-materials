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
package com.kodeco.android.dogbreedsapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodeco.android.dogbreedsapp.domain.model.Breed
import com.kodeco.android.dogbreedsapp.domain.repository.BreedRepository
import com.kodeco.android.dogbreedsapp.presentation.models.BreedAction
import com.kodeco.android.dogbreedsapp.presentation.models.BreedsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import logcat.logcat

class LikedBreedsViewModel(
  private val breedRepository: BreedRepository
) : ViewModel() {

  private val message ="Whoops!You've not liked any breeds, yet."

  private val _likedBreedsUiState = MutableStateFlow<BreedsUiState>(BreedsUiState.Empty(message))
  val likedBreedsUiState get() = _likedBreedsUiState.asStateFlow()

  val uiActions = Channel<BreedAction>()

  fun handleUiEvents() {
    viewModelScope.launch {
      uiActions.consumeAsFlow().collect { action ->
        when (action) {
          is BreedAction.GetBreeds -> getLikedBreeds()
          is BreedAction.LikeBreed -> markBreedAsLiked(action.updatedBreed)
          is BreedAction.DislikeBreed -> markBreedAsDisliked(action.updatedBreed)
          is BreedAction.Retry -> getLikedBreeds()
        }
      }
    }
  }

  private suspend fun getLikedBreeds() {
    breedRepository.fetchLikedBreeds()
      .onStart {
        _likedBreedsUiState.value = BreedsUiState.Loading
      }
      .catch { cause: Throwable ->
        _likedBreedsUiState.value = BreedsUiState.Error(cause.message ?: "Something went wrong!")
      }
      .collectLatest { breeds ->
        if (breeds.isEmpty()) {
          _likedBreedsUiState.value = BreedsUiState.Empty(message = message)
        } else {
          _likedBreedsUiState.value = BreedsUiState.Data(breeds = breeds)
        }
      }
  }

  private suspend fun updateBreed(updatedBreed: Breed) {
    val res = breedRepository.updateBreed(updatedBreed = updatedBreed)
    if (res.isSuccess) {
      uiActions.send(BreedAction.GetBreeds)
    } else {
      val error = res.exceptionOrNull()
      logcat("BreedsViewModel") { "Update failed $error" }
    }
  }


  private suspend fun markBreedAsLiked(updatedBreed: Breed) {
    viewModelScope.launch(Dispatchers.IO) {
      updateBreed(updatedBreed)
    }
  }

  private suspend fun markBreedAsDisliked(updatedBreed: Breed) {
    viewModelScope.launch(Dispatchers.IO) {
      updateBreed(updatedBreed)
    }
  }
}
