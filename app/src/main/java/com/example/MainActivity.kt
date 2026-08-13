package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.AddEditProductScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminLoginScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.CustomerServiceScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.PaymentSuccessScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RatingScreen
import com.example.ui.screens.ReferralScreen
import com.example.ui.screens.VouchersScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.BerkahMartViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BerkahMartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BerkahMartApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun BerkahMartApp(viewModel: BerkahMartViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = "${NavRoutes.CATALOG}?category={category}",
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            CatalogScreen(
                viewModel = viewModel,
                initialCategory = category,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = NavRoutes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: "1"
            ProductDetailScreen(
                productId = productId,
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.CART) {
            CartScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.CHECKOUT) {
            CheckoutScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = NavRoutes.PAYMENT_SUCCESS,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            PaymentSuccessScreen(
                orderId = orderId,
                onNavigate = { route -> navController.navigate(route) {
                    popUpTo(NavRoutes.HOME) { inclusive = false }
                } }
            )
        }

        composable(NavRoutes.ORDERS) {
            OrdersScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.VOUCHERS) {
            VouchersScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.CUSTOMER_SERVICE) {
            CustomerServiceScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.REFERRAL) {
            ReferralScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = NavRoutes.RATING,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("productId") ?: ""
            RatingScreen(
                orderId = orderId,
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.LOGIN_ADMIN) {
            AdminLoginScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(NavRoutes.DASHBOARD_ADMIN) {
            AdminDashboardScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = NavRoutes.ADD_EDIT_PRODUCT,
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            AddEditProductScreen(
                productId = productId,
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}
