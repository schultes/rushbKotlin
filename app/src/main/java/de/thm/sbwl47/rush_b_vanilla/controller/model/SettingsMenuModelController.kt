package de.thm.sbwl47.rush_b_vanilla.controller.model

import de.thm.sbwl47.rush_b_vanilla.model.HighscoreModel
import de.thm.sbwl47.rush_b_vanilla.model.SettingsModel
import de.thm.tp.library.sqlite.TPSQLite
import de.thm.tp.library.sqlite.eq

interface SettingsMenuViewControllerInterface {
    fun setInitialSettings(settings: SettingsModel)
}

class SettingsMenuModelController( private val viewController: SettingsMenuViewControllerInterface, private val conn: TPSQLite?) {

    fun getInitialSettings() {
        conn?.query(SettingsModel::class.java, "SELECT * FROM settings;") { result, error ->
            result?.let { resultObject -> viewController.setInitialSettings(resultObject[0])}
            error?.let { errorObject -> println("Error: ${errorObject}") }
        }
    }

    fun changeSettings(settings: SettingsModel) {
        conn?.update("settings", settings, "id".eq(1)) {
            it?.let { itObject -> println(itObject) }
        }
    }

    fun resetHighscore() {
        conn?.query(HighscoreModel::class.java, "UPDATE highscore SET score = 0")  { _, error ->
            error?.let { errorObject -> println("Error: ${errorObject}") }
        }
    }
}