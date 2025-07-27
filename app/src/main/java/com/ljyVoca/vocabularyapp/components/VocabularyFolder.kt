package com.ljyVoca.vocabularyapp.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.model.Vocabulary
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography

@Composable
fun VocabularyFolder(
    vocabulary: Vocabulary,
    onClick: () -> Unit,
    onUpdate: (Vocabulary) -> Unit,
    onDelete: (String) -> Unit
) {
    val language = Language.entries.find { it.code == vocabulary.category }
    val displayCategory = language?.displayName() ?: vocabulary.category

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = vocabulary.title,
                    style = AppTypography.fontSize20ExtraBold,
                    modifier = Modifier
                        .padding(top = 4.dp, end = 16.dp)
                        .weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.BorderColor,
                    contentDescription = "insert",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onUpdate(vocabulary) }
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onDelete(vocabulary.id) }
                )
            }

            Text(
                text = vocabulary.description,
                style = AppTypography.fontSize16Regular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Text(
                text = displayCategory,
                style = AppTypography.fontSize14Regular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.End
            )
        }
    }
}