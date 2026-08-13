package com.example.data

import com.example.data.local.AppDatabase
import com.example.data.local.CartItemEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.VoucherEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CartItemWithProduct(
    val cartItem: CartItemEntity,
    val product: ProductEntity
)

class BerkahMartRepository(private val db: AppDatabase) {

    val allProducts: Flow<List<ProductEntity>> = db.productDao().getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = db.cartDao().getCartItems()
    val allOrders: Flow<List<OrderEntity>> = db.orderDao().getAllOrders()
    val allVouchers: Flow<List<VoucherEntity>> = db.voucherDao().getAllVouchers()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingProducts = db.productDao().getAllProducts().first()
        if (existingProducts.isEmpty()) {
            db.productDao().insertProducts(
                listOf(
                    ProductEntity(
                        id = "p1",
                        name = "Bayam Hijau Segar Ikatan Besar",
                        category = "Sayuran",
                        price = 4500.0,
                        originalPrice = 5000.0,
                        unit = "250g / pack",
                        description = "Bayam hijau segar dipetik langsung dari kebun lokal Blok 21. Tinggi zat besi dan nutrisi harian keluarga.",
                        stock = 30,
                        isPromo = true,
                        promoLabel = "-10%",
                        rating = 4.8f,
                        reviewCount = 24
                    ),
                    ProductEntity(
                        id = "p2",
                        name = "Minyak Goreng Sunco 2L",
                        category = "Sembako",
                        price = 34500.0,
                        originalPrice = 38000.0,
                        unit = "2 Liter",
                        description = "Minyak goreng kelapa sawit bening, murni, tidak mudah beku, cocok untuk gorengan renyah.",
                        stock = 25,
                        isPromo = true,
                        promoLabel = "Spesial 20%",
                        rating = 4.9f,
                        reviewCount = 42
                    ),
                    ProductEntity(
                        id = "p3",
                        name = "Apel Fuji Premium Segar",
                        category = "Buah",
                        price = 45000.0,
                        unit = "1 kg",
                        description = "Apel Fuji pilihan rasa manis renyah dengan kandungan air melimpah. Cocok untuk cemilan sehat.",
                        stock = 18,
                        rating = 4.7f,
                        reviewCount = 15
                    ),
                    ProductEntity(
                        id = "p4",
                        name = "Telur Ayam Negeri Organik",
                        category = "Sembako",
                        price = 28000.0,
                        originalPrice = 32000.0,
                        unit = "1 kg (16 Butir)",
                        description = "Telur ayam negeri berkualitas tinggi dari peternakan Blok 21. Kaya protein & omega 3.",
                        stock = 40,
                        isPromo = true,
                        promoLabel = "Promo",
                        rating = 4.9f,
                        reviewCount = 56
                    ),
                    ProductEntity(
                        id = "p5",
                        name = "Beras Pandan Wangi 5kg",
                        category = "Sembako",
                        price = 65000.0,
                        unit = "5 kg",
                        description = "Beras putih pulen bermutu tinggi dengan wangi pandan alami. Bebas pemutih dan pemutih kimia.",
                        stock = 24,
                        rating = 5.0f,
                        reviewCount = 38
                    ),
                    ProductEntity(
                        id = "p6",
                        name = "Pisang Cavendish Sunpride",
                        category = "Buah",
                        price = 18000.0,
                        unit = "500g / sisir",
                        description = "Pisang Cavendish mulus tanpa cacat, rasa manis dan tekstur padat bergizi.",
                        stock = 20,
                        rating = 4.8f,
                        reviewCount = 19
                    ),
                    ProductEntity(
                        id = "p7",
                        name = "Roti Gandum Utuh Fresh Bakery",
                        category = "Kebutuhan Rumah",
                        price = 24000.0,
                        unit = "1 Loaf (400g)",
                        description = "Roti olahan gandum utuh buatan rumah segar setiap pagi, tinggi serat.",
                        stock = 15,
                        rating = 4.6f,
                        reviewCount = 11
                    ),
                    ProductEntity(
                        id = "p8",
                        name = "Susu Murni Lokal 1L",
                        category = "Minuman",
                        price = 18500.0,
                        unit = "1 Liter",
                        description = "Susu sapi segar hasil pasterisasi tanpa bahan pengawet.",
                        stock = 12,
                        rating = 4.9f,
                        reviewCount = 27
                    ),
                    ProductEntity(
                        id = "p9",
                        name = "Paket Sayur Sop Segar Organik",
                        category = "Sayuran",
                        price = 12500.0,
                        originalPrice = 15000.0,
                        unit = "250g / pack",
                        description = "Racikan lengkap sayur sop: wortel, buncis, kentang, kol, daun bawang, dan seledri.",
                        stock = 35,
                        isPromo = true,
                        rating = 4.8f,
                        reviewCount = 31
                    )
                )
            )
        }

        val existingVouchers = db.voucherDao().getAllVouchers().first()
        if (existingVouchers.isEmpty()) {
            db.voucherDao().insertVouchers(
                listOf(
                    VoucherEntity(
                        code = "HEMAT20K",
                        title = "Potongan Rp 20.000",
                        description = "Minimal belanja Rp 100.000. Berlaku untuk semua produk segar dan kebutuhan pokok.",
                        discountAmount = 20000.0,
                        minSpend = 100000.0,
                        expiryDate = "25 Nov 2023",
                        status = "Aktif",
                        daysLeftText = "3 hari lagi"
                    ),
                    VoucherEntity(
                        code = "GRATISONGKIR",
                        title = "Gratis Ongkir s/d 15rb",
                        description = "Minimal belanja Rp 50.000. Khusus pengiriman instan area Blok 21.",
                        discountAmount = 15000.0,
                        minSpend = 50000.0,
                        expiryDate = "30 Nov 2023",
                        status = "Aktif"
                    ),
                    VoucherEntity(
                        code = "BERKAHNEW",
                        title = "Diskon 10% Pengguna Baru",
                        description = "Maksimal potongan Rp 50.000. Hanya untuk transaksi pertama.",
                        discountAmount = 10000.0,
                        minSpend = 0.0,
                        expiryDate = "31 Des 2023",
                        status = "Aktif"
                    )
                )
            )
        }

        val existingOrders = db.orderDao().getAllOrders().first()
        if (existingOrders.isEmpty()) {
            db.orderDao().insertOrders(
                listOf(
                    OrderEntity(
                        id = "ORD-892A",
                        date = "24 Okt 2023 10:45",
                        status = "Baru",
                        customerName = "Budi Santoso",
                        address = "Jl. Merdeka Raya No. 45, RT 02/RW 05, Blok C, Dekat masjid Al-Ikhlas",
                        phone = "0812-3456-7890",
                        itemsSummary = "2x Beras Rojolele 5kg, 1x Minyak Goreng Bimoli 2L",
                        totalAmount = 145000.0,
                        timestamp = System.currentTimeMillis() - 1000 * 60 * 15
                    ),
                    OrderEntity(
                        id = "ORD-20231024-01",
                        date = "24 Okt 2023 09:30",
                        status = "Dikemas",
                        customerName = "Budi Santoso",
                        address = "Jl. Mawar Merah No. 15, Blok 21",
                        phone = "0812-3456-7890",
                        itemsSummary = "Paket Sayur Segar Organik, Telur Ayam Negeri",
                        totalAmount = 45000.0,
                        timestamp = System.currentTimeMillis() - 1000 * 60 * 120
                    ),
                    OrderEntity(
                        id = "ORD-20231023-42",
                        date = "23 Okt 2023 14:10",
                        status = "Dikirim",
                        customerName = "Siti Aminah",
                        address = "Jl. Melati Blok B No. 8",
                        phone = "0857-1122-3344",
                        itemsSummary = "Beras Pandan Wangi 5kg, Minyak Goreng 1L",
                        totalAmount = 78500.0,
                        timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24
                    )
                )
            )
        }

        val existingReviews = db.reviewDao().getAllReviews().first()
        if (existingReviews.isEmpty()) {
            db.reviewDao().insertReviews(
                listOf(
                    ReviewEntity(
                        productId = "p1",
                        userName = "Budi Santoso",
                        rating = 5.0f,
                        comment = "Sayurnya segar banget! Packingnya juga rapi dan aman. Bakal langganan terus nih di BerkahMart.",
                        dateText = "2 Hari yang lalu"
                    ),
                    ReviewEntity(
                        productId = "p1",
                        userName = "Ani Lestari",
                        rating = 4.0f,
                        comment = "Kualitas bagus, tapi pengiriman agak telat dikit. Overall memuaskan.",
                        dateText = "1 Minggu yang lalu"
                    )
                )
            )
        }
    }

    // Cart operations
    suspend fun addToCart(productId: String, quantityToAdd: Int = 1) = withContext(Dispatchers.IO) {
        val existing = db.cartDao().getCartItem(productId)
        if (existing != null) {
            db.cartDao().insertOrUpdateCartItem(existing.copy(quantity = existing.quantity + quantityToAdd))
        } else {
            db.cartDao().insertOrUpdateCartItem(CartItemEntity(productId = productId, quantity = quantityToAdd, selected = true))
        }
    }

    suspend fun updateCartQuantity(productId: String, newQty: Int) = withContext(Dispatchers.IO) {
        if (newQty <= 0) {
            db.cartDao().deleteCartItem(productId)
        } else {
            val existing = db.cartDao().getCartItem(productId)
            if (existing != null) {
                db.cartDao().insertOrUpdateCartItem(existing.copy(quantity = newQty))
            }
        }
    }

    suspend fun toggleCartSelection(productId: String, selected: Boolean) = withContext(Dispatchers.IO) {
        val existing = db.cartDao().getCartItem(productId)
        if (existing != null) {
            db.cartDao().insertOrUpdateCartItem(existing.copy(selected = selected))
        }
    }

    suspend fun setAllCartSelected(selected: Boolean) = withContext(Dispatchers.IO) {
        db.cartDao().setAllSelected(selected)
    }

    suspend fun deleteCartItem(productId: String) = withContext(Dispatchers.IO) {
        db.cartDao().deleteCartItem(productId)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        db.cartDao().clearCart()
    }

    // Product operations
    suspend fun addOrUpdateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        db.productDao().insertProduct(product)
    }

    suspend fun deleteProduct(productId: String) = withContext(Dispatchers.IO) {
        db.productDao().deleteProductById(productId)
    }

    suspend fun getProductById(id: String): ProductEntity? = withContext(Dispatchers.IO) {
        db.productDao().getProductById(id)
    }

    // Order operations
    suspend fun createOrder(order: OrderEntity) = withContext(Dispatchers.IO) {
        db.orderDao().insertOrder(order)
    }

    suspend fun updateOrderStatus(orderId: String, status: String) = withContext(Dispatchers.IO) {
        db.orderDao().updateOrderStatus(orderId, status)
    }

    // Review operations
    fun getReviewsForProduct(productId: String): Flow<List<ReviewEntity>> = db.reviewDao().getReviewsForProduct(productId)

    suspend fun addReview(review: ReviewEntity) = withContext(Dispatchers.IO) {
        db.reviewDao().insertReview(review)
    }
}
