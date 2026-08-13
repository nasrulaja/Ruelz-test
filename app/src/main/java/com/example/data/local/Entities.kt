package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val originalPrice: Double? = null,
    val unit: String,
    val description: String,
    val stock: Int,
    val isPromo: Boolean = false,
    val promoLabel: String? = null,
    val rating: Float = 4.8f,
    val reviewCount: Int = 24,
    val drawableName: String? = null
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val quantity: Int,
    val selected: Boolean = true
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val date: String,
    val status: String, // "Baru", "Dikemas", "Dikirim", "Selesai", "Ditolak"
    val customerName: String,
    val address: String,
    val phone: String,
    val itemsSummary: String, // e.g. "Beras Pandan Wangi 5kg (x1), Minyak Goreng 1L (x2)"
    val totalAmount: Double,
    val timestamp: Long
)

@Entity(tableName = "vouchers")
data class VoucherEntity(
    @PrimaryKey val code: String,
    val title: String,
    val description: String,
    val discountAmount: Double,
    val minSpend: Double,
    val expiryDate: String,
    val status: String = "Aktif", // "Aktif", "Terpakai", "Kadaluarsa"
    val daysLeftText: String? = null
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis()
)
