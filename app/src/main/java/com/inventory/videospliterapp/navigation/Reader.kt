package com.inventory.videospliterapp.navigation


enum class Reader {
     VideoScreen,
    VideoDetialScreen,
    SplitVideoScreen

    ;

    companion object {
        fun fromRouteName(name: String): Reader = when (name.substringBefore("/")) {
            VideoScreen.name -> VideoScreen
            VideoDetialScreen.name->VideoDetialScreen
            SplitVideoScreen.name->SplitVideoScreen
          /*  SplashScreen.name -> SplashScreen
            UserStatsScreen.name-> UserStatsScreen
            LoginScreen.name -> LoginScreen
            SignUpScreen.name -> SignUpScreen
            BookDetailScreen.name -> BookDetailScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            UpdateScreen.name -> UpdateScreen
            ReaderStatsScreen.name -> ReaderStatsScreen*/
            null -> VideoScreen
            else -> {
                throw java.lang.IllegalArgumentException("Route not found")
            }
        }
    }

}