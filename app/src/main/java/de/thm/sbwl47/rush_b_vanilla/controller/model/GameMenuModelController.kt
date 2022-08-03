package de.thm.sbwl47.rush_b_vanilla.controller.model

import de.thm.sbwl47.rush_b_vanilla.model.CountDownModel
import de.thm.sbwl47.rush_b_vanilla.model.HighscoreModel
import de.thm.sbwl47.rush_b_vanilla.model.SettingsModel
import de.thm.tp.library.json.TPJSONObject
import de.thm.tp.library.sqlite.TPSQLite
import de.thm.tp.library.sqlite.TPSQLiteColumn
import de.thm.tp.library.sqlite.TPSQLiteDataType
import de.thm.tp.library.sqlite.TPSQLiteTable

interface GameMenuViewControllerInterface {
    fun getLevel(): String
}

class GameMenuModelController(private val viewControllerInterface: GameMenuViewControllerInterface, private val conn: TPSQLite?) {

    fun setInitialSettings() {
        conn?.dropTable("settings"){ _ ->
            println("removed table settings")
        }

        var tableSettings = TPSQLiteTable("settings", arrayOf(
            TPSQLiteColumn("id", TPSQLiteDataType.Integer).PrimaryKey(),
            TPSQLiteColumn("sound", TPSQLiteDataType.Integer),
            TPSQLiteColumn("music", TPSQLiteDataType.Integer)
        ))

        conn?.createTable(tableSettings) {connObject ->
            println(connObject)
        }

        conn?.insert("settings", SettingsModel()) { _ ->
            println("inserted")
        }
    }

    fun setInitialCountDown() {
        conn?.dropTable("countdown"){ _ ->
            println("removed table countdown")
        }

        var tableCountDown = TPSQLiteTable("countdown", arrayOf(
            TPSQLiteColumn("id", TPSQLiteDataType.Integer).PrimaryKey(),
            TPSQLiteColumn("seconds", TPSQLiteDataType.Integer)
        ))

        conn?.createTable(tableCountDown) {connObject ->
            println(connObject)
        }

        conn?.insert("countdown", CountDownModel()) { _ ->
            println("inserted")
        }
    }

    fun setInitialHighscore() {
        conn?.dropTable("highscore"){ _ ->
            println("removed table highscore")
        }

        var tableHighscore = TPSQLiteTable("highscore", arrayOf(
            TPSQLiteColumn("id", TPSQLiteDataType.Integer).PrimaryKey(),
            TPSQLiteColumn("level", TPSQLiteDataType.Text),
            TPSQLiteColumn("score", TPSQLiteDataType.Integer)

        ))

        conn?.createTable(tableHighscore) { connObject ->
            println(connObject)
        }

        val jsonString = viewControllerInterface.getLevel()
        val jsonObject = TPJSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("levels")!!

        for (i in 0 until jsonArray.length() + 1) {
            val highscoreModel = HighscoreModel()
            highscoreModel.id = i
            if (i == 0) {
                highscoreModel.level = "timeBreaker"
            } else {
                highscoreModel.level = "level_${i}"
            }
            conn?.insert("highscore", highscoreModel) { _ ->
                println("inserted")
            }
        }
    }
}
