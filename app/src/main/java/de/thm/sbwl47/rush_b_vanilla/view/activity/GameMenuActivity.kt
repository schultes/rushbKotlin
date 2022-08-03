package de.thm.sbwl47.rush_b_vanilla.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.sbwl47.rush_b_vanilla.*
import de.thm.sbwl47.rush_b_vanilla.controller.view.GameMenuViewController

class GameMenuActivity : AppCompatActivity() {

    private lateinit var viewController: GameMenuViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_menu)

        viewController = GameMenuViewController(this)
    }
}