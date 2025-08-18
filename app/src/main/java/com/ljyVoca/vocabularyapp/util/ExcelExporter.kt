package com.ljyVoca.vocabularyapp.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.ljyVoca.vocabularyapp.model.VocaWord
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelExporter(private val context: Context) {

    fun exportWordsToExcel(words: List<VocaWord>, fileName: String = "단어장.xlsx"): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                exportWithMediaStore(words, fileName)
            } else {
                exportWithLegacyStorage(words, fileName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("NewApi")
    private fun exportWithMediaStore(words: List<VocaWord>, fileName: String): Boolean {
        val workbook = createWorkbook(words)

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            ?: return false

        resolver.openOutputStream(uri)?.use { outputStream ->
            workbook.write(outputStream)
        }

        workbook.close()

        // 파일 생성 후 자동으로 열기
        openExcelFile(uri)
        return true
    }

    private fun exportWithLegacyStorage(words: List<VocaWord>, fileName: String): Boolean {
        val workbook = createWorkbook(words)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }

        workbook.close()

        // 파일 생성 후 자동으로 열기
        openExcelFile(Uri.fromFile(file))
        return true
    }

    private fun createWorkbook(words: List<VocaWord>): XSSFWorkbook {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("단어장")

        // 헤더 스타일 생성
        val headerStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 12
            }
            setFont(font)
        }

        // VocaWord에 description 필드가 있는지 확인 후 헤더 생성
        val hasDescription = try {
            words.firstOrNull()?.let { word ->
                // reflection으로 description 필드 존재 확인
                word::class.java.getDeclaredField("description")
                true
            } ?: false
        } catch (e: Exception) {
            false
        }

        // 헤더 행 생성
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).apply {
            setCellValue("단어")
            cellStyle = headerStyle
        }
        headerRow.createCell(1).apply {
            setCellValue("뜻")
            cellStyle = headerStyle
        }

        if (hasDescription) {
            headerRow.createCell(2).apply {
                setCellValue("부가설명")
                cellStyle = headerStyle
            }
        }

        // 데이터 행 생성
        words.forEachIndexed { index, word ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(word.word)
            row.createCell(1).setCellValue(word.mean)

            if (hasDescription) {
                // reflection으로 description 값 가져오기
                try {
                    val field = word::class.java.getDeclaredField("description")
                    field.isAccessible = true
                    val description = field.get(word) as? String
                    row.createCell(2).setCellValue(description ?: "")
                } catch (e: Exception) {
                    row.createCell(2).setCellValue("")
                }
            }
        }

        // 컬럼 너비 수동 설정 (autoSizeColumn은 Android에서 AWT 오류 발생)
        sheet.setColumnWidth(0, 5000)  // 단어 컬럼: 약 20자 정도
        sheet.setColumnWidth(1, 8000)  // 뜻 컬럼: 약 30자 정도
        if (hasDescription) {
            sheet.setColumnWidth(2, 6000)  // 부가설명 컬럼: 약 24자 정도
        }

        return workbook
    }

    private fun openExcelFile(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // 엑셀 앱이 없는 경우 공유
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "단어장 공유"))
        }
    }
}