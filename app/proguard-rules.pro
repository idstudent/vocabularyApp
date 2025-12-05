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
#####################
# OSS LICENSES
#####################
-keep class com.google.android.gms.oss.licenses.** { *; }
-dontwarn com.google.android.gms.oss.licenses.**

-keep class com.google.android.gms.oss.licenses.OssLicensesMenuActivity
-keep class com.google.android.gms.oss.licenses.OssLicensesActivity

-keep class com.google.android.gms.internal.oss_licenses.** { *; }
-dontwarn com.google.android.gms.internal.oss_licenses.**

-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn org.apache.batik.**
-dontwarn javax.xml.stream.**
-dontwarn net.sf.saxon.**
-dontwarn org.osgi.**

# Microsoft Office 스키마 관련
-dontwarn com.microsoft.schemas.**
-keep class com.microsoft.schemas.** { *; }

# Apache POI
-dontwarn org.apache.poi.**
-dontwarn org.openxmlformats.**
-dontwarn org.apache.xmlbeans.**
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn org.apache.batik.**
-dontwarn javax.xml.stream.**
-dontwarn net.sf.saxon.**
-dontwarn org.osgi.**
-dontwarn com.graphbuilder.**
-dontwarn org.apache.commons.math3.**

-keep class org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-keep class org.openxmlformats.schemas.** { *; }
-keep class com.microsoft.schemas.** { *; }

# 사용하지 않는 POI 기능 제외
-dontnote org.apache.poi.**
-dontnote org.apache.xmlbeans.**