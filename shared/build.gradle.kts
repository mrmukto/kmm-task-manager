plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    // Android target for KMM
    androidTarget()

    // iOS targets (you can keep them, even if you only use Android for now)
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        // -------- commonMain --------
        commonMain.dependencies {
            // Coroutines & DateTime (shared KMM)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            // SQLDelight shared runtime
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
        }

        // -------- commonTest --------
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        // -------- androidMain --------
        val androidMain by getting
        androidMain.dependencies {
            // SQLDelight Android driver
            implementation(libs.sqldelight.android.driver)
        }

        // iOS source sets will just inherit commonMain for now
    }
}

android {
    // you can change this to match your package if you want
    namespace = "com.kmm.taskmanager.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// SQLDelight configuration (must be top-level, not inside android{})
sqldelight {
    databases {
        create("TaskDatabase") {
            packageName.set("com.kmm.taskmanager.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }
    }
}
