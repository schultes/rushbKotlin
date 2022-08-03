package de.thm.sbwl47.rush_b_vanilla.controller.model

import de.thm.tp.library.json.TPJSONArray
import de.thm.tp.library.json.TPJSONObject

interface LevelMenuViewControllerInterface {

}

class LevelMenuModelController( private val viewControllerInterface: LevelMenuViewControllerInterface) {

    companion object {
        var levels: TPJSONArray = TPJSONArray()
    }

    fun setLevels(jsonString: String) {

        val obj = TPJSONObject(jsonString)
        // fetch JSONArray named levels
        LevelMenuModelController.levels = obj.getJSONArray("levels")!!
    }

    fun getLevelCount(): Int {
        return LevelMenuModelController.levels.length()
    }

    fun getLevelDifficulty(index: Int): String? {
        val levelObject = LevelMenuModelController.levels.getJSONObject(index)
        return levelObject?.getString("difficulty")
    }
}