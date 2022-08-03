package de.thm.sbwl47.rush_b_vanilla.controller.view

import android.content.Intent
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import de.thm.sbwl47.rush_b_vanilla.*
import de.thm.sbwl47.rush_b_vanilla.controller.model.SettingsMenuModelController
import de.thm.sbwl47.rush_b_vanilla.controller.model.SettingsMenuViewControllerInterface
import de.thm.sbwl47.rush_b_vanilla.model.SettingsModel
import de.thm.sbwl47.rush_b_vanilla.view.activity.*
import de.thm.sbwl47.rush_b_vanilla.view.helper.DB
import de.thm.tp.library.sqlite.TPSQLite

class SettingsMenuViewController(private val activity: SettingsMenuActivity): SettingsMenuViewControllerInterface {

    private var modelController: SettingsMenuModelController

    private var gameMenuButton: Button
    private var musicSwitch: Switch
    private var soundSwitch: Switch
    private var timeHighscoreResetButton: Button

    var settings = SettingsModel()

    init {
        modelController = SettingsMenuModelController(this, DB.conn)

        gameMenuButton = activity.findViewById(R.id.game_menu)
        gameMenuButton.setOnClickListener {
            val intent = Intent(activity, GameMenuActivity::class.java)
            activity.startActivity(intent)
        }

        modelController.getInitialSettings()

        // set switches according to settings
        musicSwitch = activity.findViewById(R.id.music_switch)
        musicSwitch.textOff = "OFF"
        musicSwitch.textOn = "ON"
        musicSwitch.isChecked = settings.music

        soundSwitch = activity.findViewById(R.id.sound_switch)
        soundSwitch.textOff = "OFF"
        soundSwitch.textOn = "ON"
        soundSwitch.isChecked = settings.sound

        timeHighscoreResetButton = activity.findViewById(R.id.resetHighscore_button)
        timeHighscoreResetButton.setOnClickListener {

            modelController.resetHighscore()

            val resetText = "Highscores wurden zurückgesetzt."
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(activity, resetText, duration)
            toast.show()
        }

        musicSwitch.setOnCheckedChangeListener { _, _ ->
            settings.music = !settings.music
            modelController.changeSettings(settings)
        }

        soundSwitch.setOnCheckedChangeListener { _, _ ->
            settings.sound = !settings.sound
            modelController.changeSettings(settings)
        }

    }

    override fun setInitialSettings(settings: SettingsModel) {
        this.settings = settings
    }
}