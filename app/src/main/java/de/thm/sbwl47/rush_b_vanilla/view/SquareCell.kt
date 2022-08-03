package de.thm.sbwl47.rush_b_vanilla.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.model.Position
import de.thm.sbwl47.rush_b_vanilla.model.Block
import de.thm.sbwl47.rush_b_vanilla.model.Cell

class SquareCell : View {
    private var cell: Cell? = null

    private val paint: Paint = Paint().apply {
        this.color = Color.WHITE
        this.style = Paint.Style.FILL
    }

    private val rect: Rect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * Measure our Screen
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    /**
     * Function that is being called if a cell is rendered invalid
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rect.left = 0
        rect.top = 0
        rect.bottom = height
        rect.right = width

        if (cell?.block?.color != null) {
            if(cell?.block?.color == Block.Colors.RED) {
                paint.color = Color.rgb(255,0,0)
            } else {
                paint.color = Color.rgb(102,102,102)
            }
        } else {
            paint.color = Color.rgb(102,102,102)
        }
        rect.left = 0
        rect.top = 0
        rect.bottom = height
        rect.right = width
        canvas.drawRect(rect, paint)
        var d: Drawable? = null
        val vehicles = mapOf(
                "police_car_back_horizontal" to R.drawable.police_car_back_horizontal,
                "police_car_back_vertical" to R.drawable.police_car_back_vertical,
                "police_car_front_horizontal" to R.drawable.police_car_front_horizontal,
                "police_car_front_vertical" to R.drawable.police_car_front_vertical,

                "taxi_car_back_horizontal" to R.drawable.taxi_car_back_horizontal,
                "taxi_car_back_vertical" to R.drawable.taxi_car_back_vertical,
                "taxi_car_front_horizontal" to R.drawable.taxi_car_front_horizontal,
                "taxi_car_front_vertical" to R.drawable.taxi_car_front_vertical,

                "blue_car_back_horizontal" to R.drawable.blue_car_back_horizontal,
                "blue_car_back_vertical" to R.drawable.blue_car_back_vertical,
                "blue_car_front_horizontal" to R.drawable.blue_car_front_horizontal,
                "blue_car_front_vertical" to R.drawable.blue_car_front_vertical,

                "brown_car_back_horizontal" to R.drawable.brown_car_back_horizontal,
                "brown_car_back_vertical" to R.drawable.brown_car_back_vertical,
                "brown_car_front_horizontal" to R.drawable.brown_car_front_horizontal,
                "brown_car_front_vertical" to R.drawable.brown_car_front_vertical,

                "green_car_back_horizontal" to R.drawable.green_car_back_horizontal,
                "green_car_back_vertical" to R.drawable.green_car_back_vertical,
                "green_car_front_horizontal" to R.drawable.green_car_front_horizontal,
                "green_car_front_vertical" to R.drawable.green_car_front_vertical,

                "lightgreen_car_back_horizontal" to R.drawable.lightgreen_car_back_horizontal,
                "lightgreen_car_back_vertical" to R.drawable.lightgreen_car_back_vertical,
                "lightgreen_car_front_horizontal" to R.drawable.lightgreen_car_front_horizontal,
                "lightgreen_car_front_vertical" to R.drawable.lightgreen_car_front_vertical,

                "orange_car_back_horizontal" to R.drawable.orange_car_back_horizontal,
                "orange_car_back_vertical" to R.drawable.orange_car_back_vertical,
                "orange_car_front_horizontal" to R.drawable.orange_car_front_horizontal,
                "orange_car_front_vertical" to R.drawable.orange_car_front_vertical,

                "red_car_back_horizontal" to R.drawable.red_car_back_horizontal,
                "red_car_back_vertical" to R.drawable.red_car_back_vertical,
                "red_car_front_horizontal" to R.drawable.red_car_front_horizontal,
                "red_car_front_vertical" to R.drawable.red_car_front_vertical,

                "white_car_back_horizontal" to R.drawable.white_car_back_horizontal,
                "white_car_back_vertical" to R.drawable.white_car_back_vertical,
                "white_car_front_horizontal" to R.drawable.white_car_front_horizontal,
                "white_car_front_vertical" to R.drawable.white_car_front_vertical,

                "white_truck_back_horizontal" to R.drawable.white_truck_back_horizontal,
                "white_truck_back_vertical" to R.drawable.white_truck_back_vertical,
                "white_truck_front_horizontal" to R.drawable.white_truck_front_horizontal,
                "white_truck_front_vertical" to R.drawable.white_truck_front_vertical,
                "white_truck_middle_horizontal" to R.drawable.white_truck_middle_horizontal,
                "white_truck_middle_vertical" to R.drawable.white_truck_middle_vertical,

                "yellow_bus_back_horizontal" to R.drawable.yellow_bus_back_horizontal,
                "yellow_bus_back_vertical" to R.drawable.yellow_bus_back_vertical,
                "yellow_bus_front_horizontal" to R.drawable.yellow_bus_front_horizontal,
                "yellow_bus_front_vertical" to R.drawable.yellow_bus_front_vertical,
                "yellow_bus_middle_horizontal" to R.drawable.yellow_bus_middle_horizontal,
                "yellow_bus_middle_vertical" to R.drawable.yellow_bus_middle_vertical
        )

        if(cell?.block?.length == 2) {
            if (cell?.block?.start?.equals(cell?.position) == true) {
                d = if(cell?.block?.orientation == Block.Orientations.HORIZONTAL){
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_back_horizontal"] ?: R.drawable.white_car_back_horizontal)
                } else{
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_front_vertical"] ?: R.drawable.white_car_front_vertical)
                }
            } else if(cell?.block?.end?.equals(cell?.position) == true) {
                d = if (cell?.block?.orientation == Block.Orientations.HORIZONTAL) {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_front_horizontal"] ?: R.drawable.white_car_front_horizontal)
                } else {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_back_vertical"] ?: R.drawable.white_car_back_vertical)
                }
            }
        } else if(cell?.block?.length == 3) {
            if (cell?.block?.start?.equals(cell?.position) == true) {
                d = if (cell?.block?.orientation == Block.Orientations.HORIZONTAL) {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_back_horizontal"] ?: R.drawable.white_truck_back_horizontal)
                } else {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_front_vertical"] ?: R.drawable.white_truck_front_vertical)
                }
            } else if (cell?.block?.end?.equals(cell?.position) == true) {
                d = if (cell?.block?.orientation == Block.Orientations.HORIZONTAL) {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_front_horizontal"] ?: R.drawable.white_truck_front_horizontal)
                } else {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_back_vertical"] ?: R.drawable.white_truck_back_vertical)
                }
            } else {
                d = if (cell?.block?.orientation == Block.Orientations.HORIZONTAL) {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_middle_horizontal"] ?: R.drawable.white_truck_middle_horizontal)
                } else {
                    ContextCompat.getDrawable(context, vehicles[cell?.block?.type + "_middle_vertical"] ?: R.drawable.white_truck_middle_vertical)
                }
            }
        }

        d?.setBounds(0, 0, width, height)
        d?.draw(canvas)

        // set finish line image
        if(cell?.position?.equals(Position(5,2)) == true){
            val finishLine: Drawable? = ContextCompat.getDrawable(context, R.drawable.finish)
            finishLine?.setBounds((width/1.5).toInt(), 0, width, height)
            finishLine?.alpha = 150
            finishLine?.draw(canvas)
        }
    }

    fun bind(cell: Cell) {
        this.cell = cell
        invalidate()
    }
}