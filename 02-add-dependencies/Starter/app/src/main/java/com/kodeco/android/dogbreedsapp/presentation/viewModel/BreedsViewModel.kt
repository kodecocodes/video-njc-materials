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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import logcat.logcat

class BreedsViewModel(
  private val breedRepository: BreedRepository,
  private val dispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _breedUiState = MutableStateFlow<BreedsUiState>(BreedsUiState.Empty())
  val breedUiState get() = _breedUiState.asStateFlow()

  val uiActions = Channel<BreedAction>()

  fun handleUiEvents() {
    viewModelScope.launch(dispatcher) {
      uiActions.consumeAsFlow().collect { uiEvent ->
        when (uiEvent) {
          is BreedAction.GetBreeds -> fetchBreeds()
          is BreedAction.Retry -> fetchBreeds()
          is BreedAction.LikeBreed -> updateBreed(uiEvent.updatedBreed)
          is BreedAction.DislikeBreed -> updateBreed(uiEvent.updatedBreed)

        }
      }
    }
  }

  private suspend fun updateBreed(updatedBreed: Breed) {

    viewModelScope.launch(dispatcher) {
      val res = breedRepository.updateBreed(updatedBreed = updatedBreed)

      if (res.isSuccess) {
        uiActions.send(BreedAction.GetBreeds)
      } else {
        val error = res.exceptionOrNull()
        logcat("BreedsViewModel") { "Update failed $error" }
      }
    }

  }


  private suspend fun fetchBreeds() {
    breedRepository.getAllBreeds() .onStart {
      _breedUiState.value = BreedsUiState.Loading
    }
      .catch { cause: Throwable ->
        _breedUiState.value = BreedsUiState.Error(cause.message ?: "Something went wrong!")
      }
      .collectLatest { breeds ->
        if (breeds.isEmpty()) {
          _breedUiState.value = BreedsUiState.Empty()
        } else {
          _breedUiState.value = BreedsUiState.Data(breeds = breeds)
        }
      }
  }

}