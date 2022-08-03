package de.thm.sbwl47.rush_b_vanilla.model

class Block(var length : Int, var orientation : Orientations, var start: Position, var color : Colors, var type: String? = "default", var isStart: Boolean = false) {

    /**
     * Enumaration for the Orientation of the Block
     */
    enum class Orientations {
        VERTICAL,
        HORIZONTAL
    }

    /**
     * Enumaration for the possible Colors of cars
     */
    enum class Colors {
        RED, GREY
    }

    var end: Position
    init {
         // While Start is a parameter, End is calculated by adding the length onto start
        end = if(orientation == Orientations.HORIZONTAL) {
            Position(start.x + length-1, start.y)
        } else {
            Position(start.x, start.y + length-1)
        }
    }

    /**
     * @param p Position which you are looking for inside the block
     * @return Boolean wether `this` contains the position
     */
    fun contains(p: Position): Boolean {
        when(orientation) {
            Orientations.HORIZONTAL-> return start.y == p.y && start.x <= p.x && end.x >= p.x
            Orientations.VERTICAL -> return start.x == p.x && start.y <= p.y && end.y >= p.y
        }
    }

    /**
     * @param block Block which you are looking for overlaps inside `this`
     * @return boolean whether `this` overlaps with the block
     */
    fun contains(block : Block): Boolean {
        when(block.orientation) {
            Orientations.HORIZONTAL -> {
                for (x in block.start.x .. block.end.x) {
                    if (this.contains(Position(x, block.start.y))) {
                        return true
                    }
                }
                return false
            }

            Orientations.VERTICAL -> {
                for (y in block.start.y .. block.end.y) {
                    if (this.contains(Position(block.start.x, y))) {
                        return true
                    }
                }
                return false
            }
        }
    }

    /**
     * @params width, height check if the block is positioned inside the grid
     * @return boolean whether the block is inside
     */
    fun isValid(width: Int, height: Int) : Boolean{
        return (this.start.x >= 0 && this.start.y >= 0 && this.end.y < height && this.end.x < width)
    }
    

    override fun toString() : String {
        return "X: ${start.x} -> ${end.x} Y: ${start.y} -> ${end.y} Orientation: ${orientation} Length: ${length} color: ${color}"
    }
}

