package com.ljyVoca.vocabularyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.daissue.koroman.Koroman
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography


@Composable
fun TodayWordCard(
    word: VocaWord,
    ttsClick: (VocaWord) -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            ),
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
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = if(word.description.isBlank()) {
                Arrangement.Center // description 없으면 중앙 정렬
            } else {
                Arrangement.Top // description 있으면 위부터 정렬
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = word.word,
                    style = AppTypography.fontSize20SemiBold,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        ttsClick(word)
                    },
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(24.dp),
                            clip = false
                        )
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(24.dp)
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Campaign,
                        contentDescription = "tts"
                    )
                }
            }
            Text(
                text= word.mean,
                style = AppTypography.fontSize16SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
            Spacer(Modifier.width(8.dp))
            // description이 있을 때만 나머지 내용 표시
            if(word.description.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                if(word.category == Language.KOREAN.code) {
                    Text(
                        Koroman.romanize(word.mean),
                        style = AppTypography.fontSize16Regular.copy(MaterialTheme.colorScheme.onSecondary),
                    )
                    Spacer(Modifier.height(16.dp))
                }
                Text(
                    word.description,
                    style = AppTypography.fontSize16Regular,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )
            } else if(word.category == Language.KOREAN.code) {
                // description 없고 한국어인 경우 로마자만 표시
                Spacer(Modifier.height(8.dp))
                Text(
                    Koroman.romanize(word.mean),
                    style = AppTypography.fontSize16Regular.copy(MaterialTheme.colorScheme.onSecondary)
                )
            }
        }
    }
}