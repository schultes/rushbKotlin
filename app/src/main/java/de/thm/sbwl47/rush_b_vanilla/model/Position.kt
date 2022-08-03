package de.thm.sbwl47.rush_b_vanilla.model

class Position(var x : Int, var y : Int) {
    /**
     * @return String of Object
     */
    override fun toString(): String {
        return "{x: ${x}, y: ${y} }"
    }

    /**
     *  @param pos Position you want the difference to
     *  @return Position with the difference between `this` and `pos`
     */
    fun minus(pos: Position): Position {
        return Position(this.x - pos.x, this.y - pos.y)
    }

    /**
     * @param other the opposite Position you want to check the equality
     * @return Boolean whether this Position is the same as other
     */
    fun equals(other: Position?): Boolean {
        other?.let { position ->
            return other.x == this.x && other.y == this.y
        }
        return false
    }

    /**
     * @param other target for comparation
     * @return positive, if is larger, negative, if is smaller, and 0 if the same
     */
    operator fun compareTo(other: Position): Int {
        val diff: Position = other.minus(this)
        if (diff.x > 0 || diff.y > 0) {
            return 1
        }
        else if (diff.x < 0 || diff.y < 0) {
            return -1
        }
        else {
            return 0;
        }
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}