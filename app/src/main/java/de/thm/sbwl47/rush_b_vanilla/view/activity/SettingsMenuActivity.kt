package de.thm.sbwl47.rush_b_vanilla.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.controller.view.SettingsMenuViewController
import de.thm.sbwl47.rush_b_vanilla.view.helper.DB

class SettingsMenuActivity : AppCompatActivity() {

    private lateinit var viewController: SettingsMenuViewController

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_menu)
        viewController = SettingsMenuViewController(this)
    }
}