package com.eduardo.facturacion.app.model

data class Invoice(
    val number: Int,
    val clientName: String,
    val date: String,
    val products: ArrayList<Product>
)