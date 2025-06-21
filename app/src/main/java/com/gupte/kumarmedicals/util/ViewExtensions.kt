// Utility extensions for common View operations like show/hide
package com.gupte.kumarmedicals.util

import android.view.View

/**
 * Extension functions for View visibility handling
 */
object ViewExtensions {
    /**
     * Makes the view visible
     */
    fun View.show() {
        visibility = View.VISIBLE
    }

    /**
     * Hides the view
     */
    fun View.hide() {
        visibility = View.GONE
    }
} 