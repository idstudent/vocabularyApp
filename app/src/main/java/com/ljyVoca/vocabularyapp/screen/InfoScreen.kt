package com.ljyVoca.vocabularyapp.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.navigation.AppRoutes
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography

@Composable
fun InfoScreen(
    navController: NavHostController
) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.info),
                style = AppTypography.fontSize20SemiBold.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(16.dp)
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.privacy),
                    style = AppTypography.fontSize16Regular.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(AppRoutes.PRIVACY_SCREEN)
                        }
                )
            }
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = stringResource(R.string.open_source_licenses),
                    style = AppTypography.fontSize16Regular.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            val intent = Intent(context, OssLicensesMenuActivity::class.java)
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}