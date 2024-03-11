package com.curiozing.locationapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.MapFragment

@Composable
fun MapView(){
    Column {
        Text(text = "Map")
        MapView()
    }
}