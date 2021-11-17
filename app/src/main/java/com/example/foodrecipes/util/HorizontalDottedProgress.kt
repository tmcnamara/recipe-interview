package com.example.foodrecipes.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

class HorizontalDottedProgress : View {

    private val mDotRadius : Float = 5f
    private val mBounceDotRadius : Float = 8f

    //to get identified in which position dot has to bounce
    private var mDotPosition = 0

    //specify how many dots you need in a progressbar
    private val mDotAmount = 10

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs);

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr);

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.parseColor("#fd583f")
        createDot(canvas, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation()
    }

    private fun createDot(canvas: Canvas?, paint: Paint) {
        for(i in 0..mDotAmount){
            if(i == mDotPosition){
                canvas?.drawCircle(10f+(i*20), mBounceDotRadius, mBounceDotRadius, paint);
            }else {
                canvas?.drawCircle(10f+(i*20), mBounceDotRadius, mDotRadius, paint);
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //calculate the view width
        val calculatedWidth = (20*9);
        val width = calculatedWidth;
        val height : Float = (mBounceDotRadius*2);

        //MUST CALL THIS
        setMeasuredDimension(width, height.toInt());
    }

    private fun startAnimation() {
        val bounceAnimation = BounceAnimation()
        bounceAnimation.duration = 100
        bounceAnimation.repeatCount = Animation.INFINITE
        bounceAnimation.interpolator = LinearInterpolator()
        bounceAnimation.setAnimationListener(object: Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {
                mDotPosition++;
                //when mDotPosition == mDotAmount , then start again applying animation from 0th positon , i.e  mDotPosition = 0;
                if (mDotPosition == mDotAmount) {
                    mDotPosition = 0;
                }
            }
        })
        startAnimation(bounceAnimation);
    }

    inner class BounceAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t);
            //call invalidate to redraw your view againg.
            invalidate()
        }
    }

}