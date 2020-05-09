package com.elijahverdoorn.mirrordisplay.component

import com.elijahverdoorn.mirrordisplay.data.manager.DataManager

interface Component {
    fun setManager(dataManager: DataManager)
    suspend fun update()
}