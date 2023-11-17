package es.unex.nbafantasy.Data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NBATeam (
    var id           : Int,
    var abbreviation : String,
    var city         : String,
    var conference   : String,
    var division     : String,
    var fullName     : String,
    var name         : String

): Serializable