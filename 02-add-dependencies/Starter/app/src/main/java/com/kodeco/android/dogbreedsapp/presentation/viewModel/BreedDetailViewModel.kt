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
import com.kodeco.android.dogbreedsapp.domain.repository.BreedRepository
import com.kodeco.android.dogbreedsapp.presentation.models.BreedDetailAction
import com.kodeco.android.dogbreedsapp.presentation.models.BreedDetailUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class BreedDetailViewModel(
  private val breedRepository: BreedRepository
) : ViewModel() {

  private val _breedDetailUiState = MutableStateFlow<BreedDetailUiState>(BreedDetailUiState.Empty())
  val breedDetailUiState get() = _breedDetailUiState.asStateFlow()

  val uiActions = Channel<BreedDetailAction>()

  fun handleActions() {
    viewModelScope.launch {
      uiActions.consumeAsFlow().collect { event ->
        when (event) {
          is BreedDetailAction.GetBreedById -> getBreedById(event.breedId)
        }

      }
    }

  }

  fun getBreedById(breedId: Int) {
    viewModelScope.launch {
      breedRepository.getBreedById(breedId = breedId)
        .onStart {
          _breedDetailUiState.value = BreedDetailUiState.Loading
        }
        .catch { exception ->
          _breedDetailUiState.value =
            BreedDetailUiState.Error(message = exception.message ?: "Whoops! Something went wrong")
        }
        .collectLatest { breed ->
          if (breed == null) {
            _breedDetailUiState.value = BreedDetailUiState.Empty()
          } else {
            _breedDetailUiState.value = BreedDetailUiState.Data(breed = breed)
          }
        }
    }
  }


}