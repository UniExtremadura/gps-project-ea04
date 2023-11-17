package es.unex.nbafantasy.Data.api

import com.google.gson.annotations.SerializedName


data class NBAPlayers (

  @SerializedName("data" ) var data : ArrayList<Data> = arrayListOf(),
  @SerializedName("meta" ) var meta : Meta?           = Meta()

)