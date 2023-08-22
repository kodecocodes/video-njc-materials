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

package com.kodeco.android.dogbreedsapp.data.util

import com.kodeco.android.dogbreedsapp.data.local.model.BreedEntity
import com.kodeco.android.dogbreedsapp.data.local.model.toBreed
import com.kodeco.android.dogbreedsapp.data.network.model.BreedNetworkResponseItem
import com.kodeco.android.dogbreedsapp.data.network.model.Image

val breedNetworkResponse = BreedNetworkResponseItem(
  bred_for = "Small rodent hunting, lapdog",
  breed_group = "Tay",
  name = "Affenpinscher",
  life_span = "10 - 12 years",
  image = Image(
    id = "BJa4kxc4X",
    height = 1600,
    width = 1199,
    url = "https://cdn2.thedogapi.com/images/BJa4kxc4X.jpg"
  ),
  origin = "Germany, France",
  reference_image_id = "BJa4kxc4X",
  temperament = "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving",
  weight = null,
  height = null,
  id = 1,
  country_code = null,
  description = null,
  history = null,
)
val breedEntity = BreedEntity(
  name = "Affenpinscher",
  temperament = "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving",
  origin = "Germany, France",
  imageUrl = "https://cdn2.thedogapi.com/images/BJa4kxc4X.jpg",
  isLiked = false,
  isDisliked = false
)

val breed = breedEntity.toBreed()

val breedsList = listOf(
  breed,
  breed,
  breed,
  breed
)



val breedsResponseList = listOf(
  breedNetworkResponse,
  breedNetworkResponse,
  breedNetworkResponse,
  breedNetworkResponse,
  breedNetworkResponse,
  breedNetworkResponse,
  breedNetworkResponse
)


val breedEntityList = listOf(
  breedEntity,
  breedEntity,
  breedEntity,
  breedEntity,
  breedEntity
)

var testBreedsList  = emptyList<BreedEntity>()