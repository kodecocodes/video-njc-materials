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


package com.kodeco.android.dogbreedsapp.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.kodeco.android.dogbreedsapp.domain.model.Breed
import com.kodeco.android.dogbreedsapp.presentation.view.components.BreedComponent
import com.kodeco.android.dogbreedsapp.presentation.view.screens.BreedsScreen
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.stopKoin

class BreedsScreenTest {

  @get:Rule
  val composeTestRule = createComposeRule()


  @After
  fun tearDown(){
    stopKoin()
  }
  @Test
  fun testToolbarIsDisplayed() {
    composeTestRule.setContent {
      BreedsScreen()
    }

    composeTestRule.onNodeWithContentDescription("BreedsToolbar").assertIsDisplayed()
  }

  @Test
  fun testBreedComponentIsDisplayed() {
    composeTestRule.setContent {
      BreedComponent(breed = Breed())
    }
    composeTestRule.onRoot(useUnmergedTree = true).printToLog("BreedComponentTest")
    composeTestRule.onNode(hasContentDescription("clickableImage") and hasParent(
      hasContentDescription("BreedComponent")) , useUnmergedTree = true)

    composeTestRule.onNode(hasContentDescription("likeIcon") and hasParent(
      hasContentDescription("BreedComponent")) , useUnmergedTree = true)

    composeTestRule.onNode(hasContentDescription("dislikeIcon") and hasParent(
      hasContentDescription("BreedComponent")) , useUnmergedTree = true)



  }
}