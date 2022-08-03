package de.thm.sbwl47.rush_b_vanilla.model

import de.thm.tp.library.sqlite.TPSQLiteEntity

class HighscoreModel: TPSQLiteEntity {
    var id: Int? = null
    var level: String = ""
    var score: Int = 0
}