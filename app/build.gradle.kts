plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias (libs.plugins.googleService)

}

android {
    namespace = "com.dmo.tiendavirtual"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.dmo.tiendavirtual"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding=true //evita escribir el findbyid
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.lottie)//Animacion
    implementation(libs.firebaseAuth) //Autenticacion con firebase
    implementation(libs.firebaseDatabase)//Base de Datos
    implementation(libs.maps)//Google Maps
    implementation(libs.places)//Lugares para googleMaps
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}