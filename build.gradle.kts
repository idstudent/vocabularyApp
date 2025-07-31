plugins {
    id("com.android.application") version "8.5.0" apply false  // 8.3.0 → 8.5.0
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false  // 1.9.20 → 2.0.20
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false  // 추가
    id("com.google.dagger.hilt.android") version "2.52" apply false  // 2.48 → 2.52
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false  // 업그레이드
    id("com.google.gms.google-services") version "4.4.3" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}