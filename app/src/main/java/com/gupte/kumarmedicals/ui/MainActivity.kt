// Main entry point of the app, hosts the search fragment and manages the activity lifecycle
package com.gupte.kumarmedicals.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gupte.kumarmedicals.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }
}