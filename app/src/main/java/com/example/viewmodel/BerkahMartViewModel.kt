package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BerkahMartRepository
import com.example.data.CartItemWithProduct
import com.example.data.local.AppDatabase
import com.example.data.local.CartItemEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.VoucherEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val senderName: String,
    val isFromAdmin: Boolean,
    val text: String,
    val timestamp: String = "Baru saja",
    val orderIdContext: String? = null
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val type: String = "order" // "order", "promo", "system"
)

class BerkahMartViewModel(application: Application) : AndroidViewModel(application) {

    val repository: BerkahMartRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BerkahMartRepository(db)
    }

    val products: StateFlow<List<ProductEntity>> = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartWithProducts: StateFlow<List<CartItemWithProduct>> = combine(
        repository.cartItems,
        repository.allProducts
    ) { items, productList ->
        val productMap = productList.associateBy { it.id }
        items.mapNotNull { cartItem ->
            val product = productMap[cartItem.productId]
            if (product != null) CartItemWithProduct(cartItem, product) else null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val vouchers: StateFlow<List<VoucherEntity>> = repository.allVouchers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // User session
    private val _isLoggedIn = MutableStateFlow(true) // Default true for rich user demo
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("Budi Santoso")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("budi.santoso@email.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _userPhone = MutableStateFlow("0812-3456-7890")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _userAddress = MutableStateFlow("Jl. Merdeka Raya No. 45, RT 02/RW 05, Blok C, Blok 21")
    val userAddress: StateFlow<String> = _userAddress.asStateFlow()

    // Applied Voucher in Checkout
    private val _appliedVoucher = MutableStateFlow<VoucherEntity?>(null)
    val appliedVoucher: StateFlow<VoucherEntity?> = _appliedVoucher.asStateFlow()

    // Delivery & Payment selection
    private val _selectedDeliveryOption = MutableStateFlow("Pengiriman Instan (15-30 Mins)")
    val selectedDeliveryOption = _selectedDeliveryOption.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow("Bank BCA (Virtual Account)")
    val selectedPaymentMethod = _selectedPaymentMethod.asStateFlow()

    // Customer Service Chat state
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                senderName = "Admin Nisa",
                isFromAdmin = true,
                text = "Halo Kak Budi! Selamat datang di Customer Service BerkahMart Blok 21. Ada yang bisa kami bantu mengenai pesanan #INV-20231025?",
                timestamp = "10:30 AM",
                orderIdContext = "#INV-20231025"
            ),
            ChatMessage(
                senderName = "Budi Santoso",
                isFromAdmin = false,
                text = "Halo Mba, mau tanya estimasi beras pandan wangi nya kapan sampai ya?",
                timestamp = "10:32 AM"
            ),
            ChatMessage(
                senderName = "Admin Nisa",
                isFromAdmin = true,
                text = "Kurir kami Pak Agus sedang dalam perjalanan ke lokasi Blok C No. 45 Kak. Perkiraan 10 menit lagi sampai!",
                timestamp = "10:33 AM"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Notifications state
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "n1",
                title = "Pesanan Diantar 🚚",
                message = "Pesanan #ORD-892A sedang diantar oleh Kurir Agus. Mohon siapkan pembayaran jika COD.",
                timeAgo = "10 Mins lalu",
                isRead = false,
                type = "order"
            ),
            NotificationItem(
                id = "n2",
                title = "Promo Akhir Pekan! 🎉",
                message = "Gunakan kode HEMAT20K untuk potongan Rp 20.000 belanja sembako murah hari ini.",
                timeAgo = "2 Jam lalu",
                isRead = false,
                type = "promo"
            ),
            NotificationItem(
                id = "n3",
                title = "Voucher Berhasil Diklaim ✨",
                message = "Selamat! Voucher GRATISONGKIR telah ditambahkan ke dompet voucher Anda.",
                timeAgo = "1 Hari lalu",
                isRead = true,
                type = "system"
            ),
            NotificationItem(
                id = "n4",
                title = "Pesanan Selesai ✅",
                message = "Pesanan #ORD-20231023-42 telah selesai. Berikan ulasan bintang 5 untuk bantu tetangga!",
                timeAgo = "2 Hari lalu",
                isRead = true,
                type = "order"
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Cart actions
    fun addToCart(productId: String, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(productId, quantity)
        }
    }

    fun updateCartQty(productId: String, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(productId, newQty)
        }
    }

    fun toggleCartSelection(productId: String, selected: Boolean) {
        viewModelScope.launch {
            repository.toggleCartSelection(productId, selected)
        }
    }

    fun selectAllCartItems(selected: Boolean) {
        viewModelScope.launch {
            repository.setAllCartSelected(selected)
        }
    }

    fun deleteCartItem(productId: String) {
        viewModelScope.launch {
            repository.deleteCartItem(productId)
        }
    }

    fun applyVoucher(voucher: VoucherEntity?) {
        _appliedVoucher.value = voucher
    }

    fun setSelectedDeliveryOption(option: String) {
        _selectedDeliveryOption.value = option
    }

    fun setSelectedPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }

    // Checkout process
    fun placeOrder(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val cartList = cartWithProducts.value.filter { it.cartItem.selected }
            if (cartList.isEmpty()) return@launch

            val subtotal = cartList.sumOf { it.product.price * it.cartItem.quantity }
            val discount = _appliedVoucher.value?.discountAmount ?: 0.0
            val deliveryFee = 10000.0
            val total = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)

            val summaryText = cartList.joinToString(", ") { "${it.cartItem.quantity}x ${it.product.name}" }
            val orderId = "ORD-" + (1000..9999).random().toString() + "B"

            val newOrder = OrderEntity(
                id = orderId,
                date = "Hari ini, " + java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                status = "Baru",
                customerName = _userName.value,
                address = _userAddress.value,
                phone = _userPhone.value,
                itemsSummary = summaryText,
                totalAmount = total,
                timestamp = System.currentTimeMillis()
            )

            repository.createOrder(newOrder)
            repository.clearCart()
            _appliedVoucher.value = null
            onSuccess(orderId)
        }
    }

    // Admin Operations
    fun loginAdmin(email: String, pass: String, onSuccess: () -> Unit) {
        _isAdminLoggedIn.value = true
        onSuccess()
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    fun saveProduct(product: ProductEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addOrUpdateProduct(product)
            onDone()
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    // User login/logout
    fun loginUser(emailOrPhone: String, pass: String, onSuccess: () -> Unit) {
        _isLoggedIn.value = true
        _userEmail.value = if (emailOrPhone.contains("@")) emailOrPhone else "user@berkahmart.com"
        _userName.value = "Budi Santoso"
        onSuccess()
    }

    fun registerUser(name: String, email: String, phone: String, pass: String, onSuccess: () -> Unit) {
        _isLoggedIn.value = true
        _userName.value = name
        _userEmail.value = email
        _userPhone.value = phone
        onSuccess()
    }

    fun logoutUser() {
        _isLoggedIn.value = false
    }

    fun updateProfile(name: String, email: String, phone: String, address: String) {
        _userName.value = name
        _userEmail.value = email
        _userPhone.value = phone
        _userAddress.value = address
    }

    // Customer Service send message
    fun sendChatMessage(msgText: String) {
        if (msgText.isBlank()) return
        val userMsg = ChatMessage(
            senderName = _userName.value,
            isFromAdmin = false,
            text = msgText,
            timestamp = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        )
        val updated = _chatMessages.value + userMsg
        _chatMessages.value = updated

        // Auto reply simulation from Admin Nisa
        viewModelScope.launch {
            kotlinx.coroutines.delay(1200)
            val adminReply = ChatMessage(
                senderName = "Admin Nisa",
                isFromAdmin = true,
                text = "Terima kasih atas pesannya Kak! Admin BerkahMart siap membantu transaksi Anda.",
                timestamp = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            )
            _chatMessages.value = _chatMessages.value + adminReply
        }
    }

    // Mark notification as read
    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    // Add Review for product
    fun submitReview(productId: String, rating: Float, comment: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.addReview(
                ReviewEntity(
                    productId = productId,
                    userName = _userName.value,
                    rating = rating,
                    comment = comment,
                    dateText = "Baru saja"
                )
            )
            onDone()
        }
    }
}
