package de.thm.sbwl47.rush_b_vanilla.model

data class Cell(
        var id: Int,
        var position: Position,
        var block: Block?,
        var offset: Int
)

