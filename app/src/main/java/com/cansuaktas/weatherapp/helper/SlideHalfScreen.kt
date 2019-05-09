package com.cansuaktas.weatherapp.helper

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout

abstract class SlideHalfScreen : AppCompatActivity() {

    fun slideToHalf(motionLayout: MotionLayout) {

        if (motionLayout.progress == 0.5F) {
            return
        }
        ValueAnimator.ofFloat(motionLayout.progress, 0.5F).also {
            it.addUpdateListener { valueAnimator ->
                val progress = valueAnimator.animatedValue as Float
                motionLayout.progress = progress
            }
            it.duration = 200L
            it.interpolator = AccelerateInterpolator()
            it.start()
        }
    }
}
