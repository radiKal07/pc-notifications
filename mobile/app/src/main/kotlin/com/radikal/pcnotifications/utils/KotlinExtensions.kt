package com.radikal.pcnotifications.utils

import android.support.design.widget.Snackbar
import android.view.View

/**
 * Created by tudor on 27.12.2016.
 */
fun snackbar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
            .show()
}