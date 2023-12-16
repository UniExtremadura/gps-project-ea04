package es.unex.nbafantasy.data.model

import java.io.Serializable

data class NBASeasonData (
    val gamesPlayed : Int,
    val playerId    : Int,
    val season      : Int,
    val min         : String,
    val fgm         : Double,
    val fga         : Double,
    val fg3m        : Double,
    val fg3a        : Double,
    val ftm         : Double,
    val fta         : Double,
    val oreb        : Double,
    val dreb        : Double,
    val reb         : Double,
    val ast         : Double,
    val stl         : Double,
    val blk         : Double,
    val turnover    : Double,
    val pf          : Double,
    val pts         : Double,
    val fgPct       : Double,
    val fg3Pct      : Double,
    val ftPct       : Double
) : Serializable