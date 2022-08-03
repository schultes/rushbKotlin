package de.thm.sbwl47.rush_b_vanilla.view.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.controller.view.HelpContentViewController


class HelpContentActivity : AppCompatActivity() {

    private lateinit var viewController: HelpContentViewController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_content)
        viewController = HelpContentViewController(this)
    }
}