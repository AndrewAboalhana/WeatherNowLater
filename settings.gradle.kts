pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WeatherNow&Later"
include(":app")
include(":data")
include(":domain")
include(":core:common")
include(":core:network")
include(":core:database")
include(":features:search")
include(":features:current_weather")
include(":features:forecast")
include(":weather-lib")
