package es.dam.booknest.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import es.dam.booknest.ui.book.BookItem
import es.dam.booknest.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onBookClick: () -> Unit,
    vm: HomeViewModel
) {
    val uiState by vm.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.40f),
                drawerContainerColor = OldPaper,
                drawerContentColor = InkBlack,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Menu",
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
                    .background(OldPaper)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = LeatherBrown)
                } else if (uiState.error != null) {
                    Text(text = uiState.error!!, color = ErrorRed, modifier = Modifier.align(Alignment.Center))
                } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item(
                                span = {
                                    androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan)
                                }
                            ) {
                                Text(
                                    "Library",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = InkBlack,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            items(uiState.books) { book ->
                                BookItem(
                                    book = book,
                                    onClick = {
                                        vm.selectBook(book)
                                        onBookClick()
                                    }
                                )
                            }
                        }
                    }
            }
        }
    }
}
