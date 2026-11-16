package com.sarinvoice.app

import android.content.Intent
import android.graphics.Color as AColor
import android.os.Bundle
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: AppVM = viewModel()
            val dark by vm.dark.collectAsState()
            DisposableEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = if (dark) SystemBarStyle.dark(AColor.TRANSPARENT) else SystemBarStyle.light(AColor.TRANSPARENT, AColor.TRANSPARENT),
                    navigationBarStyle = if (dark) SystemBarStyle.dark(AColor.TRANSPARENT) else SystemBarStyle.light(AColor.TRANSPARENT, AColor.TRANSPARENT)
                )
                onDispose { }
            }
            SarTheme(dark) { App(vm, dark) }
        }
    }
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(vm: AppVM, dark: Boolean) {
    val nav = rememberNavController()
    val ctx = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val auth = remember { AuthManager(ctx) }

    val tabs = listOf(
        Tab("dashboard", "Home", Icons.Default.Dashboard),
        Tab("invoices", "Invoices", Icons.Default.Receipt),
        Tab("customers", "Customers", Icons.Default.People),
        Tab("items", "Items", Icons.Default.Inventory2),
        Tab("settings", "Settings", Icons.Default.Settings)
    )
    val route = nav.currentBackStackEntryAsState().value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(24.dp)
                ) {
                    Column {
                        Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text(auth.getCurrentUserName(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(auth.getCurrentUserEmail() ?: "sar.developer2026@gmail.com", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                HorizontalDivider()
                val drawerItemColors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFF0B3A66),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = Color(0xFF00B8FF),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedIconColor = Color(0xFF00B8FF),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = route == "dashboard",
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("dashboard") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Home, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Invoices") },
                    selected = route == "invoices",
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("invoices") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Receipt, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Customers") },
                    selected = route == "customers",
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("customers") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.People, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Items") },
                    selected = route == "items",
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("items") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Inventory2, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Banking") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("invoices") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.AccountBalance, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Sales") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("invoices") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.ShoppingCart, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Purchases") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("record_expense") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.ShoppingBag, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Time Tracking") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("dashboard") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Timer, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Accountant") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("settings") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Person, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Documents") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("invoices") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Folder, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Reports") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("dashboard") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.BarChart, null) },
                    colors = drawerItemColors
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = route == "settings",
                    onClick = { scope.launch { drawerState.close() }; nav.navigate("settings") { popUpTo("dashboard"); launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Settings, null) },
                    colors = drawerItemColors
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Create a New Account") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        ctx.startActivity(Intent(ctx, RegisterActivity::class.java))
                    },
                    icon = { Icon(Icons.Default.PersonAdd, null) },
                    colors = drawerItemColors
                )
            }
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("SAR Invoice", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Open Menu")
                        }
                    },
                    actions = {
                        IconButton({ vm.setDark(!dark) }) {
                            Icon(if (dark) Icons.Default.LightMode else Icons.Default.DarkMode, "Toggle Dark Mode")
                        }
                    }
                )
            }
        ) { pad ->
            NavHost(nav, "dashboard", Modifier.padding(pad)) {
                composable("dashboard") { Dashboard(vm, nav) }
                composable("invoices") { Invoices(vm, nav) }
                composable("customers") { Customers(vm) }
                composable("items") { Items(vm) }
                composable("settings") { Settings(vm, dark) }
                composable("new") { NewInvoice(vm, nav) }
                composable("inv/{id}") { Detail(vm, nav, it.arguments?.getString("id") ?: "") }
                composable("record_expense") { RecordExpenseScreen(vm, nav) }
            }
        }
    }
}

@Composable
fun Stat(label: String, value: String) = Panel {
    Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
}

@Composable
fun InvoiceRow(i: Invoice, onClick: () -> Unit) = Panel(onClick = onClick) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(i.number, fontWeight = FontWeight.Bold)
            Text("${i.customer} · ${i.date}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(sar(i.total), fontWeight = FontWeight.Bold)
            Text(
                if (i.paid) "PAID" else "UNPAID", style = MaterialTheme.typography.labelMedium,
                color = if (i.paid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun CashFlowSection(vm: AppVM, nav: NavHostController, onOpenFiscal: () -> Unit, onOpenNewBill: () -> Unit) {
    val inv = vm.db.invoices
    val bills = vm.db.bills
    val expenses = vm.db.expenses

    val openingBal = 0.0
    val incoming = inv.filter { it.paid }.sumOf { it.total }
    val outgoing = expenses.sumOf { it.amount } + bills.filter { it.paid }.sumOf { it.amount }
    val endingBal = openingBal + incoming - outgoing

    Panel {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.ShowChart, null, tint = MaterialTheme.colorScheme.primary)
                Text("Cash Flow", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Surface(
                onClick = onOpenFiscal,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Row(
                    Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(vm.selectedFiscalPeriod, style = MaterialTheme.typography.bodySmall)
                    Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Cash as on 01 Jan 2026", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(sar(openingBal), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("+ Incoming", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
            Text(sar(incoming), style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4CAF50), fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("- Outgoing", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
            Text(sar(outgoing), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.padding(vertical = 4.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("= Cash as on 31 Dec 2026", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(sar(endingBal), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { nav.navigate("new") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add Invoice")
            }
            OutlinedButton(onClick = onOpenNewBill) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add Bill")
            }
        }
    }
}

@Composable
fun IncomeAndExpenseSection(vm: AppVM) {
    var accrual by remember { mutableStateOf(true) }
    val inv = vm.db.invoices
    val bills = vm.db.bills
    val expenses = vm.db.expenses

    val income = if (accrual) inv.sumOf { it.total } else inv.filter { it.paid }.sumOf { it.total }
    val expense = if (accrual) expenses.sumOf { it.amount } + bills.sumOf { it.amount } else expenses.sumOf { it.amount } + bills.filter { it.paid }.sumOf { it.amount }

    Panel {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.PieChart, null, tint = MaterialTheme.colorScheme.primary)
                Text("Income and Expense", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Text(vm.selectedFiscalPeriod, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = accrual,
                onClick = { accrual = true },
                label = { Text("Accrual") }
            )
            FilterChip(
                selected = !accrual,
                onClick = { accrual = false },
                label = { Text("Cash") }
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Income", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(sar(income), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Expense", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(sar(expense), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun Dashboard(vm: AppVM, nav: NavHostController) {
    var selectedTopTab by rememberSaveable { mutableIntStateOf(0) } // 0: Dashboard, 1: Announcements, 2: Help
    var showNewBill by remember { mutableStateOf(false) }
    var showNewCustomer by remember { mutableStateOf(false) }
    var showLogTime by remember { mutableStateOf(false) }
    var showFiscalSheet by remember { mutableStateOf(false) }

    val inv = vm.db.invoices
    val expenses = vm.db.expenses
    val timeLogs = vm.db.timeLogs

    Column(Modifier.fillMaxSize()) {
        // Top Sub-tabs row (Dashboard, Announcements, Help)
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val titles = listOf("Dashboard", "Announcements", "Help")
            val icons = listOf(Icons.Default.Dashboard, Icons.Default.Campaign, Icons.Default.Help)
            titles.forEachIndexed { idx, title ->
                val selected = selectedTopTab == idx
                FilterChip(
                    selected = selected,
                    onClick = { selectedTopTab = idx },
                    label = { Text(title) },
                    leadingIcon = { Icon(icons[idx], null, modifier = Modifier.size(16.dp)) }
                )
            }
        }

        when (selectedTopTab) {
            0 -> {
                // Dashboard Tab Content matching reference images
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Quick Create Section
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Bolt, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Text("Quick Create", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickActionCard("New\nCustomer", Icons.Default.PersonAdd, Modifier.weight(1f)) { showNewCustomer = true }
                                QuickActionCard("New\nInvoice", Icons.Default.Receipt, Modifier.weight(1f)) { nav.navigate("new") }
                                QuickActionCard("New\nBill", Icons.Default.RequestQuote, Modifier.weight(1f)) { showNewBill = true }
                                QuickActionCard("New\nExpense", Icons.Default.ReceiptLong, Modifier.weight(1f)) { nav.navigate("record_expense") }
                            }
                        }
                    }

                    // Cash Flow Section
                    item {
                        CashFlowSection(vm, nav, { showFiscalSheet = true }, { showNewBill = true })
                    }

                    // Income and Expense Section
                    item {
                        IncomeAndExpenseSection(vm)
                    }

                    // Project Summary & Timer Section
                    item {
                        Panel {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.FolderOpen, null, tint = MaterialTheme.colorScheme.primary)
                                Text("Project Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(12.dp))
                            Column(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)).padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    formatTimer(vm.timerSeconds),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    if (vm.isTimerRunning) "Timer Running (${vm.timerProject})" else "Start Project Timer",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(onClick = { showLogTime = true }) {
                                        Text("Log Time")
                                    }
                                    if (vm.isTimerRunning) {
                                        Button(onClick = { vm.stopAndLogTimer() }) {
                                            Text("Stop & Log")
                                        }
                                    } else {
                                        Button(onClick = { vm.startTimer() }) {
                                            Text("Start Timer")
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val unbilledHours = timeLogs.filter { !it.billed }.sumOf { it.hours }
                                val unbilledExpenses = expenses.sumOf { it.amount }
                                Surface(
                                    Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                ) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text("Unbilled Hours", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(String.format(Locale.US, "%.2f hrs", unbilledHours), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Surface(
                                    Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                ) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text("Unbilled Expenses", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(sar(unbilledExpenses), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Top Expenses Section
                    item {
                        Panel {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.DonutLarge, null, tint = MaterialTheme.colorScheme.primary)
                                    Text("Top Expenses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                                Surface(
                                    onClick = { showFiscalSheet = true },
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(vm.selectedFiscalPeriod, style = MaterialTheme.typography.bodySmall)
                                        Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            if (expenses.isEmpty()) {
                                Text("You haven't created any expenses for the selected period.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    expenses.take(3).forEach { e ->
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(e.title, fontWeight = FontWeight.Bold)
                                            Text(sar(e.amount))
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { nav.navigate("record_expense") }, Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.Add, null)
                                Spacer(Modifier.width(4.dp))
                                Text("Record Expense")
                            }
                        }
                    }

                    // Existing Quick Stats & Recent Invoices
                    item { Stat("Outstanding", sar(inv.filter { !it.paid }.sumOf { it.total })) }
                    item { Stat("Paid", sar(inv.filter { it.paid }.sumOf { it.total })) }
                    item { Stat("Invoices", "${inv.size}") }
                    item { Text("Recent invoices", style = MaterialTheme.typography.titleMedium) }
                    items(inv.takeLast(5).reversed(), key = { it.id }) { InvoiceRow(it) { nav.navigate("inv/${it.id}") } }
                }
            }
            1 -> {
                // Announcements Tab
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Panel {
                            Text("🎉 SAR Invoice v2.0 Released", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("We've added powerful new features including Quick Bills, Expense Recording, and Project Time Tracking timers directly on your dashboard.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            2 -> {
                // Help Tab
                Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Panel {
                        Text("Need Assistance?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Welcome to SAR Invoice help center. You can manage customers, items, invoices, bills, and track time effortlessly.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }

    // Fiscal Year Bottom Sheet
    if (showFiscalSheet) {
        FiscalYearBottomSheet(
            current = vm.selectedFiscalPeriod,
            onSelect = { vm.selectedFiscalPeriod = it },
            onDismiss = { showFiscalSheet = false }
        )
    }

    // Dialogs
    if (showNewBill) {
        var vendor by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showNewBill = false },
            title = { Text("New Bill") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(vendor, { vendor = it }, label = { Text("Vendor Name") }, singleLine = true)
                    OutlinedTextField(amount, { amount = it }, label = { Text("Amount (SAR)") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton({
                    val d = amount.toDoubleOrNull()
                    if (vendor.isNotBlank() && d != null) {
                        vm.addBill(vendor, d)
                        showNewBill = false
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton({ showNewBill = false }) { Text("Cancel") } }
        )
    }

    if (showLogTime) {
        var project by remember { mutableStateOf(vm.timerProject) }
        var hours by remember { mutableStateOf("1.0") }
        AlertDialog(
            onDismissRequest = { showLogTime = false },
            title = { Text("Log Time Manually") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(project, { project = it }, label = { Text("Project Name") }, singleLine = true)
                    OutlinedTextField(hours, { hours = it }, label = { Text("Hours") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton({
                    val h = hours.toDoubleOrNull()
                    if (project.isNotBlank() && h != null) {
                        vm.logTime(project, h)
                        showLogTime = false
                    }
                }) { Text("Log") }
            },
            dismissButton = { TextButton({ showLogTime = false }) { Text("Cancel") } }
        )
    }

    if (showNewCustomer) {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showNewCustomer = false },
            title = { Text("New Customer") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(name, { name = it }, label = { Text("Name") }, singleLine = true)
                    OutlinedTextField(email, { email = it }, label = { Text("Email") }, singleLine = true)
                    OutlinedTextField(phone, { phone = it }, label = { Text("Phone") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton({
                    if (name.isNotBlank()) {
                        vm.addCustomer(name, email, phone)
                        showNewCustomer = false
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton({ showNewCustomer = false }) { Text("Cancel") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiscalYearBottomSheet(current: String, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    val options = listOf(
        "This Fiscal Year",
        "This Quarter",
        "This Month",
        "Previous Fiscal Year",
        "Previous Quarter",
        "Previous Month",
        "Last 6 Months",
        "Last 12 Months"
    )
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Select Period", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            options.forEach { opt ->
                Surface(
                    onClick = { onSelect(opt); onDismiss() },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (opt == current) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        opt,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (opt == current) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordExpenseScreen(vm: AppVM, nav: NavHostController) {
    var date by remember { mutableStateOf("11 Nov 2026") }
    var expenseAccount by remember { mutableStateOf("") }
    var paidThrough by remember { mutableStateOf("") }
    var itemize by remember { mutableStateOf(false) }
    var currency by remember { mutableStateOf("USD") }
    var amount by remember { mutableStateOf("") }
    var tax by remember { mutableStateOf("None") }
    var vendor by remember { mutableStateOf("") }
    var reference by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var customer by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Expense", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton({ nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                },
                actions = {
                    TextButton(onClick = {
                        val d = amount.toDoubleOrNull()
                        if (d != null) {
                            vm.addExpense(
                                title = expenseAccount.ifBlank { "Expense" },
                                category = expenseAccount.ifBlank { "General" },
                                amount = d,
                                date = date,
                                notes = notes,
                                paidThrough = paidThrough,
                                currency = currency,
                                tax = tax,
                                vendor = vendor,
                                reference = reference,
                                customer = customer
                            )
                            nav.popBackStack()
                        }
                    }) {
                        Text("SAVE", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Date") },
                    singleLine = true
                )
                Surface(
                    onClick = { /* attach receipt */ },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(
                        Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AttachFile, null, tint = MaterialTheme.colorScheme.primary)
                        Text("Attach\nReceipt", style = MaterialTheme.typography.bodySmall, maxLines = 2)
                    }
                }
            }

            OutlinedTextField(
                value = expenseAccount,
                onValueChange = { expenseAccount = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Expense Account") },
                placeholder = { Text("Select Expense Account") },
                singleLine = true
            )

            OutlinedTextField(
                value = paidThrough,
                onValueChange = { paidThrough = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Paid Through") },
                placeholder = { Text("Select account") },
                singleLine = true
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Itemize", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = itemize, onCheckedChange = { itemize = it })
            }

            HorizontalDivider()

            Panel {
                Text("Amount", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it },
                        modifier = Modifier.width(90.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("0.00") },
                        singleLine = true
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text("Tax", color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(
                    value = tax,
                    onValueChange = { tax = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Panel {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = vendor,
                        onValueChange = { vendor = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Vendor") },
                        placeholder = { Text("Start typing to select a Vendor") },
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Add, "Add Vendor")
                    }
                }

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = reference,
                    onValueChange = { reference = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Reference#") },
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Notes") },
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = customer,
                        onValueChange = { customer = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Customer") },
                        placeholder = { Text("Start typing to select a Customer") },
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Add, "Add Customer")
                    }
                }

                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { }) { Text("Reporting Tags") }
                    TextButton(onClick = { }) { Text("Associate Tags") }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(label: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, maxLines = 2)
        }
    }
}

private fun formatTimer(sec: Int): String {
    val m = sec / 60
    val s = sec % 60
    val h = m / 60
    val remM = m % 60
    return if (h > 0) "%d:%02d:%02d".format(Locale.US, h, remM, s) else "%02d:%02d".format(Locale.US, remM, s)
}

@Composable
fun Invoices(vm: AppVM, nav: NavHostController) {
    var filter by rememberSaveable { mutableIntStateOf(0) }
    val list = vm.db.invoices.reversed().filter { filter == 0 || (filter == 1 && !it.paid) || (filter == 2 && it.paid) }
    Box(Modifier.fillMaxSize()) {
        Column {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("All", "Unpaid", "Paid").forEachIndexed { n, s -> FilterChip(filter == n, { filter = n }, { Text(s) }) }
            }
            LazyColumn(contentPadding = PaddingValues(16.dp, 4.dp, 16.dp, 88.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(list, key = { it.id }) { InvoiceRow(it) { nav.navigate("inv/${it.id}") } }
            }
        }
        FloatingActionButton(
            { nav.navigate("new") }, Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
        ) { Icon(Icons.Default.Add, "New invoice") }
    }
}

@Composable
fun NewInvoice(vm: AppVM, nav: NavHostController) {
    var customer by rememberSaveable { mutableStateOf("") }
    val lines = remember { mutableStateListOf<Line>() }
    var name by rememberSaveable { mutableStateOf("") }
    var qty by rememberSaveable { mutableStateOf("1") }
    var price by rememberSaveable { mutableStateOf("") }
    val rate = vm.db.business.vatRate
    val sub = lines.sumOf { it.total }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("New invoice", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(customer, { customer = it }, Modifier.fillMaxWidth(), label = { Text("Customer") }, singleLine = true)
        if (vm.db.customers.isNotEmpty()) LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.db.customers) { c -> AssistChip({ customer = c.name }, { Text(c.name) }) }
        }
        if (vm.db.items.isNotEmpty()) {
            Text("Add from items", color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.db.items) { i -> AssistChip({ lines.add(Line(i.name, 1.0, i.price)) }, { Text("${i.name} · ${sar(i.price)}") }) }
            }
        }
        Panel {
            Text("Add line", fontWeight = FontWeight.Bold)
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Description") }, singleLine = true)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(qty, { qty = it }, Modifier.weight(1f), label = { Text("Qty") }, singleLine = true)
                OutlinedTextField(price, { price = it }, Modifier.weight(1f), label = { Text("Price") }, singleLine = true)
            }
            Spacer(Modifier.height(8.dp))
            Button({
                val q = qty.toDoubleOrNull(); val p = price.toDoubleOrNull()
                if (name.isNotBlank() && q != null && p != null) { lines.add(Line(name, q, p)); name = ""; qty = "1"; price = "" }
            }) { Text("Add line") }
        }
        lines.forEachIndexed { n, l ->
            Panel {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column { Text(l.name, fontWeight = FontWeight.Bold); Text("${l.qty} × ${sar(l.price)}") }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(sar(l.total))
                        IconButton({ lines.removeAt(n) }) { Icon(Icons.Default.Delete, "Remove") }
                    }
                }
            }
        }
        Panel {
            Text("Subtotal: ${sar(sub)}")
            Text("VAT ($rate%): ${sar(sub * rate / 100)}")
            Text("Total: ${sar(sub * (1 + rate / 100))}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Button({ vm.addInvoice(customer.ifBlank { "Walk-in customer" }, lines.toList()); nav.popBackStack() }, Modifier.fillMaxWidth(), enabled = lines.isNotEmpty()) { Text("Save invoice") }
    }
}

@Composable
fun Detail(vm: AppVM, nav: NavHostController, id: String) {
    val inv = vm.db.invoices.find { it.id == id }
    val ctx = LocalContext.current
    if (inv == null) { LaunchedEffect(Unit) { nav.popBackStack() }; return }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Panel {
            Text(inv.number, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("${inv.customer} · ${inv.date}")
            Text(if (inv.paid) "PAID" else "UNPAID", color = if (inv.paid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
        }
        Panel {
            inv.lines.forEach {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${it.name} (${it.qty} × ${sar(it.price)})", Modifier.weight(1f)); Text(sar(it.total))
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Subtotal: ${sar(inv.subtotal)}")
            Text("VAT (${inv.vatRate}%): ${sar(inv.vat)}")
            Text("Total: ${sar(inv.total)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Button({ vm.togglePaid(inv.id) }, Modifier.fillMaxWidth()) { Text(if (inv.paid) "Mark as unpaid" else "Mark as paid") }
        OutlinedButton({ Pdf.share(ctx, inv, vm.db.business) }, Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Share, null); Spacer(Modifier.width(8.dp)); Text("Share PDF")
        }
        TextButton({ vm.deleteInvoice(inv.id) }, Modifier.fillMaxWidth()) { Text("Delete invoice", color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
fun Customers(vm: AppVM) {
    var show by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 88.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(vm.db.customers, key = { it.id }) { c ->
                Panel {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column { Text(c.name, fontWeight = FontWeight.Bold); Text(listOf(c.email, c.phone).filter { it.isNotBlank() }.joinToString(" · ")) }
                        IconButton({ vm.deleteCustomer(c.id) }) { Icon(Icons.Default.Delete, "Delete") }
                    }
                }
            }
        }
        Fab(Modifier.align(Alignment.BottomEnd)) { show = true }
    }
    if (show) {
        var n by remember { mutableStateOf("") }; var e by remember { mutableStateOf("") }; var p by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { show = false }, title = { Text("New customer") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(n, { n = it }, label = { Text("Name") }, singleLine = true)
                    OutlinedTextField(e, { e = it }, label = { Text("Email") }, singleLine = true)
                    OutlinedTextField(p, { p = it }, label = { Text("Phone") }, singleLine = true)
                }
            },
            confirmButton = { TextButton({ if (n.isNotBlank()) { vm.addCustomer(n, e, p); show = false } }) { Text("Save") } },
            dismissButton = { TextButton({ show = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun Items(vm: AppVM) {
    var show by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 88.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(vm.db.items, key = { it.id }) { i ->
                Panel {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column { Text(i.name, fontWeight = FontWeight.Bold); Text(sar(i.price)) }
                        IconButton({ vm.deleteItem(i.id) }) { Icon(Icons.Default.Delete, "Delete") }
                    }
                }
            }
        }
        Fab(Modifier.align(Alignment.BottomEnd)) { show = true }
    }
    if (show) {
        var n by remember { mutableStateOf("") }; var p by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { show = false }, title = { Text("New item") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(n, { n = it }, label = { Text("Name") }, singleLine = true)
                    OutlinedTextField(p, { p = it }, label = { Text("Price (SAR)") }, singleLine = true)
                }
            },
            confirmButton = { TextButton({ val d = p.toDoubleOrNull(); if (n.isNotBlank() && d != null) { vm.addItem(n, d); show = false } }) { Text("Save") } },
            dismissButton = { TextButton({ show = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun Fab(modifier: Modifier, onClick: () -> Unit) = FloatingActionButton(
    onClick, modifier.padding(16.dp),
    containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
) { Icon(Icons.Default.Add, "Add") }

@Composable
fun Settings(vm: AppVM, dark: Boolean) {
    val ctx = LocalContext.current
    val b = vm.db.business
    var name by remember(b) { mutableStateOf(b.name) }
    var vat by remember(b) { mutableStateOf(b.vatNo) }
    var rate by remember(b) { mutableStateOf(b.vatRate.toString()) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Panel {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Dark Mode", fontWeight = FontWeight.Bold)
                    Text(if (dark) "ON · Black & Blue Neon" else "OFF · Light", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(dark, { vm.setDark(it) })
            }
        }
        Panel {
            Text("Account", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { ctx.startActivity(android.content.Intent(ctx, RegisterActivity::class.java)) }, Modifier.fillMaxWidth()) {
                Text("Create a New Account / Add Account")
            }
        }
        Panel {
            Text("Business", fontWeight = FontWeight.Bold)
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Business name") }, singleLine = true)
            OutlinedTextField(vat, { vat = it }, Modifier.fillMaxWidth(), label = { Text("VAT number") }, singleLine = true)
            OutlinedTextField(rate, { rate = it }, Modifier.fillMaxWidth(), label = { Text("VAT rate %") }, singleLine = true)
            Spacer(Modifier.height(8.dp))
            Button({ vm.saveBusiness(Business(name.ifBlank { "My Company" }, vat, rate.toDoubleOrNull() ?: 15.0)) }) { Text("Save") }
        }
    }
}
