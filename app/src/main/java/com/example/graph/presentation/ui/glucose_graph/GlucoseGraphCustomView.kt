package com.example.graph.presentation.ui.glucose_graph

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.applyCanvas
import com.example.graph.R
import com.example.graph.presentation.ui.glucose_graph.model.DateType
import com.example.graph.presentation.ui.glucose_graph.model.GraphAttr
import com.example.graph.presentation.ui.glucose_graph.model.GraphData
import java.util.Random
import kotlin.math.max


class GlucoseGraphCustomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?
) : View(
    context,
    attributeSet
) {
    var graphData: GraphData? = null
        set(value) {
            field = value
            updateUISize()
            requestLayout()
            invalidate()
        }
    private var graphAttr: GraphAttr? = null

    private var aboveLine = 0f
    private var belowLine = 0f

    private val graphPath = Path()
    private val clipPath = Path()

    private val fieldRect = RectF()

    private val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E2E2E2")
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            2f,
            resources.displayMetrics
        )
    }

    private val gridAboveNormalPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            resources.displayMetrics
        )
        alpha = (0.25 * 255).toInt()
    }

    private val gridBelowNormalPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF005C")
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            resources.displayMetrics
        )
        alpha = (0.25 * 255).toInt()
    }

    private val lineGraphPaint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            14f,
            resources.displayMetrics
        )
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            2f,
            resources.displayMetrics
        )
    }
    private val graphPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#1F849A")
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            resources.displayMetrics
        )
        pathEffect = CornerPathEffect(ROUND_LINE_VALUE)
    }

    private val stichesPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1F849A")
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            resources.displayMetrics
        )
    }

    private val graphPaintAbove: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#ECB800")
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            resources.displayMetrics
        )
        pathEffect = CornerPathEffect(ROUND_LINE_VALUE)
    }

    private val graphPaintBelow: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#FF005C")
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            resources.displayMetrics
        )
        pathEffect = CornerPathEffect(ROUND_LINE_VALUE)
    }

    private var glucoseRoundedValue: Int = 0
        get() {
            val max = graphData?.glucoseRange?.max() ?: 0
            val min = graphData?.glucoseRange?.min() ?: 0
            return max - min
        }

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet)
        }
        else {
            initDefaultAttributes()
        }
        if (isInEditMode) {
            graphData = GraphData(
                glucoseValue = listOf(
                    55,
                    100,
                    122,
                    210,
                    144,
                    190,
                    40,
                    133,
                    80,
                    120,
                    90,
                    99,
                    70,
                    40,
                    230,
                    250,
                    100,
                ).shuffled(Random(15)),
                glucoseRange = (40..250 step 30).toList(),
                dateRange = listOf(
                    "12 am",
                    "6 am",
                    "12 pm",
                    "6 pm",
                    "12 pm",
                ),
//                dateRange = listOf(
//                    "Sat",
//                    "Sum",
//                    "Mon",
//                    "Tue",
//                    "Wed",
//                    "Thu",
//                    "Fri",
//                    ),
                aboveNormal = 190,
                belowNormal = 70,
                dateType = DateType.Last24
            )
        }
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(
            w,
            h,
            oldw,
            oldh
        )
        updateUISize()
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val minWidth = suggestedMinimumWidth + paddingStart + paddingEnd
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCellSizeInPixel = TypedValue
            .applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DESIRED_SIZE,
                resources.displayMetrics
            )
            .toInt()

        val rows = graphData!!.glucoseValue.size ?: 0
        val columns = graphData!!.glucoseRange.size ?: 0

        val desiredWidth = max(
            columns * desiredCellSizeInPixel + paddingEnd + paddingStart,
            minWidth
        )
        val desiredHeight = max(
            rows * desiredCellSizeInPixel + paddingTop + paddingBottom,
            minHeight
        )

        setMeasuredDimension(
            resolveSize(
                desiredWidth,
                widthMeasureSpec
            ),
            resolveSize(
                desiredHeight,
                heightMeasureSpec
            )
        )
    }

    private fun updateUISize() {
        val field = this.graphData ?: return

        val safeWidth = width - paddingStart - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        val cellWidth = safeWidth / field.glucoseValue.size.toFloat()
        val cellHeight = safeHeight / field.glucoseRange.size.toFloat()

        val fieldWidth = cellWidth * field.glucoseValue.size.toFloat()
        val fieldHeight = cellHeight * field.glucoseRange.size.toFloat()

        fieldRect.left = paddingStart + (safeWidth - fieldWidth) / 2
        fieldRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRect.right = fieldRect.left + fieldWidth
        fieldRect.bottom = fieldRect.top + fieldHeight
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (graphData == null) return
        drawBackground(canvas)
        drawHorizontalLines(canvas)
        drawStiches(canvas, belowLine, fieldRect.bottom, gridBelowNormalPaint, 25)
        drawStiches(canvas, 0f, aboveLine, gridAboveNormalPaint, 25)
        drawVerticalGrid(canvas)
//        drawText(canvas)
        drawGraph(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    private fun drawStiches(
        canvas: Canvas,
        startY: Float,
        endY: Float,
        paint: Paint,
        step : Int
    ) {
        val background = Bitmap.createBitmap(
            width,
            endY.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val lineCanvas = Canvas(background)

        var moveToX = 0f
        var moveToBX = -width

        while (moveToX < (width + width / 2)) {
            lineCanvas.drawLine(moveToX, 0f, moveToBX.toFloat(), height.toFloat(), paint)
            moveToX += step
            moveToBX += step
        }
        canvas.drawBitmap(background, 0f, startY, null)
    }

    private fun drawVerticalGrid(canvas: Canvas) {
        val xStart = fieldRect.left
        val step = fieldRect.width() / graphData!!.dateRange.size.toFloat()
        for (i in 0 until graphData!!.dateRange.size) {
            val x = xStart + step * i
            canvas.drawLine(
                x,
                fieldRect.top,
                x,
                fieldRect.bottom,
                gridPaint
            )
        }

    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val data = graphData ?: return
        val coef = fieldRect.bottom / glucoseRoundedValue
        val y1 = fieldRect.bottom - data.aboveNormal * coef + data.glucoseRange.min() * coef
        val y2 = fieldRect.bottom - data.belowNormal * coef + data.glucoseRange.min() * coef
        aboveLine = y1
        belowLine = y2
        canvas.drawLine(
            fieldRect.left,
            y1,
            width.toFloat(),
            y1,
            gridPaint
        )
        canvas.drawLine(
            fieldRect.left,
            y2,
            width.toFloat(),
            y2,
            gridPaint
        )
    }

    private fun drawGraph(canvas: Canvas) {
        val data = graphData ?: return
        val xStart = fieldRect.left
        val coef = fieldRect.bottom / (glucoseRoundedValue)
        val stepSizeX = fieldRect.right / data.glucoseValue.size
        val glucoseList = graphData!!.glucoseValue
        for (i in glucoseList.indices) {
            val x = xStart + stepSizeX * i
            val y = fieldRect.bottom - glucoseList[i] * coef + data.glucoseRange.min() * coef
            when (i) {
                0 -> {
                    graphPath.moveTo(
                        xStart,
                        y
                    )
                }

                else -> {
                    when {
                        (glucoseList[i - 1] < glucoseList[i]) -> {
                            graphPath.lineTo(
                                x,
                                y - ROUND_LINE_VALUE / 2
                            )
                        }

                        (glucoseList[i - 1] > glucoseList[i]) -> {
                            graphPath.lineTo(
                                x,
                                y + ROUND_LINE_VALUE / 2
                            )
                        }

                        else -> {
                            graphPath.lineTo(
                                x,
                                y
                            )
                        }
                    }
                }
            }
        }
        canvas.drawPath(
            graphPath,
            graphPaint
        )
        clipPath.addRect(
            0f,
            0f,
            width.toFloat(),
            aboveLine,
            Path.Direction.CW
        )
        canvas.clipPath(clipPath)
        canvas.drawPath(
            graphPath,
            graphPaintAbove
        )
        clipPath.reset()
        clipPath.addRect(
            0f,
            belowLine,
            width.toFloat(),
            height.toFloat(),
            Path.Direction.CW
        )
        canvas.clipPath(clipPath)
        canvas.drawPath(
            graphPath,
            graphPaintBelow
        )
    }

    private fun drawText(canvas: Canvas) {
        val data = graphData ?: return
        val coef = fieldRect.bottom / (data.glucoseRange.size - 1)
        for (i in 0 until data.glucoseRange.size) {
            val fontMetrics = lineGraphPaint2.fontMetrics
            val textHeight = fontMetrics.descent - fontMetrics.ascent
            val y = fieldRect.bottom - coef * i + (textHeight * (i - 2)) / 4
            canvas.drawText(
                data.glucoseRange[i].toString(),
                20f,
                y,
                lineGraphPaint2
            )
        }
    }

    private fun initAttributes(
        attributeSet: AttributeSet?,
    ) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.GraphCustomView,
        )

        graphAttr = GraphAttr(
            textColor = typedArray.getColor(
                R.styleable.GraphCustomView_textColor,
                TEXT_COLOR_DEFAULT
            ),
            lineGraphColor = typedArray.getColor(
                R.styleable.GraphCustomView_lineGraphColor,
                LINE_GRAPH_COLOR_DEFAULT
            ),
            lineGraphStrokeWidth = typedArray.getDimension(
                R.styleable.GraphCustomView_lineGraphStrokeWidth,
                LINE_GRAPH_STROKE_WIDTH_DEFAULT
            ),
            textSize = typedArray.getDimension(
                R.styleable.GraphCustomView_textSize,
                TEXT_SIZE_DEFAULT
            ),
            gridColor = typedArray.getColor(
                R.styleable.GraphCustomView_gridColor,
                GRID_COLOR_COLOR_DEFAULT
            )
        )

        typedArray.recycle()
    }

    private fun initDefaultAttributes() {
        graphAttr = GraphAttr(
            textColor = TEXT_COLOR_DEFAULT,
            lineGraphColor = LINE_GRAPH_COLOR_DEFAULT,
            lineGraphStrokeWidth = LINE_GRAPH_STROKE_WIDTH_DEFAULT,
            textSize = TEXT_SIZE_DEFAULT,
            gridColor = GRID_COLOR_COLOR_DEFAULT,
        )
    }

    companion object {
        private const val TEXT_COLOR_DEFAULT = Color.BLACK
        private const val LINE_GRAPH_COLOR_DEFAULT = Color.GRAY
        private const val GRID_COLOR_COLOR_DEFAULT = Color.GRAY
        private const val LINE_GRAPH_STROKE_WIDTH_DEFAULT = 1f
        private const val TEXT_SIZE_DEFAULT = 16f
        private const val DESIRED_SIZE = 50f
        private const val ROUND_LINE_VALUE = 150f

    }
}