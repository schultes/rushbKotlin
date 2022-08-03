package de.thm.sbwl47.rush_b_vanilla.controller.model

import de.thm.sbwl47.rush_b_vanilla.model.CountDownModel
import de.thm.sbwl47.rush_b_vanilla.view.helper.DB
import de.thm.sbwl47.rush_b_vanilla.model.HighscoreModel
import de.thm.sbwl47.rush_b_vanilla.model.SettingsModel
import de.thm.tp.library.sqlite.TPSQLite
import de.thm.tp.library.sqlite.eq

interface GameViewControllerInterface {
    fun setMusicAndSound(settings: SettingsModel)

    fun setCountDown(countDown: CountDownModel)

    fun setHighscore(highscore: HighscoreModel)
}

class GameModelController( private val viewControllerInterface: GameViewControllerInterface, private val conn: TPSQLite?) {

    fun loadSound() {
        conn?.query(SettingsModel::class.java, "SELECT * FROM settings;") { result, error ->
            result?.let { resultObject -> viewControllerInterface.setMusicAndSound(resultObject[0]) }
            error?.let { errorObject -> println("Error: ${errorObject}") }
        }
    }

    fun loadCountDown() {
        conn?.query(CountDownModel::class.java, "SELECT * FROM countdown;") { result, error ->
            result?.let { resultObject -> viewControllerInterface.setCountDown(resultObject[0]) }
            error?.let { errorObject -> println("Error: ${errorObject}") }
        }
    }

    fun updateCountDown(model: CountDownModel) {
        conn?.update("countdown", model, "id".eq(model.id)) { it ->
            it?.let { itObject -> println(itObject) }
        }
    }

    fun getScoreForLevel(level: String) {
        conn?.query(HighscoreModel::class.java, "SELECT * FROM highscore WHERE level = ?", arrayOf(level)) { result, error ->
            result?.let { resultObject -> viewControllerInterface.setHighscore(resultObject[0]) }
            error?.let { errorObject -> println("Error: ${errorObject}") }
        }
    }

    fun updateScore(model: HighscoreModel) {
        conn?.update("highscore", model, "id".eq(model.id!!)) { it ->
            it?.let { itObject -> println(itObject) }
        }
    }
}