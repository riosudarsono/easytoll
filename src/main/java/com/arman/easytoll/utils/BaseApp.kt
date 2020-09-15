package com.arman.easytoll.utils

class BaseApp(private val listener: Listener) {
    fun set(){
        listener.getIntentData()
        listener.setOnClick()
        listener.setAdapter()
        listener.setContent()
        listener.loadData()}
    interface Listener {
        fun getIntentData()
        fun setOnClick()
        fun setAdapter()
        fun setContent()
        fun loadData()
    }

}