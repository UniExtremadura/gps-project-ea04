package com.example.example

import com.google.gson.annotations.SerializedName


data class SeasonAverages (

  @SerializedName("data" ) var data : ArrayList<SeasonData> = arrayListOf()

)