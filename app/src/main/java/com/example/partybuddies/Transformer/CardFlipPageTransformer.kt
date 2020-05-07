package com.example.partybuddies.Transformer

import android.view.View
import androidx.viewpager.widget.ViewPager

class CardFlipPageTransformer : ViewPager.PageTransformer {

    var isScalable = true
    /**
     * Sets the Flip Orientation. Can be either CardFlipPageTransformer.HORIZONTAL or CardFlipPageTransformer.VERTICAL
     * @param flipOrientation Can be either CardFlipPageTransformer.HORIZONTAL or CardFlipPageTransformer.VERTICAL
     */
    var flipOrientation = VERTICAL
        set(flipOrientation) {
            field = if (flipOrientation > 1) VERTICAL else HORIZONTAL
        }

    override fun transformPage(page: View, position: Float) {
        val percentage = 1 - Math.abs(position)
        page.cameraDistance = 12000f
        setVisibility(page, position)
        setTranslation(page)
        setSize(page, position, percentage)
        setRotation(page, position, percentage)
    }

    private fun setVisibility(page: View, position: Float) {
        if (position < 0.5 && position > -0.5) {
            page.visibility = View.VISIBLE
        } else {
            page.visibility = View.INVISIBLE
        }
    }

    private fun setTranslation(page: View) {
        val viewPager = page.parent as ViewPager
        val scroll = viewPager.getScrollX() - page.left
        page.translationX = scroll.toFloat()
    }

    private fun setSize(page: View, position: Float, percentage: Float) {
        // Do nothing, if its not scalable
        if (!isScalable) return

        page.setScaleX(if (position != 0f && position != 1f) percentage else 1f)
        page.setScaleY(if (position != 0f && position != 1f) percentage else 1f)
    }

    private fun setRotation(page: View, position: Float, percentage: Float) {
        if (this.flipOrientation == VERTICAL) {
            if (position > 0) {
                page.rotationY = -180 * (percentage + 1)
            } else {
                page.rotationY = 180 * (percentage + 1)
            }
        } else {
            if (position > 0) {
                page.rotationX = -180 * (percentage + 1)
            } else {
                page.rotationX = 180 * (percentage + 1)
            }
        }
    }

    companion object {
        val HORIZONTAL = 1
        val VERTICAL = 2
    }
}
