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
package com.kodeco.android.dogbreedsapp.presentation.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kodeco.android.dogbreedsapp.R
import com.kodeco.android.dogbreedsapp.domain.model.Breed

@Composable
fun BreedDetailComponent(
  modifier: Modifier = Modifier,
  breed: Breed,
  onLikeClicked: (Breed) -> Unit,
  onDislikeClicked: (Breed) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      text = breed.name,
      textAlign = TextAlign.Center,
      fontSize = 18.sp,
      fontFamily = FontFamily.Monospace
    )

    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(breed.imageUrl)
        .crossfade(true)
        .build(),
      placeholder = painterResource(R.drawable.ic_launcher_foreground),
      contentDescription = "Breed Image",
      contentScale = ContentScale.Crop,

      )

  Spacer(modifier = Modifier.height(12.dp))
  Text(
    modifier = modifier.fillMaxWidth().padding(12.dp),
    text = breed.origin,
    style = TextStyle(
      fontFamily = FontFamily.Monospace,
      fontSize = 15.sp,
      fontWeight = FontWeight.Normal,
      color = Color.DarkGray,
    ),
  )
  Text(
    modifier = modifier.fillMaxWidth().padding(9.dp),
    text = breed.temperament,
    style = TextStyle(
      fontFamily = FontFamily.Monospace,
      fontSize = 15.sp,
      fontWeight = FontWeight.Normal,
      color = Color.DarkGray,
    ),
  )
  Spacer(modifier = Modifier.height(12.dp))

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 24.dp, top = 16.dp, bottom = 12.dp, end = 24.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.Bottom
  ) {
    Icon(
      tint = if (breed.isLiked) Color.Magenta else Color.DarkGray,
      painter = painterResource(id = R.drawable.outline_thumb_up_24),
      contentDescription = "Like Icon",

      modifier = Modifier.clickable {
        onLikeClicked(breed)
      }
    )
    Spacer(modifier = Modifier.width(40.dp))
    Icon(
      tint = if (breed.isDisliked) Color.Magenta else Color.DarkGray,
      painter = painterResource(id = R.drawable.baseline_thumb_down_off_alt_24),
      contentDescription = "Dislike Icon",
      modifier = Modifier.clickable {
        onDislikeClicked(breed)
      }
    )
  }

}}


