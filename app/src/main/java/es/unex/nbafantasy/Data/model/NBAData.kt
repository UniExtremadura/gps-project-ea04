package es.unex.nbafantasy.Data.model

import com.google.gson.annotations.SerializedName
import es.unex.nbafantasy.Data.api.Team
import java.io.Serializable
data class NBAData (
    val id: Int,
    val firstName: String,
    val heightFeet   : String? = null,
    val heightInches : String? = null,
    val lastName     : String,
    val position     : String? = null,
    val team         : Team?   = Team(),
    val weightPounds : String? = null

) : Serializable