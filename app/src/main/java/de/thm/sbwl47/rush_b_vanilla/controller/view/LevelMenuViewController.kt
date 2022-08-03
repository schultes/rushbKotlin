package de.thm.sbwl47.rush_b_vanilla.controller.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import de.thm.sbwl47.rush_b_vanilla.*
import de.thm.sbwl47.rush_b_vanilla.view.helper.JSONLoader
import de.thm.sbwl47.rush_b_vanilla.controller.model.LevelMenuModelController
import de.thm.sbwl47.rush_b_vanilla.controller.model.LevelMenuViewControllerInterface
import de.thm.sbwl47.rush_b_vanilla.view.activity.*

class LevelMenuViewController(private val activity: LevelMenuActivity) :
    LevelMenuViewControllerInterface {

    private var modelController: LevelMenuModelController

    private var gameMenuButton: Button
    private var layout: LinearLayout

    init {
        modelController = LevelMenuModelController(this)

        gameMenuButton = activity.findViewById(R.id.game_menu)
        gameMenuButton.setOnClickListener {
            val intent = Intent(activity, GameMenuActivity::class.java)
            activity.startActivity(intent)
        }

        layout = activity.findViewById(R.id.linearLayout)

        var jsonString = JSONLoader.loadJSONFromAsset(activity)
        modelController.setLevels(jsonString)

        var horizontalLayout: LinearLayout?
        horizontalLayout = LinearLayout(activity)
        horizontalLayout.orientation = LinearLayout.HORIZONTAL

        for (i in 0 until modelController.getLevelCount()) {

            if ((i) % 4 == 0) {
                horizontalLayout = LinearLayout(activity)
                horizontalLayout.orientation = LinearLayout.HORIZONTAL
            }

            // creating the button
            val button = Button(activity)
            // setting layout_width and layout_height using layout parameters
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            button.text = (i + 1).toString()

            var difficulty = modelController.getLevelDifficulty(i)
            if (difficulty == "Beginner") {
                //green = beginner
                button.setTextColor(Color.parseColor("#228B22"))
            }
            if (difficulty == "Intermediate") {
                //orange = intermediate
                button.setTextColor(Color.parseColor("#F37C22"))
            }
            if (difficulty == "Advanced") {
                //blue
                button.setTextColor(Color.parseColor("#0000FF"))
            }
            if (difficulty == "Expert") {
                //red
                button.setTextColor(Color.parseColor("#B22222"))
            }
            button.setOnClickListener {
                val intent = Intent(activity, GameActivity::class.java)
                val b = Bundle()
                b.putInt("level", i + 1) //chosen level
                intent.putExtras(b)
                activity.startActivity(intent)
            }

            // add Button to LinearLayout
            horizontalLayout?.addView(button)

            //add to Layout if we have less than 4 buttons ->  1 row
            if (horizontalLayout?.parent == layout) {
                layout.removeView(horizontalLayout)
            }
            layout.addView(horizontalLayout)
        }
    }
}