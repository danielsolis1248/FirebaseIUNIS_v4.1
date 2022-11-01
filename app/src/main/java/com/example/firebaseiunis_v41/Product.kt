package com.example.firebaseiunis_v41

data class Product (var id:String?,
                    var name:String?,
                    var description:String?,
                    var imgUrl:String?,
                    var quantity: Int=0,
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
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (imgUrl?.hashCode() ?: 0)
        result = 31 * result + quantity
        result = 31 * result + precio.hashCode()
        return result
    }
}
