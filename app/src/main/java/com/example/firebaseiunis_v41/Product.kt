package com.example.firebaseiunis_v41

data class Product(
    var id:String?,
    var name:String?,
    var description:String? = null,
    var imgUrl:String? = null,
    var quantity: String? = null ,
    var precio:Double=0.0

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (imgUrl != other.imgUrl) return false
        if (quantity != other.quantity) return false
        if (precio != other.precio) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
