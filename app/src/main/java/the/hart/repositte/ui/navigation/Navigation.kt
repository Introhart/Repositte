package the.hart.repositte.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import the.hart.repositte.ui.screens.details.DetailsScreen
import the.hart.repositte.ui.screens.details.vm.DetailsViewModel
import the.hart.repositte.ui.screens.search.SearchScreen
import the.hart.repositte.ui.screens.search.vm.SearchViewModel

@Composable
internal fun NavigationHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.Search.route
    ) {
        composable(
            route = Destination.Search.route
        ) {
            val vm: SearchViewModel = hiltViewModel()
            val uiState = vm.uiState.collectAsState().value

            SearchScreen(
                uiState = uiState,
                navController = navController,
                search = vm::search,
                clearQuery = vm::clearQuery,
                onQueryChange = vm::onQueryChange,
            )
        }
        composable(
            route = "${NavDest.DETAILS}/{${NavArg.OWNER_NAME}}/{${NavArg.REPO_NAME}}", // todo :: looks like shit
            arguments = listOf(
                navArgument("ownerName") { type = NavType.StringType },
                navArgument("repoName") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val argsBundle = backStackEntry.arguments
            val ownerName = argsBundle?.getString(NavArg.OWNER_NAME) ?: ""
            val repoName = argsBundle?.getString(NavArg.REPO_NAME) ?: ""

            val vm: DetailsViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                vm.loadData(ownerName, repoName)
            }

            val uiState = vm.uiState.collectAsState().value

            DetailsScreen(
                navController = navController,
                uiState = uiState
            )
        }
    }
}

enum class Destination(val route: String) {
    Search("search_screen"),
    Details("details_screen"),
}

object NavDest {
    const val SEARCH = "search_screen"
    const val DETAILS = "details_screen"
}

private object NavArg {
    const val OWNER_NAME = "ownerName"
    const val REPO_NAME = "repoName"
}