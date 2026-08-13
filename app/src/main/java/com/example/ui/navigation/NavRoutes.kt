package com.example.ui.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val CHECKOUT = "checkout"
    const val CART = "cart"
    const val PAYMENT_SUCCESS = "payment_success/{orderId}"
    const val ORDERS = "orders"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val ADD_EDIT_PRODUCT = "add_edit_product?productId={productId}"
    const val VOUCHERS = "vouchers"
    const val CUSTOMER_SERVICE = "customer_service"
    const val NOTIFICATIONS = "notifications"
    const val LOGIN_ADMIN = "login_admin"
    const val REFERRAL = "referral"
    const val DASHBOARD_ADMIN = "dashboard_admin"
    const val INCOMING_ORDERS = "incoming_orders"
    const val RATING = "rating/{productId}"

    fun productDetail(productId: String) = "product_detail/$productId"
    fun paymentSuccess(orderId: String) = "payment_success/$orderId"
    fun addEditProduct(productId: String? = null) = if (productId != null) "add_edit_product?productId=$productId" else "add_edit_product"
    fun rating(productId: String) = "rating/$productId"
}
