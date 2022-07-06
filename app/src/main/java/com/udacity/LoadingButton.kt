package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var rectColor = 0
    private var circleColor = 0

    private var prog = 0

    private var loadRect = Rect()

    private var rectF = RectF(0f, 0f, 80f, 80f)

    private var buttonText = ""


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color = Color.WHITE
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed)
    { p, old, new ->
        when (new) {
            is ButtonState.Clicked-> {}

            is ButtonState.Completed -> {
                // stop circle animation
                buttonText = context.getString(R.string.button_name)
                prog = 0
                valueAnimator.cancel()
                valueAnimator.setIntValues(0, 360)
                invalidate()
            }

            is ButtonState.Loading -> {
                // circle position and animation
                rectF.offset((width - 250).toFloat(), 40f)

                valueAnimator = ValueAnimator.ofInt(0,360).apply {
                    duration = 1800
                    addUpdateListener {
                        prog = it.animatedValue as Int

                        if (!downloadCompleted) {
                            repeatCount = ValueAnimator.INFINITE
                            repeatMode = ValueAnimator.RESTART
                        } else {
                            repeatCount = 0
                        }

                        invalidate()
                    }
                    start()
                }
                buttonText = context.getString(R.string.button_loading)
            }
        }
    }


    init {
        isClickable = true
        buttonState = ButtonState.Completed
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            rectColor = getColor(R.styleable.LoadingButton_rectColor, 0)
            circleColor = getColor((R.styleable.LoadingButton_circleColor), 0)
        }
    }

    override fun performClick(): Boolean {
        // if there is no radio button then only the super should be used
        if (!radioSelected){
           return super.performClick()
        }

        // animation should only run when radio button is selected
        super.performClick()
        buttonState = if (buttonState == ButtonState.Completed) {
            ButtonState.Loading

        } else ButtonState.Completed

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Draw rectangle and circle
        paint.color = rectColor
        loadRect.set(0, 0, (width * prog / 360), height)
        canvas?.drawRect(loadRect, paint)

        paint.color = circleColor
        canvas?.drawArc(rectF, 0f, prog.toFloat(), true, paint)

        // draw text
        paint.color = Color.WHITE
        canvas?.drawText(buttonText, width / 2.toFloat(), (heightSize + 20) / 2.toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}