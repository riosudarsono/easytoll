package com.arman.easytoll.data.model

class HistoryData(
    var status: String,
    var price: String,
    var debit: Boolean) {

    constructor(): this("","", true)
}