package es.dam.booknest.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.dam.booknest.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.45f),
                drawerContainerColor = OldPaper,
                drawerContentColor = InkBlack,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "BookNest",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = LeatherBrown
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = LeatherBrown.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))
                
                NavigationDrawerItem(
                    label = { Text("Profile", color = InkBlack) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Navegar a perfil
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = LeatherBrown) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = { Text("Books", color = InkBlack) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Navegar a libros
                    },
                    icon = { Icon(Icons.Default.AutoStories, contentDescription = null, tint = LeatherBrown) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent
                    )
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "BookNest",
                            style = MaterialTheme.typography.headlineMedium,
                            color = InkBlack
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open drawer",
                                tint = LeatherBrown
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = OldPaper
                    )
                )
            },
            containerColor = OldPaper
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(OldPaper),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Welcome to your Library",
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkBlack.copy(alpha = 0.6f)
                )
            }
        }
    }
}
