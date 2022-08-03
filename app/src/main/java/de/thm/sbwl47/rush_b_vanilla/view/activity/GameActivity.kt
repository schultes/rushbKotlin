package de.thm.sbwl47.rush_b_vanilla.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.controller.view.GameViewController
import de.thm.sbwl47.rush_b_vanilla.view.helper.DB


class GameActivity : AppCompatActivity() {

    private lateinit var viewController: GameViewController

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Set up Context and Activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        viewController = GameViewController(this)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onResume() {
        super.onResume()
        viewController.loadAndPlaySound()
    }

    override fun onStop() {
        super.onStop()
        viewController.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewController.stopMusicPlayer()
        viewController.stopSoundEffectPlayer()
    }

}