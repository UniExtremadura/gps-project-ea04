package es.unex.nbafantasy.data.api

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("id"            ) var id           : Int?    = null,
  @SerializedName("first_name"    ) var firstName    : String? = null,
  @SerializedName("height_feet"   ) var heightFeet   : String? = null,
  @SerializedName("height_inches" ) var heightInches : String? = null,
  @SerializedName("last_name"     ) var lastName     : String? = null,
  @SerializedName("position"      ) var position     : String? = null,
  @SerializedName("team"          ) var team         : Team?   = Team(),
  @SerializedName("weight_pounds" ) var weightPounds : String? = null

)