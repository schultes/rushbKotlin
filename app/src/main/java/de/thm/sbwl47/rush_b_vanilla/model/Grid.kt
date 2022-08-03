package de.thm.sbwl47.rush_b_vanilla.model

import de.thm.tp.library.json.TPJSONArray
import de.thm.tp.library.json.TPJSONObject
import de.thm.tp.library.random.TPRandom

class Grid {
    var width = 6
    var height = 6

    val blockList: MutableList<Block> = mutableListOf<Block>()
    var fixedBlockList: MutableList<Block> = mutableListOf<Block>()

    fun generate(width: Int?, height: Int?, json: String, level: Int = 0) {
        // if (width != null) this.width = width
        width?.let { widthobject -> this.width = widthobject }
        //if (height != null) this.height = height
        height?.let { heightObject -> this.height = heightObject }
        if (level > 0) {
            loadLevel(json, level)
            blockList.forEach { block ->
                fixedBlockList.add(block)
            }
        } else {
            blockList.clear()

            //blockList.addAll(createGrid())
            val tmpList: List<Block> = createGrid()
            for (x in 0 until tmpList.size) {
                blockList += tmpList[x]
            }
            blockList.forEach { block ->
                fixedBlockList.add(block)
            }
        }
    }

    private fun loadLevel(json: String, level: Int) {
        // get JSONObject from JSON file
        val obj = TPJSONObject(json)

        // fetch JSONArray named levels
        val levelArray: TPJSONArray? = obj.getJSONArray("levels")
        val levelObject = levelArray?.getJSONObject(level - 1)

        // generate Blocks
        val blockArray: TPJSONArray? = levelObject?.getJSONArray("blocks")

        blockArray?.let { array ->
            for (i in 0 until array.length()) {
                val block = array.getJSONObject(i)
                block?.let { blockObject ->
                    val blockLength = blockObject.getInt("length")
                    val blockOrientation = blockObject.getString("orientation")
                    val blockX = blockObject.getInt("x")
                    val blockY = blockObject.getInt("y")
                    val blockType = blockObject.getString("type")
                    var newColor = Block.Colors.GREY

                    var isStart = false
                    if (blockObject.has("start")) {
                        if (blockObject.getBoolean("start") == true) {
                            newColor = Block.Colors.RED
                            isStart = true
                        }
                    }

                    var newOrientation = Block.Orientations.VERTICAL
                    if (blockOrientation == "HORIZONTAL") {
                        newOrientation = Block.Orientations.HORIZONTAL
                    }

                    blockList.add(
                        Block(
                            blockLength!!,
                            newOrientation,
                            Position(blockX!!, blockY!!),
                            newColor,
                            blockType,
                            isStart
                        )
                    )
                }
            }
        }
    }

    /**
     * Generate a new List of Blocks and test it for solvability
     * @return List<Block> a list of Blocks
     */
    fun createGrid(): List<Block> {
        val limit = 20

        // Startblock
        val list = mutableListOf<Block>()
        val cars = arrayOf(
            "blue_car",
            "brown_car",
            "green_car",
            "lightgreen_car",
            "orange_car",
            "red_car",
            "taxi_car",
            "white_car"
        )
        val trucks = arrayOf("white_truck", "yellow_bus")

        // Startblock
        val start = Block(
            2,
            Block.Orientations.HORIZONTAL,
            Position(width - 2, 2),
            Block.Colors.RED,
            "police_car",
            true
        )
        list.add(start)

        val blockCount = TPRandom.int(7, 15)
        for (i in 0..blockCount) {
            val value_1 = (blockCount.toDouble() / 100 * 60).toInt()
            val value_2 = (blockCount.toDouble() / 100 * 30).toInt()
            val sizeDistribution = intArrayOf(
                0,
                0,
                value_1,
                value_2,
                1
            )

            // Select a more or less unique color for each block (can be extended for true uniqueness)
            val color = Block.Colors.GREY

            // Select a Random Orientation
            val allOrientations = listOf(Block.Orientations.HORIZONTAL, Block.Orientations.VERTICAL)
            val orientation = allOrientations[TPRandom.int(0, allOrientations.size)]

            // Generate a Size that isnt used more than allowed (according to `sizeDistribution`)
            var size = 0
            do {
                when (orientation) {
                    Block.Orientations.HORIZONTAL -> {
                        size = TPRandom.int(2, 4)
                    }
                    Block.Orientations.VERTICAL -> {
                        size = TPRandom.int(2, 3)
                    }
                }
            } while (sizeDistribution[size] < 1)
            // sizeDistribution[size]-- // Line has no use

            var position: Position  // Current Position of the block

            var counter = 0
            var block: Block
            var tmpx = 0;
            var tmpbool = false;

            do {
                when (orientation) {
                    Block.Orientations.HORIZONTAL -> {
                        do {
                            position =
                                Position(TPRandom.int(0, width - size), TPRandom.int(0, height))
                            counter++
                        } while (position.y == start.start.y)
                    }
                    Block.Orientations.VERTICAL -> {
                        position = Position(TPRandom.int(0, width), TPRandom.int(0, height - size))
                    }
                }
                if (size == 2) {
                    val type = cars[TPRandom.int(0, cars.size)]
                    block = Block(size, orientation, position, color, type)
                } else {
                    val type = trucks[TPRandom.int(0, trucks.size)]
                    block = Block(size, orientation, position, color, type)
                }
                counter++
                while (tmpx < list.size) {
                    if (list[tmpx].contains(block)) {
                        tmpbool = true;
                    }
                    tmpx++;

                }
                //list.any { it.contains(block) }
            } while ((tmpbool || !block.isValid(width, height)) && counter < limit)
            if (counter < limit) {
                list.add(block)
            }
        }
        scramble(list)

        // Return the List if its valid, create a new one if its not
        return if (isValid(list)) list else createGrid()
    }

    /**
     * @param list input list of blocks to check validity
     * @return Boolean of validity
     */
    fun isValid(list: List<Block>): Boolean {
        var valid = false
        val start = list[0]

        if (start.end.x > 2) {
            return false
        }

        for (x in start.start.x..width) {
            val pos = Position(x, start.start.y)

            var tmpx = 1;
            var tmpbool = false;
            while (tmpx < list.size) {

                if (list[tmpx].contains(pos)) {
                    tmpbool = true;
                }
                tmpx++;

            }
            //if ( list.any { if(it == list[0]) { false }  else (it.contains(pos))}) {
            if (tmpbool) {
                valid = true
            }
        }
        return valid
    }

    /**
     * @param list input list of blocks to randomize
     */
    private fun scramble(list: List<Block>) {
        for (i in 0..1000) {
            val block = list[TPRandom.int(0, list.size)]
            move(block, TPRandom.boolean(), list)
        }
    }

    /**
     * @param block The block that is supposed to be moved
     * @param direction true if moving forward; false if moving backward
     * @param list the list you want to move the block on (for block generation) blockList by default
     * @return if block has been moved
     */
    fun move(block: Block, direction: Boolean, list: List<Block>? = null): Boolean {
        var newList: List<Block> = blockList
        list?.let { obj ->
            newList = obj
        }
        var positionChanged = false
        val blockStartPosX: Int = block.start.x
        val blockStartPosY: Int = block.start.y

        when (block.orientation) {
            Block.Orientations.HORIZONTAL -> {
                when (direction) {
                    true -> {
                        var tmpx = 0;
                        var tmpbool = false;
                        while (tmpx < newList.size) {
                            if (newList[tmpx].contains(Position(block.end.x + 1, block.start.y))) {
                                tmpbool = true;
                            }
                            tmpx++;

                        }
                        if (!tmpbool && block.end.x + 1 < width) {
                            //if(list.none { it.contains(Position(block.end.x+1, block.start.y))} && block.end.x+1 < width ) {
                            block.start.x++
                            block.end.x++
                        }
                    }

                    false -> {
                        var tmpx = 0;
                        var tmpbool = false;
                        while (tmpx < newList.size) {
                            if (newList[tmpx].contains(Position(block.start.x - 1, block.start.y))) {
                                tmpbool = true;
                            }
                            tmpx++;

                        }
                        if (!tmpbool && block.start.x > 0) {
                            //if(list.none { it.contains(Position(block.start.x-1, block.start.y))} && block.start.x > 0) {
                            block.start.x--
                            block.end.x--
                        }
                    }
                }
            }
            Block.Orientations.VERTICAL -> {
                when (direction) {
                    true -> {
                        var tmpx = 0;
                        var tmpbool = false;
                        while (tmpx < newList.size) {
                            if (newList[tmpx].contains(Position(block.end.x, block.end.y + 1))) {
                                tmpbool = true;
                            }
                            tmpx++;

                        }
                        if (!tmpbool && block.end.y + 1 < height) {
                            //if(list.none {it.contains(Position(block.end.x, block.end.y+1))} && block.end.y+1 < height ) {
                            block.start.y++
                            block.end.y++
                        }
                    }
                    false -> {
                        var tmpx = 0;
                        var tmpbool = false;
                        while (tmpx < newList.size) {
                            if (newList[tmpx].contains(Position(block.start.x, block.start.y - 1))) {
                                tmpbool = true;
                            }
                            tmpx++;

                        }
                        if (!tmpbool && block.start.y > 0) {
                            //if(list.none { it.contains(Position(block.start.x, block.start.y-1))} && block.start.y > 0) {
                            block.start.y--
                            block.end.y--
                        }
                    }
                }
            }
        }
        if (blockStartPosX != block.start.x || blockStartPosY != block.start.y) {
            positionChanged = true
        }

        return positionChanged
    }
}