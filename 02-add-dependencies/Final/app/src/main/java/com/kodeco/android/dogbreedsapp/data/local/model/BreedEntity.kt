package com.kodeco.android.dogbreedsapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kodeco.android.dogbreedsapp.domain.model.Breed

@Entity(tableName = "breeds")
data class BreedEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val name: String,
  val origin: String,
  val temperament: String,
  val imageUrl: String,
  val isLiked: Boolean = false,
  val isDisliked: Boolean = false
)


fun BreedEntity.toBreed() = Breed(
  id = this.id,
  name = this.name,
  origin = this.origin,
  temperament = this.temperament,
  isDisliked = this.isDisliked,
  isLiked =  this.isLiked,
  imageUrl = this.imageUrl
)
