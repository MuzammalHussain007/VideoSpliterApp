package com.inventory.videospliterapp.navigation

import VideoDetialScreen
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inventory.videospliterapp.screens.SplitVideoScreen
import com.inventory.videospliterapp.screens.VideoScreen

@Composable
fun VideoNavigation()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Reader.VideoScreen.name ) {
        composable(Reader.VideoScreen.name){
            VideoScreen(navController = navController)
        }

        composable(Reader.SplitVideoScreen.name){
            SplitVideoScreen(navController = navController)
        }

        val uri = Reader.VideoDetialScreen.name
        composable("$uri/{videoUrl}") { backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("videoUrl")
            if (videoUrl != null) {
                val decodedUrl = Uri.decode(videoUrl)
                VideoDetialScreen(navController, decodedUrl)
            } else {
            }
        /*composable(Reader.LoginScreen.name){

        }

        composable(Reader.HomeScreen.name){

        }

        composable(Reader.UserStatsScreen.name){

        }

        var detailName = Reader.BookDetailScreen.name

        composable("$detailName/{bookName}") { backStackEntry ->
            val bookName = backStackEntry.arguments?.getString("bookName")
        //    BookDetailsScreen(navController, bookName)
        }



        composable(Reader.SearchScreen.name){
           // SearchScreen(navController)
        }*/
    }

}
}