package de.thm.sbwl47.rush_b_vanilla.controller.view

import android.content.Intent
import android.widget.Button
import de.thm.sbwl47.rush_b_vanilla.*
import de.thm.sbwl47.rush_b_vanilla.controller.model.GameMenuModelController
import de.thm.sbwl47.rush_b_vanilla.controller.model.GameMenuViewControllerInterface
import de.thm.sbwl47.rush_b_vanilla.view.helper.DB
import de.thm.sbwl47.rush_b_vanilla.view.activity.*
import de.thm.sbwl47.rush_b_vanilla.view.helper.JSONLoader

class GameMenuViewController(private val activity: GameMenuActivity): GameMenuViewControllerInterface {

    private lateinit var modelController: GameMenuModelController

    private var help: Button
    private var levels: Button
    private var generate: Button
    private var settings: Button

    init {
        if (DB.conn == null) {
            DB().createConn(activity)
            val pref = activity.getSharedPreferences("appStart", 0)
            if (!pref.contains("firstStart")){
                val editor = pref.edit()
                editor.putBoolean("firstStart", true)
                editor.apply()
                modelController = GameMenuModelController(this, DB.conn)
                modelController.setInitialSettings()
                modelController.setInitialCountDown()
                modelController.setInitialHighscore()
            }
        } else {
            modelController = GameMenuModelController(this, null)
        }

        help = activity.findViewById(R.id.help_button)
        levels = activity.findViewById(R.id.levels_button)
        generate = activity.findViewById(R.id.generate_button)
        settings = activity.findViewById(R.id.settings_button)

        help.setOnClickListener {
            val intent = Intent(activity, HelpContentActivity::class.java)
            activity.startActivity(intent)
        }

        levels.setOnClickListener {
            val intent = Intent(activity, LevelMenuActivity::class.java)
            activity.startActivity(intent)
        }

        generate.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            activity.startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(activity, SettingsMenuActivity::class.java)
            activity.startActivity(intent)
        }

    }

    override fun getLevel(): String {
        return JSONLoader.loadJSONFromAsset(activity)
    }

}
