package es.unex.nbafantasy.data

import com.example.example.SeasonData
import es.unex.nbafantasy.data.api.Data
import es.unex.nbafantasy.data.api.Team
import es.unex.nbafantasy.data.model.NBAData
import es.unex.nbafantasy.data.model.NBASeasonData

fun Data.toNBAData() = NBAData(
    id = id ?: 0,
    firstName = firstName ?: "",
    heightFeet = heightFeet?: "",
    heightInches = heightInches?: "",
    lastName = lastName?: "",
    position = position?: "",
    team = team?: Team(),
    weightPounds = weightPounds?: ""

)
fun SeasonData.toSeasonAverages() = NBASeasonData(
    gamesPlayed = gamesPlayed?: 0,
    playerId    = playerId?: 0,
    season      = season?: 0,
    min         = min?: "",
    fgm         = fgm?: 0.0,
    fga         = fga?: 0.0,
    fg3m        = fg3m?: 0.0,
    fg3a        = fg3a?: 0.0,
    ftm         = ftm?: 0.0,
    fta         = fta?: 0.0,
    oreb        = oreb?: 0.0,
    dreb        = dreb?: 0.0,
    reb         = reb?: 0.0,
    ast         = ast?: 0.0,
    stl         = stl?: 0.0,
    blk         = blk?: 0.0,
    turnover    = turnover?: 0.0,
    pf          = pf?: 0.0,
    pts         = pts?: 0.0,
    fgPct       = fgPct?: 0.0,
    fg3Pct      = fg3Pct?: 0.0,
    ftPct       = ftPct?: 0.0

)