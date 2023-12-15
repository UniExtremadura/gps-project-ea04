package es.unex.nbafantasy.data.model

import es.unex.nbafantasy.data.api.Team
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