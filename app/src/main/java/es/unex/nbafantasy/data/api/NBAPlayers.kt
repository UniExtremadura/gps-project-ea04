package es.unex.nbafantasy.data.api

import com.google.gson.annotations.SerializedName


data class NBAPlayers (

  @SerializedName("data" ) var data : ArrayList<Data> = arrayListOf(),
  @SerializedName("meta" ) var meta : Meta?           = Meta()

)