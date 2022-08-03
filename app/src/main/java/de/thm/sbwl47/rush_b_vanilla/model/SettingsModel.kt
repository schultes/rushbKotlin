package de.thm.sbwl47.rush_b_vanilla.model

import de.thm.tp.library.sqlite.TPSQLiteEntity

class SettingsModel: TPSQLiteEntity {
    var id: Int = 1
    var sound: Boolean = true
    var music: Boolean = true
}