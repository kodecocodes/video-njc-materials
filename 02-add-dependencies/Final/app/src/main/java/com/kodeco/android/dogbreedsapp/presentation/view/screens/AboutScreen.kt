package com.kodeco.android.dogbreedsapp.presentation.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {
  Column(modifier = Modifier.fillMaxSize().padding(32.dp),
  horizontalAlignment = Alignment.CenterHorizontally,
  verticalArrangement = Arrangement.Top) {
    Text(
      text = "Did you know?", style = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
      )
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text = "Yup, medical detection dogs are a thing. Because their sense of smell is so great, " +
          "some dogs can be trained to sniff out medical conditions. They are used to diagnose a " +
          "particular condition or to alert their owners if they need more medication. Some are even " +
          "being trained to sniff out Covid-19!",
      style = TextStyle(
        textAlign = TextAlign.Justify,
        fontFamily = FontFamily.Monospace,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
      )
    )
  }
}