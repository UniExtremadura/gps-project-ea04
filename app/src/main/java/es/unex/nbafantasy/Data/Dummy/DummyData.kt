package es.unex.nbafantasy.Data.Dummy

import es.unex.nbafantasy.Data.api.Team
import es.unex.nbafantasy.Data.model.NBAData
import es.unex.nbafantasy.Data.model.NBATeam
val team = Team(
    12,
    "IND",
    "Indiana",
    "East",
    "Central",
    "Indiana Pacers",
    "Pacers"
)
val dummy: List<NBAData> = listOf(
    NBAData(
        14,
        "Ike",
        null,
        null,
        "Anigbogu",
        "C",
        team,
        null
    )
)


