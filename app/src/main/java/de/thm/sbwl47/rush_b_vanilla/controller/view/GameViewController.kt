package de.thm.sbwl47.rush_b_vanilla.controller.view

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.view.adapter.UiCellAdapter
import de.thm.sbwl47.rush_b_vanilla.controller.model.GameModelController
import de.thm.sbwl47.rush_b_vanilla.controller.model.GameViewControllerInterface
import de.thm.sbwl47.rush_b_vanilla.controller.model.LevelMenuModelController
import de.thm.sbwl47.rush_b_vanilla.model.*
import de.thm.sbwl47.rush_b_vanilla.view.activity.*
import de.thm.sbwl47.rush_b_vanilla.view.helper.DB
import de.thm.sbwl47.rush_b_vanilla.view.helper.JSONLoader
import de.thm.tp.library.sqlite.TPSQLite
import java.util.*

class GameViewController(private val activity: GameActivity): GameViewControllerInterface {

    private var modelController: GameModelController

    private lateinit var adapter: UiCellAdapter

    private var grid = Grid()
    private var count: Int = 0
    private val w: Int = 6
    private val h: Int = 6
    private var level: Int = 0

    private var currentLevel : String
    private var timeBreakerLevel = 0

    var backgroundPlayer: MediaPlayer? = null
    var soundEffectPlayer: MediaPlayer? = null
    lateinit var countDown: CountDownTimer

    private lateinit var menu: Button
    private lateinit var textView: TextView
    private lateinit var nextLevel: Button
    private lateinit var buttonLayout: ConstraintLayout
    private var highScoreLayout: LinearLayout
    private var recyclerView: RecyclerView
    private var counterTextView: TextView
    private var resetLevelButton: Button
    private var levelMenuButton: Button

    var countDownModel = CountDownModel()
    var settingsModel = SettingsModel()
    var highscoreModel = HighscoreModel()

    init {
        modelController = GameModelController(this, DB.conn)

        // get level parameter
        val extras = activity.intent.extras
        if (extras != null) {
            level = extras.getInt("level")
            timeBreakerLevel = extras.getInt("timeBreakerLevel")
        }

        highScoreLayout = activity.findViewById(R.id.linearLayout)

        // Set up Game if it is started in auto generated mode
        if(level == 0){
            modelController.loadCountDown()
            if(timeBreakerLevel > 0) {
                startTimer(countDownModel.seconds.toLong())
            } else {
                startTimer(90000)
            }
        }

        recyclerView = activity.findViewById(R.id.recyclerview)
        counterTextView = activity.findViewById(R.id.counterTextView)

        currentLevel = "level_$level"
        if(level == 0){
            modelController.getScoreForLevel("timeBreaker")
            counterTextView.text = "Level Nr.: $timeBreakerLevel | Highscore: ${highscoreModel.score}"
        } else {
            modelController.getScoreForLevel(currentLevel)
            counterTextView.text = "Counter: $count | Highscore: ${highscoreModel.score}"
        }

        // Define Reset-Button
        resetLevelButton = activity.findViewById(R.id.reset_level)
        resetLevelButton.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            val b = Bundle()
            b.putInt("level", level)
            intent.putExtras(b)
            activity.startActivity(intent)
            countDown.cancel()
        }

        // Define Levels Buttons
        levelMenuButton = activity.findViewById(R.id.level_menu)
        levelMenuButton.setOnClickListener {
            var intent = Intent(activity, GameMenuActivity::class.java)
            if(level > 0) {
                intent = Intent(activity, LevelMenuActivity::class.java)
            }
            activity.startActivity(intent)
        }

        // Generate Adapter with `onClick` function
        adapter = UiCellAdapter(activity) {

            playSoundEffect()

            // Check if Touch hit a Block
            if (it.block != null) {
                // Figure which button has been touched
                val block: Block = grid.blockList.first { block: Block -> it.block == block }
                when (block.orientation) {
                    // Differentiate between horizontal and vertical movement
                    Block.Orientations.HORIZONTAL -> {
                        if (it.position.x == block.start.x) {
                            // Try to move the block towards the 0 point, count up counter if successful
                            if(grid.move(block, false)) this.count++
                        } else if (it.position.x == block.end.x) {
                            // Try to move hte block towards the edge, count up counter if successful
                            if(grid.move(block, true)){
                                this.count++
                            } else {
                                this.count++
                                // Check if Start is on the finishing line
                                if (block.end.x == w-1 && block.isStart && !grid.move(block, true)) {
                                    finished()
                                    return@UiCellAdapter
                                }
                            }
                        }
                    }
                    Block.Orientations.VERTICAL -> {
                        // Check if the block should move towards 0 and if it can count up
                        if (it.position.y == block.start.y) {
                            if(grid.move(block, false)) this.count++
                        } else if (it.position.y == block.end.y) {
                            if(grid.move(block, true)) this.count++
                        }
                    }
                }
                // Show the Contuer and Highscore if its not a generated map
                if(level > 0){
                    //counterTextView.text = "Counter: $count | Highscore: $highscore"
                    counterTextView.text = "Counter: $count | Highscore: ${highscoreModel.score}"
                }

                // remove red background-color from startblock if moved
                if(this.count > 0 && it.block != null) {
                    if(it.block!!.isStart) {
                        it.block!!.color = Block.Colors.GREY
                    }
                }

                val cells = convert(grid)
                adapter.updateList(cells)

            }

        }
        // Set Stable ID and remove Animations to reduce visual distractions
        adapter.setHasStableIds(true)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        // Create the generated grid in a thread, so it doesnt clog the UI Thread
        Thread(Runnable() {

            grid.generate(w, h, JSONLoader.loadJSONFromAsset(activity), level)
            val cells = convert(grid)
            recyclerView.post {
                adapter.cells.clear()
                adapter.cells.addAll(cells)
                adapter.notifyDataSetChanged()
            }
        }).start()

        recyclerView.layoutManager = GridLayoutManager(activity, w)
        recyclerView.adapter = adapter

    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    fun startTimer(millisUntilFinished: Long) {
        textView = activity.findViewById(R.id.gametitle)
        var countDown = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countDownModel.seconds = millisUntilFinished.toInt()
                modelController.updateCountDown(countDownModel)
                textView.setText((millisUntilFinished / 1000).toString() + "s")
            }

            override fun onFinish() {
                countDownModel.seconds = 0
                modelController.updateCountDown(countDownModel)
                finished()
            }
        }
        countDown.start()
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun finished() {
        // Check if the level was a generated one
        if(level > 0) {
            //set new highscore
            if (highscoreModel.score == 0 || count <= highscoreModel.score) {
                highscoreModel.score = count
                modelController.updateScore(highscoreModel)
            }

            activity.setContentView(R.layout.endscreen_level_menu)
            menu = activity.findViewById(R.id.gamemenu_button)
            menu.setOnClickListener {
                val intent = Intent(activity, GameMenuActivity::class.java)
                activity.startActivity(intent)
            }

            textView = activity.findViewById(R.id.textView)
            textView.text = "Du hast $count Züge dafür benötigt!"

            nextLevel = activity.findViewById(R.id.next_Level)
            if(level < LevelMenuModelController.levels.length()) {
                nextLevel.setOnClickListener {
                    val intent = Intent(activity, GameActivity::class.java)
                    val b = Bundle()
                    b.putInt("level", level + 1)
                    intent.putExtras(b)
                    activity.startActivity(intent)
                }
            } else {
                buttonLayout = activity.findViewById(R.id.buttonLayout)
                buttonLayout.removeView(nextLevel)
            }
        } else {
            if(countDownModel.seconds.toLong() == (0).toLong()) {
                activity.setContentView(R.layout.endscreen_generated_menu)
                menu = activity.findViewById<Button>(R.id.gamemenu_button)
                menu.setOnClickListener {
                    val intent = Intent(activity, GameMenuActivity::class.java)
                    activity.startActivity(intent)
                }

                textView = activity.findViewById(R.id.textView)
                textView.text = "Du hast $timeBreakerLevel Level in der Zeit geschafft!"

                nextLevel = activity.findViewById(R.id.next_Level)
                nextLevel.setOnClickListener {
                    val intent = Intent(activity, GameActivity::class.java)
                    activity.startActivity(intent)
                }
            } else {
                timeBreakerLevel++
                grid.blockList.clear()
                grid.blockList.addAll(grid.fixedBlockList)
                adapter.updateList(convert(grid))

                if (timeBreakerLevel > highscoreModel.score) {
                    highscoreModel.score = timeBreakerLevel
                    modelController.updateScore(highscoreModel)
                }
                val intent = Intent(activity, GameActivity::class.java)
                val b = Bundle()
                b.putInt("timeBreakerLevel", timeBreakerLevel)
                intent.putExtras(b)
                activity.startActivity(intent)
            }
        }

    }

    /**
     * @param grid the grid containing the blocks that is supposed to be converted to a list of cells
     * @return cells that are representing the blocklist in a UI readable form
     */
    private fun convert(grid: Grid): List<Cell> {
        val cells = ArrayList<Cell>(grid.width * grid.height)
        var index : Int = 0
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                val position = Position(j, i)
                val block = grid.blockList.find { it.contains(position) }
                var offset: Int = -1
                if (block != null) {
                    when(block.orientation) {
                        Block.Orientations.HORIZONTAL -> {
                            offset = position.x - block.start.x
                        }
                        Block.Orientations.VERTICAL -> {
                            offset = position.y - block.start.y
                        }
                    }
                }
                cells.add(
                    Cell(
                    id = index++,
                    position = position,
                    block = block,
                    offset = offset
                )
                )
            }
        }
        return cells
    }

    fun playSoundEffect(){
        if(settingsModel.sound){
            if(soundEffectPlayer == null){
                val soundArray = intArrayOf(
                    R.raw.honk,
                    R.raw.double_honk,
                    R.raw.car_engine,
                    R.raw.car_crash,
                    R.raw.car_tires
                )
                val randomNumber: Int = Random().nextInt(soundArray.size)
                soundEffectPlayer = MediaPlayer.create(activity, soundArray[randomNumber])

                soundEffectPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                    override fun onCompletion(mp: MediaPlayer?) {
                        stopSoundEffectPlayer()
                    }
                })
            }
            soundEffectPlayer!!.setVolume(80f, 80f)
            soundEffectPlayer?.start()
        }
    }

    fun playMusic(){
		/* DEACTIVATED due to copyright reasons
        if(backgroundPlayer == null){
            backgroundPlayer = MediaPlayer.create(activity, R.raw.puzzle_music)
            backgroundPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {
                    stopMusicPlayer()
                    playMusic()
                }
            })
        }
        backgroundPlayer!!.isLooping = true // Set looping
        backgroundPlayer!!.setVolume(100f, 100f)
        backgroundPlayer?.start()
		*/
    }

    fun pauseMusic(){
        backgroundPlayer?.pause()
    }

    fun stopSoundEffectPlayer(){
        soundEffectPlayer?.release()
        soundEffectPlayer = null
    }

    fun stopMusicPlayer(){
        backgroundPlayer?.release()
        backgroundPlayer = null
    }

    fun loadAndPlaySound(){
        modelController.loadSound()
        if(settingsModel.music){
            playMusic()
        }
    }

    override fun setMusicAndSound(settings: SettingsModel) {
        this.settingsModel = settings
    }

    override fun setCountDown(countDown: CountDownModel) {
        this.countDownModel = countDown
    }

    override fun setHighscore(highscore: HighscoreModel) {
        this.highscoreModel = highscore
    }

}