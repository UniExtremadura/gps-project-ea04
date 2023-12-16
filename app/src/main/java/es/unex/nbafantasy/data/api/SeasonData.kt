package com.example.example

import com.google.gson.annotations.SerializedName


data class SeasonData (

  @SerializedName("games_played" ) var gamesPlayed : Int?    = null,
  @SerializedName("player_id"    ) var playerId    : Int?    = null,
  @SerializedName("season"       ) var season      : Int?    = null,
  @SerializedName("min"          ) var min         : String? = null,
  @SerializedName("fgm"          ) var fgm         : Double? = null,
  @SerializedName("fga"          ) var fga         : Double? = null,
  @SerializedName("fg3m"         ) var fg3m        : Double? = null,
  @SerializedName("fg3a"         ) var fg3a        : Double? = null,
  @SerializedName("ftm"          ) var ftm         : Double? = null,
  @SerializedName("fta"          ) var fta         : Double? = null,
  @SerializedName("oreb"         ) var oreb        : Double? = null,
  @SerializedName("dreb"         ) var dreb        : Double? = null,
  @SerializedName("reb"          ) var reb         : Double? = null,
  @SerializedName("ast"          ) var ast         : Double? = null,
  @SerializedName("stl"          ) var stl         : Double? = null,
  @SerializedName("blk"          ) var blk         : Double? = null,
  @SerializedName("turnover"     ) var turnover    : Double? = null,
  @SerializedName("pf"           ) var pf          : Double? = null,
  @SerializedName("pts"          ) var pts         : Double? = null,
  @SerializedName("fg_pct"       ) var fgPct       : Double? = null,
  @SerializedName("fg3_pct"      ) var fg3Pct      : Double? = null,
  @SerializedName("ft_pct"       ) var ftPct       : Double? = null

)