#####################
# HILT (DI 관련)
#####################
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class androidx.hilt.** { *; }
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**
-dontwarn dagger.**

#####################
# ROOM (DB 관련)
#####################
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-keepattributes *Annotation*
-dontwarn androidx.room.**

#####################
# JETPACK COMPOSE
#####################
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-dontwarn androidx.compose.**

#####################
# FIREBASE (Analytics, FCM)
#####################
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

#####################
# KOROMAN (외부 한글 로마자화 라이브러리)
#####################
-keep class com.gerosyab.koroman.** { *; }
-dontwarn com.gerosyab.koroman.**

#####################
# LINGUA (언어 감지)
#####################
-keep class com.github.pemistahl.lingua.** { *; }
-dontwarn com.github.pemistahl.lingua.**

#####################
# GOOGLE ADS (AdMob)
#####################
-keep public class com.google.android.gms.ads.** { public *; }
-dontwarn com.google.android.gms.ads.**

#####################
# KOTLIN
#####################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
-dontwarn kotlin.**
-keep class kotlinx.** { *; }
-dontwarn kotlinx.**

#####################
# VIEWBINDING
#####################
-keep class **.databinding.*Binding { *; }
-keepclassmembers class **.databinding.*Binding {
    public static <fields>;
    public <init>(...);
}

#####################
# ACTIVITY / FRAGMENT / COMPOSABLE ENTRY POINTS
#####################
-keep class * extends android.app.Activity
-keep class * extends androidx.activity.ComponentActivity
-keep class * extends androidx.fragment.app.Fragment

#####################
# REFLECTION, KEEP ALL ANNOTATIONS
#####################
-keepattributes *Annotation*,InnerClasses,EnclosingMethod

#####################
# DEBUGGING TIP (선택)
#####################
# -printmapping mapping.txt
