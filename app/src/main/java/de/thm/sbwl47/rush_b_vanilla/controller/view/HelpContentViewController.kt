package de.thm.sbwl47.rush_b_vanilla.controller.view

import android.content.Intent
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import de.thm.sbwl47.rush_b_vanilla.view.activity.HelpContentActivity
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.view.activity.GameMenuActivity

class HelpContentViewController(private val activity: HelpContentActivity) {

    private var helpText: TextView
    private var back: Button

    init {
        helpText = activity.findViewById(R.id.helpTextView)
        helpText.movementMethod = ScrollingMovementMethod()

        back = activity.findViewById(R.id.backmenu_button)
        back.setOnClickListener {
            val intent = Intent(activity, GameMenuActivity::class.java)
            activity.startActivity(intent)
        }
    }
}