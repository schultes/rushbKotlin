package de.thm.sbwl47.rush_b_vanilla.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.controller.view.LevelMenuViewController

class LevelMenuActivity : AppCompatActivity() {

    private lateinit var viewController: LevelMenuViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_menu)
        viewController = LevelMenuViewController(this)

    }
}