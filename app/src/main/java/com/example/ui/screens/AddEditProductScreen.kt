package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProductEntity
import com.example.ui.navigation.NavRoutes
import com.example.viewmodel.BerkahMartViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: String?,
    viewModel: BerkahMartViewModel,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState()

    val existingProduct = products.find { it.id == productId }

    var name by remember { mutableStateOf(existingProduct?.name ?: "") }
    var category by remember { mutableStateOf(existingProduct?.category ?: "Sayuran Segar") }
    var priceStr by remember { mutableStateOf(existingProduct?.price?.toInt()?.toString() ?: "") }
    var unit by remember { mutableStateOf(existingProduct?.unit ?: "1 Kg") }
    var stockStr by remember { mutableStateOf(existingProduct?.stock?.toString() ?: "50") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) "Tambah Produk Baru" else "Edit Produk", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(NavRoutes.DASHBOARD_ADMIN) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Produk") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_product_name_input")
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Kategori (Contoh: Sayuran Segar, Buah Segar, Bumbu Dapur)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Harga (Rp)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_product_price_input")
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Satuan (Contoh: 1 Kg, 250 gram, 1 Ikat)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = stockStr,
                        onValueChange = { stockStr = it },
                        label = { Text("Jumlah Stok Saat Ini") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Deskripsi Singkat Produk") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (name.isBlank() || priceStr.isBlank()) {
                                Toast.makeText(context, "Nama dan harga produk wajib diisi!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val price = priceStr.toDoubleOrNull() ?: 0.0
                            val stock = stockStr.toIntOrNull() ?: 0

                            val newProduct = ProductEntity(
                                id = productId ?: UUID.randomUUID().toString(),
                                name = name,
                                category = category,
                                price = price,
                                originalPrice = price * 1.2,
                                unit = unit,
                                description = description.ifBlank { "Bahan berkualitas dan selalu segar langsung dari distributor terpercaya untuk warga Blok 21." },
                                stock = stock,
                                isPromo = true,
                                promoLabel = "PROMO",
                                rating = 4.8f,
                                reviewCount = 10,
                                drawableName = "ic_app_logo_1786607192226"
                            )

                            viewModel.saveProduct(newProduct)
                            Toast.makeText(context, "Produk berhasil disimpan!", Toast.LENGTH_SHORT).show()
                            onNavigate(NavRoutes.DASHBOARD_ADMIN)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_product_button")
                    ) {
                        Text("Simpan Produk", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
