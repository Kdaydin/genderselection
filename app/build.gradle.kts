import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    alias(libs.plugins.hilt.gradle)
}

android {

    val envStorePass = System.getenv("STORE_PASS")
    val envKeyAlias = System.getenv("KEY_ALIAS")
    val envKeyPass = System.getenv("KEY_PASS")
    val keystoreProperties = Properties()
    val localProperties = project.rootProject.file("local.properties")
    keystoreProperties.load(localProperties.inputStream())

    fun getStorePass(): String? {
        val storePass =
            if (keystoreProperties.containsKey("storepass")) keystoreProperties.getProperty("storepass") else envStorePass
        return storePass
    }

    fun getKeyAlias(): String? {
        val keyAlias =
            if (keystoreProperties.containsKey("keyalias")) keystoreProperties.getProperty("keyalias") else envKeyAlias
        return keyAlias
    }

    fun getKeyPass(): String? {
        val keyPass =
            if (keystoreProperties.containsKey("keypass")) keystoreProperties.getProperty("keypass") else envKeyPass
        return keyPass
    }

    namespace = "com.khomeapps.gender"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.khomeapps.gender"
        minSdk = 23
        targetSdk = 34
        versionCode = 3
        versionName = "1.1"
        vectorDrawables {
            useSupportLibrary = true
        }


    }

    signingConfigs {
        create("release") {
            storeFile = file("GenderKeyStore.jks")
            storePassword = getStorePass()?:""
            keyAlias = getKeyAlias()?:""
            keyPassword = getKeyPass()?:""
        }
        create("internal") {
            storeFile = file("GenderKeyStore.jks")
            storePassword = getStorePass()?:""
            keyAlias = getKeyAlias()?:""
            keyPassword = getKeyPass()?:""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            //proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        create("internal") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("internal")
        }
    }
    flavorDimensions += "type"

    productFlavors {
        create("Development") {
            dimension = "type"
            buildConfigField("boolean", "DEVELOPMENT", "true")
            buildConfigField("String", "AD_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
        }

        create("Store") {
            dimension = "type"
            buildConfigField("boolean", "DEVELOPMENT", "false")
            buildConfigField("String", "AD_ID", "\"ca-app-pub-8296491057212745/9516989377\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding =  true
        buildConfig  = true
    }

    packaging {
        resources {
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

ksp {
    arg("ksp.verbose", "true")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.firebase.ads)
    implementation(libs.appcenter.analytics)
    implementation(libs.appcenter.crashes)
    implementation(libs.lottie)
    implementation(libs.glide)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidChart)
    //ksp(libs.compiler)
    implementation(libs.fragment.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}