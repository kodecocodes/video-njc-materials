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

package com.kodeco.android.dogbreedsapp.data.repository

import com.kodeco.android.dogbreedsapp.data.local.dao.BreedsDao
import com.kodeco.android.dogbreedsapp.data.local.model.BreedEntity
import com.kodeco.android.dogbreedsapp.data.local.model.toBreed
import com.kodeco.android.dogbreedsapp.data.mappers.toEntity
import com.kodeco.android.dogbreedsapp.data.network.BreedsApi
import com.kodeco.android.dogbreedsapp.data.network.safeApiCall
import com.kodeco.android.dogbreedsapp.domain.model.Breed
import com.kodeco.android.dogbreedsapp.domain.model.toEntity
import com.kodeco.android.dogbreedsapp.domain.repository.BreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import logcat.logcat

class BreedRepositoryImpl(
  private val breedsApi: BreedsApi,
  private val breedsDao: BreedsDao
) : BreedRepository {

  override  fun getAllBreeds(): Flow<List<Breed>> = flow {
    val breeds = breedsDao.getDogBreeds().first().map {
      it.toBreed()
    }
    if (breeds.isEmpty()) {
      fetchBreedsFromApi()
    }

    emit(breeds)
  }

  override suspend fun updateBreed(updatedBreed: Breed): Result<Int> {

    val result: Result<Int> = try {
      val res = breedsDao.updateBreed(updatedBreed.toEntity())
      if (res < 1) {
        logcat("BreedRepository") { "Update failed" }
        val exception = Exception("Something went wrong")
        Result.failure(exception)
      } else {
        Result.success(res)
      }
    } catch (e: Exception) {
      logcat("BreedRepository") { "Error when updating a breed ${e.message}" }
      Result.failure(e)
    }

    return result
  }

  private fun fetchBreeds(block: () -> Flow<List<BreedEntity>>): Flow<List<Breed>> = flow {
    val breeds = block().first().map {
      it.toBreed()
    }
    emit(breeds)
  }

  override fun fetchLikedBreeds(): Flow<List<Breed>> =
    fetchBreeds {
      breedsDao.fetchLikedBreeds()
    }

  override fun fetchDislikedBreeds(): Flow<List<Breed>> = fetchBreeds {
    breedsDao.fetchDisLikedBreeds()
  }

  override fun getBreedById(breedId: Int): Flow<Breed?> = flow{
   val result =  breedsDao.getBreedById(breedId).first()?.toBreed()
    emit(result)
  }


  suspend fun fetchBreedsFromApi() {
    val response = safeApiCall { breedsApi.getDogBreeds() }
    if (response.isSuccess) {
      val breeds = response.getOrNull()?.map { networkResponse ->
        networkResponse.toEntity()
      }

      try {
        breeds?.let {
          breedsDao.saveBreeds(it)
        }

      } catch (e: Exception) {
        logcat("BreedRepository") { "Error when saving breeds ${e.message}" }

      }

    }
  }


}