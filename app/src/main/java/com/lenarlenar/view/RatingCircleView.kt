package com.lenarlenar.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lenarlenar.ratingviewsample.R

class RatingCircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circleBorder = 6f
    private var ratingValue = 9f
    private var ratingName: String = "RATE"
    private var textVerticalDistance = 20
    private var circleSize = 150

    private val rectF: RectF = RectF()
    private val ratingMax = 100f

    private val basisCirclePainter = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val ratingCirclePainter = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val textPainter = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 40f
        isAntiAlias = true
    }

    init {

        if (attrs != null) {

            val attrsTypedArray = context.obtainStyledAttributes(
                attrs
                , R.styleable.RatingCircleView
                , defStyleAttr
                , 0
            )

            circleBorder = attrsTypedArray.getDimension(R.styleable.RatingCircleView_circleBorder, circleBorder)

            val strokeWidth = attrsTypedArray.getDimension(R.styleable.RatingCircleView_circleThickness,basisCirclePainter.strokeWidth)
            basisCirclePainter.strokeWidth = strokeWidth
            ratingCirclePainter.strokeWidth = strokeWidth

            basisCirclePainter.color = attrsTypedArray.getColor(R.styleable.RatingCircleView_basisCircleColor, basisCirclePainter.color)
            ratingCirclePainter.color = attrsTypedArray.getColor(R.styleable.RatingCircleView_ratingCircleColor, ratingCirclePainter.color)
            textPainter.color = attrsTypedArray.getColor(R.styleable.RatingCircleView_textColor, textPainter.color)
            ratingValue = attrsTypedArray.getFloat(R.styleable.RatingCircleView_rating, ratingValue)

            checkRatingRange(ratingValue.toDouble())

            ratingName = attrsTypedArray.getString(R.styleable.RatingCircleView_ratingName) ?: ratingName
            textPainter.textSize = attrsTypedArray.getDimension(R.styleable.RatingCircleView_textSize, textPainter.textSize)
            circleSize = attrsTypedArray.getDimension(R.styleable.RatingCircleView_circleSize, circleSize.toFloat()).toInt()
            textVerticalDistance = attrsTypedArray.getDimension(R.styleable.RatingCircleView_textVerticalDistance, textVerticalDistance.toFloat()).toInt()
        }
    }

    /**
     * <p>This method set rating value and name</p>
     *
     * @param rating must be from 0 to 10.
     * @param name just name of rating.
     */
    fun setRating(rating: Double, name: String) {

        checkRatingRange(rating)
        ratingValue = rating.toFloat()
        ratingName = name
        invalidate()
    }

    private fun checkRatingRange(rating: Double) {
        if (rating < 0 || rating > 10)
            throw IllegalArgumentException("Rating must be from 0 to 10")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(circleSize, circleSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val minWidthAndHeight = Math.min(width, height).toFloat()

        rectF.set(circleBorder, circleBorder, minWidthAndHeight - circleBorder, minWidthAndHeight - circleBorder)
        rectF.offset((width - minWidthAndHeight), ((height - minWidthAndHeight) / 2))

        canvas.drawArc(
            rectF
            , 360.0f
            , 360.0f
            , false
            , basisCirclePainter
        )

        canvas.drawArc(
            rectF
            , -90.0f
            , (360f * 10 * ratingValue / ratingMax)
            , false
            , ratingCirclePainter
        )

        val rating = String.format("%.1f", ratingValue)
        val originOfTheRatingValueTextX = (width - textPainter.measureText(rating)) / 2f

        val baselineOfTheTextY = (height - textPainter.ascent() - textPainter.descent()) / 2f

        canvas.drawText(
            rating
            , originOfTheRatingValueTextX
            , baselineOfTheTextY - textVerticalDistance
            , this.textPainter
        )

        val originOfTheRatingNameX = (width - textPainter.measureText(ratingName)) / 2.0f

        canvas.drawText(
            ratingName
            , originOfTheRatingNameX
            , baselineOfTheTextY + textVerticalDistance
            , this.textPainter
        )
    }
}