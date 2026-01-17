package com.tshikasi.tshikasi_auto_school.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Comprime com nível de agressividade configurável
 */
suspend fun ImageCompressionUtil(
    context: Context,
    imageUri: Uri,
    level: CompressionLevel = CompressionLevel.AGGRESSIVE,
    onProgress: (CompressionProgress) -> Unit = {}
): Uri = withContext(Dispatchers.IO) {

    Log.d("CompressionLevel", "Usando nível: ${level.name} (alvo: ${level.targetSizeKB}KB)")

    return@withContext compressAndValidateSingleImage(
        context = context,
        imageUri = imageUri,
        targetSizeKB = level.targetSizeKB,
        onProgress = onProgress
    )
}


/**
 * Comprime com nível de agressividade configurável
 */
suspend fun ImageListCompressionUtil(
context: Context,
imageUriList: List<Uri>,
level: CompressionLevel = CompressionLevel.AGGRESSIVE,
onProgress: (CompressionProgress) -> Unit = {}
): CompressionBatchResult = withContext(Dispatchers.IO) {
    Log.d("CompressionLevel", "Usando nível: ${level.name} (alvo: ${level.targetSizeKB}KB)")

    compressAndValidateMultipleImages(
        context = context,
        imageUris = imageUriList,
        targetSizeKB = level.targetSizeKB,
        onProgress = { multi ->
            // 🔄 Converter MultipleCompressionProgress → CompressionProgress
            val mapped = CompressionProgress(
                currentImage = multi.currentImageIndex,
                totalImages = multi.totalImages,
                progress = multi.overallProgress,
                currentImageName = multi.currentImageName,
                message = multi.currentImageName
            )
            onProgress(mapped)
        }
    )

    // aqui você retorna o objeto que quiser (ajustei para CompressionBatchResult
    
}

data class VideoCompressionBatchResult(
    val compressedUris: List<Uri>,
    val failedVideos: List<Pair<Uri?, String>>,
    val totalOriginalSizeMB: Float,
    val totalCompressedSizeMB: Float,
    val compressionRatio: Float
)



/**
 * Comprime imagens de forma agressiva para atingir menos de 20KB
 */
suspend fun compressImages(
    context: Context,
    imageUris: List<Uri>,
    targetSizeKB: Float = 20f,
    onProgress: (Int, Int) -> Unit = { _, _ -> }
): List<CompressedImageResult> = withContext(Dispatchers.IO) {

    val results = mutableListOf<CompressedImageResult>()

    imageUris.forEachIndexed { index, uri ->
        onProgress(index, imageUris.size)

        val result = compressSingleImageAggressive(context, uri, targetSizeKB)
        results.add(result)
    }

    onProgress(imageUris.size, imageUris.size)
    results
}

/**
 * Comprime uma única imagem de forma muito agressiva
 */
private suspend fun compressSingleImageAggressive(
    context: Context,
    uri: Uri,
    targetSizeKB: Float = 20f
): CompressedImageResult = withContext(Dispatchers.IO) {

    try {
        Log.d("AggressiveCompression", "Starting compression for URI: $uri")

        // 1. Valida o tamanho original
        val originalSize = getFileSizeFromUri(context, uri)
        Log.d("AggressiveCompression", "Original size: ${originalSize / 1024f} KB")

        // 2. Carrega bitmap com sample size para economizar memória
        val originalBitmap = loadBitmapWithSampling(context, uri)
            ?: return@withContext CompressedImageResult.Error("Não foi possível carregar a imagem")

        Log.d("AggressiveCompression", "Original bitmap: ${originalBitmap.width}x${originalBitmap.height}")

        // 3. Compressão agressiva em múltiplas etapas
        val compressedFile = performAggressiveCompression(
            context = context,
            bitmap = originalBitmap,
            targetSizeKB = targetSizeKB,
            originalUri = uri
        )

        originalBitmap.recycle()

        val finalSizeKB = compressedFile.length() / 1024f
        Log.d("AggressiveCompression", "Final size: ${finalSizeKB} KB")

        CompressedImageResult.Success(
            originalUri = uri,
            compressedUri = Uri.fromFile(compressedFile),
            originalSizeKB = originalSize / 1024f,
            compressedSizeKB = finalSizeKB,
            compressionRatio = (originalSize / 1024f) / finalSizeKB
        )

    } catch (e: Exception) {
        Log.e("AggressiveCompression", "Error compressing image", e)
        CompressedImageResult.Error("Erro na compressão: ${e.message}")
    }
}

/**
 * Carrega bitmap com sampling para economizar memória
 */
private fun loadBitmapWithSampling(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)

        // Primeiro, obtém as dimensões sem carregar a imagem
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        // Calcula sample size para reduzir uso de memória
        val sampleSize = calculateSampleSize(options.outWidth, options.outHeight, 1024, 1024)

        // Carrega a imagem com sample size
        val finalInputStream = context.contentResolver.openInputStream(uri)
        val finalOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.RGB_565 // Usa menos memória
        }

        val bitmap = BitmapFactory.decodeStream(finalInputStream, null, finalOptions)
        finalInputStream?.close()
        bitmap
    } catch (e: Exception) {
        Log.e("AggressiveCompression", "Error loading bitmap", e)
        null
    }
}

/**
 * Calcula o sample size ideal
 */
private fun calculateSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
    var sampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while ((halfHeight / sampleSize) >= reqHeight && (halfWidth / sampleSize) >= reqWidth) {
            sampleSize *= 2
        }
    }

    return sampleSize
}

/**
 * Configurações de compressão para diferentes níveis de agressividade
 */
enum class CompressionLevel(
    val targetSizeKB: Float,
    val minQuality: Int,
    val minScaleFactor: Float,
    val description: String
) {
    ULTRA_AGGRESSIVE(5f, 5, 0.05f, "Máxima compressão - Para thumbnails pequenos"),
    VERY_AGGRESSIVE(15f, 10, 0.1f, "Muito agressivo - Para previews"),
    AGGRESSIVE(25f, 15, 0.15f, "Agressivo - Para fotos de perfil"),
    MODERATE(50f, 25, 0.25f, "Moderado - Para galeria"),
    GENTLE(100f, 40, 0.4f, "Suave - Para compartilhamento"),
    HIGH(200f, 60, 0.7f, "Alta qualidade - Quase original"),
    VERY_HIGH(400f, 75, 0.85f, "Muito alta - Qualidade profissional"),
    MAXIMUM(Float.MAX_VALUE, 90, 1.0f, "Sem compressão visível - Original")
}
enum class VideoCompressionLevel(
    val targetSizeMB: Float,
    val bitrate: String,
    val resolution: VideoResolution,
    val fps: Int,
    val audioQuality: AudioQuality,
    val description: String
) {
    ULTRA_AGGRESSIVE(
        targetSizeMB = 5f,
        bitrate = "300k",
        resolution = VideoResolution.LOW,
        fps = 20,
        audioQuality = AudioQuality.LOW,
        description = "Máxima compressão - Para previews rápidos"
    ),

    VERY_AGGRESSIVE(
        targetSizeMB = 10f,
        bitrate = "500k",
        resolution = VideoResolution.LOW,
        fps = 24,
        audioQuality = AudioQuality.LOW,
        description = "Muito agressivo - Para compartilhamento rápido"
    ),

    AGGRESSIVE(
        targetSizeMB = 20f,
        bitrate = "800k",
        resolution = VideoResolution.MEDIUM,
        fps = 24,
        audioQuality = AudioQuality.MEDIUM,
        description = "Agressivo - Para redes sociais"
    ),

    MODERATE(
        targetSizeMB = 40f,
        bitrate = "1200k",
        resolution = VideoResolution.MEDIUM,
        fps = 30,
        audioQuality = AudioQuality.MEDIUM,
        description = "Moderado - Para WhatsApp/Telegram"
    ),

    GENTLE(
        targetSizeMB = 80f,
        bitrate = "2000k",
        resolution = VideoResolution.HIGH,
        fps = 30,
        audioQuality = AudioQuality.HIGH,
        description = "Suave - Para armazenamento local"
    ),

    HIGH(
        targetSizeMB = 150f,
        bitrate = "3500k",
        resolution = VideoResolution.HIGH,
        fps = 30,
        audioQuality = AudioQuality.HIGH,
        description = "Alta qualidade - Para backup"
    ),

    VERY_HIGH(
        targetSizeMB = 300f,
        bitrate = "5000k",
        resolution = VideoResolution.FULL_HD,
        fps = 60,
        audioQuality = AudioQuality.VERY_HIGH,
        description = "Muito alta - Qualidade profissional"
    ),

    MAXIMUM(
        targetSizeMB = Float.MAX_VALUE,
        bitrate = "8000k",
        resolution = VideoResolution.ORIGINAL,
        fps = 60,
        audioQuality = AudioQuality.VERY_HIGH,
        description = "Sem compressão perceptível - Quase original"
    )
}

enum class VideoResolution(val width: Int, val height: Int, val scale: String) {
    LOW(480, 360, "480x360"),           // ~0.17 megapixels
    MEDIUM(640, 480, "640x480"),        // ~0.3 megapixels (VGA)
    HIGH(1280, 720, "1280x720"),        // ~0.9 megapixels (HD)
    FULL_HD(1920, 1080, "1920x1080"),   // ~2 megapixels (Full HD)
    ORIGINAL(-1, -1, "original")        // Mantém resolução original
}

enum class AudioQuality(val bitrate: String, val sampleRate: String) {
    LOW("32k", "22050"),        // Telefone
    MEDIUM("64k", "44100"),     // Padrão
    HIGH("128k", "44100"),      // Boa qualidade
    VERY_HIGH("192k", "48000")  // Qualidade de estúdio
}


/**
 * Versão melhorada da compressão agressiva com mais controle
 */
private suspend fun performAggressiveCompressionWithLevel(
    context: Context,
    bitmap: Bitmap,
    level: CompressionLevel,
    originalUri: Uri
): File = withContext(Dispatchers.IO) {

    val targetSizeBytes = (level.targetSizeKB * 1024).toLong()
    var currentBitmap = bitmap
    var attempt = 0
    val maxAttempts = 12

    // Configurações baseadas no nível
    var quality = when (level) {
        CompressionLevel.ULTRA_AGGRESSIVE -> 15
        CompressionLevel.VERY_AGGRESSIVE -> 25
        CompressionLevel.AGGRESSIVE -> 35
        CompressionLevel.MODERATE -> 50
        CompressionLevel.GENTLE -> 65
        CompressionLevel.HIGH -> 75
        CompressionLevel.VERY_HIGH -> 85
        CompressionLevel.MAXIMUM ->95
    }

    var scaleFactor = 1.0f

    // Calcula fator de escala inicial mais conservador para níveis menos agressivos
    val estimatedCompressionNeeded = targetSizeBytes.toFloat() / getEstimatedBitmapSize(bitmap)
    if (estimatedCompressionNeeded < 1.0f) {
        scaleFactor = when (level) {
            CompressionLevel.ULTRA_AGGRESSIVE -> sqrt(estimatedCompressionNeeded) * 0.8f
            CompressionLevel.VERY_AGGRESSIVE -> sqrt(estimatedCompressionNeeded) * 0.9f
            CompressionLevel.AGGRESSIVE -> sqrt(estimatedCompressionNeeded)
            CompressionLevel.MODERATE -> sqrt(estimatedCompressionNeeded) * 1.1f
            CompressionLevel.GENTLE -> sqrt(estimatedCompressionNeeded) * 1.2f
            CompressionLevel.HIGH -> (sqrt(estimatedCompressionNeeded) * 1.1f).coerceAtLeast(0.9f)
            CompressionLevel.VERY_HIGH -> (sqrt(estimatedCompressionNeeded) * 1.15f).coerceAtLeast(0.95f)
            CompressionLevel.MAXIMUM -> 1.0f // nunca redimensiona
        }.coerceAtMost(1.0f)
    }

    Log.d("CompressionLevel",
        "Configurações iniciais - Qualidade: $quality, Escala: $scaleFactor, " +
                "Limites - MinQuality: ${level.minQuality}, MinScale: ${level.minScaleFactor}")

    while (attempt < maxAttempts) {
        attempt++

        // 1. Redimensiona se necessário
        val scaledBitmap = if (scaleFactor < 1.0f) {
            val newWidth = (currentBitmap.width * scaleFactor).toInt().coerceAtLeast(30)
            val newHeight = (currentBitmap.height * scaleFactor).toInt().coerceAtLeast(30)

            Log.d("CompressionLevel",
                "Tentativa $attempt: Redimensionando para ${newWidth}x${newHeight}, qualidade: $quality")

            val scaledBitmap = Bitmap.createScaledBitmap(currentBitmap, newWidth, newHeight, true)
            if (currentBitmap != bitmap) currentBitmap.recycle()
            scaledBitmap
        } else {
            currentBitmap
        }

        // 2. Comprime com qualidade atual
        val byteArray = compressBitmapToByteArray(scaledBitmap, quality)
        val currentSizeBytes = byteArray.size.toLong()
        val currentSizeKB = currentSizeBytes / 1024f

        Log.d("CompressionLevel",
            "Tentativa $attempt: Tamanho = ${String.format("%.1f", currentSizeKB)} KB " +
                    "(alvo: ${level.targetSizeKB} KB)")

        // 3. Verifica se atingiu o objetivo
        if (currentSizeBytes <= targetSizeBytes) {
            val outputFile = createTempFile(context, originalUri)
            FileOutputStream(outputFile).use { fos ->
                fos.write(byteArray)
            }

            if (scaledBitmap != bitmap) scaledBitmap.recycle()
            Log.d("CompressionLevel",
                "✅ Sucesso! Tamanho final: ${String.format("%.1f", currentSizeKB)} KB " +
                        "em $attempt tentativas (${level.name})")
            return@withContext outputFile
        }

        // 4. Ajusta parâmetros baseado no nível de agressividade
        val overTarget = currentSizeBytes.toFloat() / targetSizeBytes

        when {
            overTarget > 4.0f -> {
                scaleFactor *= if (level == CompressionLevel.ULTRA_AGGRESSIVE) 0.6f else 0.7f
                quality = max(level.minQuality, quality - 20)
            }
            overTarget > 2.5f -> {
                scaleFactor *= if (level == CompressionLevel.ULTRA_AGGRESSIVE) 0.7f else 0.8f
                quality = max(level.minQuality, quality - 15)
            }
            overTarget > 1.5f -> {
                scaleFactor *= 0.85f
                quality = max(level.minQuality, quality - 10)
            }
            else -> {
                // Ajuste fino
                scaleFactor *= 0.95f
                quality = max(level.minQuality, quality - 5)
            }
        }

        // Aplica limites baseados no nível
        scaleFactor = max(level.minScaleFactor, scaleFactor)
        quality = max(level.minQuality, quality)

        // Se atingiu os limites, para
        if (scaleFactor <= level.minScaleFactor && quality <= level.minQuality) {
            Log.w("CompressionLevel", "Atingiu limites mínimos do nível ${level.name}")
            break
        }

        currentBitmap = scaledBitmap
    }

    // Última tentativa com configurações mínimas
    val finalByteArray = compressBitmapToByteArray(currentBitmap, level.minQuality)
    val outputFile = createTempFile(context, originalUri)
    FileOutputStream(outputFile).use { fos ->
        fos.write(finalByteArray)
    }

    if (currentBitmap != bitmap) currentBitmap.recycle()
    Log.w("CompressionLevel",
        "Máximo de tentativas atingido. Tamanho final: ${finalByteArray.size / 1024f} KB")

    outputFile
}

/**
 * Comprime bitmap para byte array
 */
private fun compressBitmapToByteArray(bitmap: Bitmap, quality: Int): ByteArray {
    val outputStream = ByteArrayOutputStream()

    // Usa JPEG para melhor compressão
    val format = if (hasTransparency(bitmap)) {
        Bitmap.CompressFormat.PNG
    } else {
        Bitmap.CompressFormat.JPEG
    }

    bitmap.compress(format, quality, outputStream)
    return outputStream.toByteArray()
}

/**
 * Verifica se o bitmap tem transparência
 */
private fun hasTransparency(bitmap: Bitmap): Boolean {
    return bitmap.config == Bitmap.Config.ARGB_8888 && !bitmap.isRecycled
}

/**
 * Estima o tamanho do bitmap em bytes
 */
private fun getEstimatedBitmapSize(bitmap: Bitmap): Long {
    return (bitmap.width * bitmap.height * 4).toLong() // ARGB_8888 = 4 bytes per pixel
}

/**
 * Obtém o tamanho do arquivo pela URI
 */
private fun getFileSizeFromUri(context: Context, uri: Uri): Long {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.available().toLong()
        } ?: 0L
    } catch (e: Exception) {
        0L
    }
}

private suspend fun performAggressiveCompression(
    context: Context,
    bitmap: Bitmap,
    targetSizeKB: Float,
    originalUri: Uri
): File = withContext(Dispatchers.IO) {

    val targetSizeBytes = (targetSizeKB * 1024).toLong()
    var currentBitmap = bitmap
    var attempt = 0
    val maxAttempts = 10

    // Configurações iniciais agressivas
    var quality = 85
    var scaleFactor = 1.0f

    // Calcula fator de escala inicial baseado no tamanho alvo
    val estimatedCompressionNeeded = targetSizeBytes.toFloat() / getEstimatedBitmapSize(bitmap)
    if (estimatedCompressionNeeded < 1.0f) {
        scaleFactor = sqrt(estimatedCompressionNeeded) // Redução mais agressiva
    }

    Log.d("AggressiveCompression", "Initial scale factor: $scaleFactor, estimated compression needed: $estimatedCompressionNeeded")

    while (attempt < maxAttempts) {
        attempt++

        // 1. Redimensiona se necessário
        val scaledBitmap = if (scaleFactor < 1.0f) {
            val newWidth = (currentBitmap.width * scaleFactor).toInt().coerceAtLeast(50)
            val newHeight = (currentBitmap.height * scaleFactor).toInt().coerceAtLeast(50)

            Log.d("AggressiveCompression", "Attempt $attempt: Scaling to ${newWidth}x${newHeight}, quality: $quality")

            val scaledBitmap = Bitmap.createScaledBitmap(currentBitmap, newWidth, newHeight, true)
            if (currentBitmap != bitmap) currentBitmap.recycle()
            scaledBitmap
        } else {
            currentBitmap
        }

        // 2. Comprime com qualidade atual
        val byteArray = compressBitmapToByteArray(scaledBitmap, quality)
        val currentSizeBytes = byteArray.size.toLong()

        Log.d("AggressiveCompression", "Attempt $attempt: Size = ${currentSizeBytes / 1024f} KB (target: $targetSizeKB KB)")

        // 3. Verifica se atingiu o objetivo
        if (currentSizeBytes <= targetSizeBytes) {
            val outputFile = createTempFile(context, originalUri)
            FileOutputStream(outputFile).use { fos ->
                fos.write(byteArray)
            }

            if (scaledBitmap != bitmap) scaledBitmap.recycle()
            Log.d("AggressiveCompression", "Success! Final size: ${currentSizeBytes / 1024f} KB in $attempt attempts")
            return@withContext outputFile
        }

        // 4. Ajusta parâmetros para próxima tentativa
        when {
            currentSizeBytes > targetSizeBytes * 3 -> {
                // Muito grande: reduz drasticamente o tamanho
                scaleFactor *= 0.7f
                quality = max(30, quality - 15)
            }
            currentSizeBytes > targetSizeBytes * 2 -> {
                // Grande: reduz moderadamente
                scaleFactor *= 0.8f
                quality = max(25, quality - 10)
            }
            currentSizeBytes > targetSizeBytes * 1.5 -> {
                // Um pouco grande: ajuste fino
                scaleFactor *= 0.9f
                quality = max(20, quality - 5)
            }
            else -> {
                // Muito próximo: ajuste mínimo
                quality = max(15, quality - 3)
            }
        }

        // Limita valores mínimos
        scaleFactor = max(0.1f, scaleFactor)
        quality = max(10, quality)

        currentBitmap = scaledBitmap
    }

    // Se chegou aqui, usa a última versão disponível
    val finalByteArray = compressBitmapToByteArray(currentBitmap, 10) // Qualidade mínima
    val outputFile = createTempFile(context, originalUri)
    FileOutputStream(outputFile).use { fos ->
        fos.write(finalByteArray)
    }

    if (currentBitmap != bitmap) currentBitmap.recycle()
    Log.w("AggressiveCompression", "Max attempts reached. Final size: ${finalByteArray.size / 1024f} KB")

    outputFile
}


/**
 * Cria arquivo temporário para a imagem comprimida
 */
private fun createTempFile(context: Context, originalUri: Uri): File {
    val timestamp = System.currentTimeMillis()
    val filename = "compressed_${timestamp}.jpg"
    return File(context.cacheDir, filename)
}

/**
 * Versão otimizada da função de validação e compressão para UMA imagem
 */
suspend fun compressAndValidateSingleImage(
    context: Context,
    imageUri: Uri,
    targetSizeKB: Float = 20f,
    onProgress: (CompressionProgress) -> Unit = {}
): Uri = withContext(Dispatchers.IO) {

    onProgress(CompressionProgress(0, 1, progress = 0f, message = "", currentImageName ="Iniciando compressão..."))

    val compressionResults = compressImages(
        context = context,
        imageUris = listOf(imageUri),
        targetSizeKB = targetSizeKB,
        onProgress = { current, total ->
            onProgress(CompressionProgress(
                currentImage = current,
                totalImages = total,
                message = "",
                progress = targetSizeKB,
                currentImageName = "Comprimindo para ${targetSizeKB}KB..."
            ))
        }
    )

    val result = compressionResults.firstOrNull()
        ?: throw Exception("Erro inesperado: Nenhum resultado de compressão.")

    val compressedUri = when (result) {
        is CompressedImageResult.Success -> {
            Log.d("AggressiveCompression",
                "Compressão concluída: ${result.originalSizeKB}KB -> ${result.compressedSizeKB}KB " +
                        "(${String.format("%.1fx", result.compressionRatio)} menor)")

            onProgress(CompressionProgress(currentImage = 1, totalImages = 1, progress = result.compressionRatio, currentImageName =  "Compressão concluída!"))
            result.compressedUri
        }
        is CompressedImageResult.Error -> {
            throw Exception("Erro na compressão: ${result.message}")
        }
    }

    compressedUri
}

/**
 * Comprime e valida MÚLTIPLAS imagens de forma agressiva
 */
suspend fun compressAndValidateMultipleImages1(
    context: Context,
    imageUris: List<Uri?>,
    targetSizeKB: Float = 20f,
    onProgress: (MultipleCompressionProgress) -> Unit = {},
    onImageComplete: (CompressedImageResult, Int, Int) -> Unit = { _, _, _ -> }
): List<Uri> = withContext(Dispatchers.IO) {

    if (imageUris.isEmpty()) {
        return@withContext emptyList()
    }

    onProgress(MultipleCompressionProgress(
        totalImages = imageUris.size,
        completedImages = 0,
        currentImageIndex = 0,
        currentImageName = "Preparando compressão...",
        overallProgress = 0f,
        totalOriginalSizeKB = 0f,
        totalCompressedSizeKB = 0f
    ))

    val compressedUris = mutableListOf<Uri>()
    var totalOriginalSize = 0f
    var totalCompressedSize = 0f
    val failedImages = mutableListOf<Pair<Uri, String>>()

    imageUris.forEachIndexed { index, uri ->
        try {
            onProgress(MultipleCompressionProgress(
                totalImages = imageUris.size,
                completedImages = index,
                currentImageIndex = index,
                currentImageName = "Imagem ${index + 1} de ${imageUris.size}",
                overallProgress = (index.toFloat() / imageUris.size) * 100f,
                totalOriginalSizeKB = totalOriginalSize,
                totalCompressedSizeKB = totalCompressedSize
            ))

            Log.d("MultipleCompression", "Comprimindo imagem ${index + 1}/${imageUris.size}: $uri")

            // Comprime uma imagem individual
            val result = uri?.let { compressSingleImageAggressive(context, it, targetSizeKB) }

            when (result) {
                is CompressedImageResult.Success -> {
                    compressedUris.add(result.compressedUri)
                    totalOriginalSize += result.originalSizeKB
                    totalCompressedSize += result.compressedSizeKB

                    Log.d("MultipleCompression",
                        "Sucesso ${index + 1}/${imageUris.size}: " +
                                "${result.originalSizeKB}KB -> ${result.compressedSizeKB}KB")

                    onImageComplete(result, index, imageUris.size)
                }
                is CompressedImageResult.Error -> {
                    failedImages.add(uri to result.message)
                    Log.e("MultipleCompression",
                        "Falha ${index + 1}/${imageUris.size}: ${result.message}")

                    onImageComplete(result, index, imageUris.size)
                }

                null -> {}
            }

        } catch (e: Exception) {
            failedImages.add((uri to "Erro inesperado: ${e.message}") as Pair<Uri, String>)
            Log.e("MultipleCompression", "Erro na imagem ${index + 1}/${imageUris.size}", e)

            onImageComplete(CompressedImageResult.Error(e.message ?: "Erro desconhecido"), index, imageUris.size)
        }
    }

    // Progress final
    val finalProgress = MultipleCompressionProgress(
        totalImages = imageUris.size,
        completedImages = imageUris.size,
        currentImageIndex = imageUris.size - 1,
        currentImageName = "Compressão finalizada!",
        overallProgress = 100f,
        totalOriginalSizeKB = totalOriginalSize,
        totalCompressedSizeKB = totalCompressedSize,
        successCount = compressedUris.size,
        failureCount = failedImages.size,
        compressionRatio = if (totalCompressedSize > 0) totalOriginalSize / totalCompressedSize else 0f
    )

    onProgress(finalProgress)

    // Log do resumo final
    Log.d("MultipleCompression", """
        ====== RESUMO DA COMPRESSÃO ======
        Total de imagens: ${imageUris.size}
        Sucessos: ${compressedUris.size}
        Falhas: ${failedImages.size}
        Tamanho original total: ${String.format("%.1f", totalOriginalSize)} KB
        Tamanho comprimido total: ${String.format("%.1f", totalCompressedSize)} KB
        Taxa de compressão geral: ${String.format("%.1fx", finalProgress.compressionRatio)}
        Economia de espaço: ${String.format("%.1f", ((totalOriginalSize - totalCompressedSize) / totalOriginalSize) * 100)}%
        ===================================
    """.trimIndent())

    if (failedImages.isNotEmpty()) {
        Log.w("MultipleCompression", "Imagens que falharam:")
        failedImages.forEachIndexed { index, (uri, error) ->
            Log.w("MultipleCompression", "${index + 1}. $uri - $error")
        }
    }

    // Retorna apenas as URIs das imagens que foram comprimidas com sucesso
    compressedUris
}

suspend fun compressAndValidateMultipleImages(
    context: Context,
    imageUris: List<Uri?>,
    targetSizeKB: Float = 20f,
    onProgress: (MultipleCompressionProgress) -> Unit = {},
    onImageComplete: (CompressedImageResult, Int, Int) -> Unit = { _, _, _ -> }
): CompressionBatchResult = withContext(Dispatchers.IO) {

    if (imageUris.isEmpty()) {
        return@withContext CompressionBatchResult(emptyList(), emptyList(), 0f, 0f, 0f)
    }

    val compressedUris = mutableListOf<Uri>()
    var totalOriginalSize = 0f
    var totalCompressedSize = 0f
    val failedImages = mutableListOf<Pair<Uri?, String>>()

    imageUris.forEachIndexed { index, uri ->
        try {
            val result = uri?.let { compressSingleImageAggressive(context, it, targetSizeKB) }

            when (result) {
                is CompressedImageResult.Success -> {
                    compressedUris.add(result.compressedUri)
                    totalOriginalSize += result.originalSizeKB
                    totalCompressedSize += result.compressedSizeKB
                }
                is CompressedImageResult.Error -> {
                    failedImages.add(uri to result.message)
                }
                null -> {}
            }
        } catch (e: Exception) {
            failedImages.add(uri to "Erro inesperado: ${e.message}")
        }
    }

    CompressionBatchResult(
        compressedUris = compressedUris,
        failedImages = failedImages,
        totalOriginalSizeKB = totalOriginalSize,
        totalCompressedSizeKB = totalCompressedSize,
        compressionRatio = if (totalCompressedSize > 0) totalOriginalSize / totalCompressedSize else 0f
    )
}



/**
 * Função utilitária para comprimir múltiplas imagens com callbacks simplificados
 */
/*
suspend fun compressMultipleImagesSimple(
    context: Context,
    imageUris: List<Uri>,
    targetSizeKB: Float = 20f,
    onProgressUpdate: (completed: Int, total: Int, currentImageName: String) -> Unit = { _, _, _ -> }
): CompressMultipleResult = withContext(Dispatchers.IO) {

    val startTime = System.currentTimeMillis()
    val compressedUris = mutableListOf<Uri>()
    val errors = mutableListOf<String>()
    var totalOriginalSize = 0f
    var totalCompressedSize = 0f

    val results = compressAndValidateMultipleImages(
        context = context,
        imageUris = imageUris,
        targetSizeKB = targetSizeKB,
        onProgress = { progress ->
            onProgressUpdate(
                progress.completedImages,
                progress.totalImages,
                progress.currentImageName
            )
        },
        onImageComplete = { result, index, total ->
            when (result) {
                is CompressedImageResult.Success -> {
                    totalOriginalSize += result.originalSizeKB
                    totalCompressedSize += result.compressedSizeKB
                }
                is CompressedImageResult.Error -> {
                    errors.add("Imagem ${index + 1}: ${result.message}")
                }
            }
        }
    )

    val processingTimeMs = System.currentTimeMillis() - startTime

    CompressMultipleResult(
        compressedUris = results,
        totalOriginalSizeKB = totalOriginalSize,
        totalCompressedSizeKB = totalCompressedSize,
        successCount = results.size,
        failureCount = imageUris.size - results.size,
        errors = errors,
        processingTimeMs = processingTimeMs,
        averageCompressionRatio = if (totalCompressedSize > 0) totalOriginalSize / totalCompressedSize else 0f
    )
}

*/

/**
 * Data class para progresso da compressão de UMA imagem
 */
data class CompressionProgress(
    val currentImage: Int,
    val totalImages: Int,
    val currentImageName: String,
    val progress: Float,
    val message: String? = null
)



/**
 * Data class para progresso da compressão de MÚLTIPLAS imagens
 */
data class MultipleCompressionProgress(
    val totalImages: Int,
    val completedImages: Int,
    val currentImageIndex: Int,
    val currentImageName: String,
    val overallProgress: Float, // 0-100%
    val totalOriginalSizeKB: Float,
    val totalCompressedSizeKB: Float,
    val successCount: Int = completedImages,
    val failureCount: Int = 0,
    val compressionRatio: Float = 0f
) {
    val isCompleted: Boolean get() = completedImages >= totalImages
    val spaceSavedKB: Float get() = totalOriginalSizeKB - totalCompressedSizeKB
    val spaceSavedPercentage: Float get() = if (totalOriginalSizeKB > 0) (spaceSavedKB / totalOriginalSizeKB) * 100f else 0f
}

/**
 * Data class para resultado final da compressão múltipla
 */
data class CompressMultipleResult(
    val compressedUris: List<Uri>,
    val totalOriginalSizeKB: Float,
    val totalCompressedSizeKB: Float,
    val successCount: Int,
    val failureCount: Int,
    val errors: List<String>,
    val processingTimeMs: Long,
    val averageCompressionRatio: Float
) {
    val totalImages: Int get() = successCount + failureCount
    val successRate: Float get() = if (totalImages > 0) (successCount.toFloat() / totalImages) * 100f else 0f
    val spaceSavedKB: Float get() = totalOriginalSizeKB - totalCompressedSizeKB
    val spaceSavedPercentage: Float get() = if (totalOriginalSizeKB > 0) (spaceSavedKB / totalOriginalSizeKB) * 100f else 0f
    val averageProcessingTimePerImageMs: Long get() = if (totalImages > 0) processingTimeMs / totalImages else 0L

    fun getFormattedSummary(): String {
        return """
            📊 RESUMO DA COMPRESSÃO
            ═══════════════════════
            📁 Total de imagens: $totalImages
            ✅ Sucessos: $successCount (${String.format("%.1f%%", successRate)})
            ❌ Falhas: $failureCount
            
            📉 Tamanho original: ${String.format("%.1f KB", totalOriginalSizeKB)}
            📈 Tamanho final: ${String.format("%.1f KB", totalCompressedSizeKB)}
            🗜️  Taxa de compressão: ${String.format("%.1fx", averageCompressionRatio)}
            💾 Espaço economizado: ${String.format("%.1f KB (%.1f%%)", spaceSavedKB, spaceSavedPercentage)}
            
            ⏱️  Tempo total: ${processingTimeMs}ms
            ⏱️  Média por imagem: ${averageProcessingTimePerImageMs}ms
        """.trimIndent()
    }
}

/**
 * Sealed class para resultados da compressão
 */
sealed class CompressedImageResult {
    data class Success(
        val originalUri: Uri,
        val compressedUri: Uri,
        val originalSizeKB: Float,
        val compressedSizeKB: Float,
        val compressionRatio: Float
    ) : CompressedImageResult()

    data class Error(val message: String) : CompressedImageResult()
}

/**
 * Utilitário para comprimir imagens com diferentes estratégias
 */
object ImageCompressionHelper {

    /**
     * Comprime uma única imagem - Interface mais simples
     */
    suspend fun compressSingle(
        context: Context,
        imageUri: Uri,
        targetSizeKB: Float = 20f
    ): Result<Uri> = try {
        val compressedUri = compressAndValidateSingleImage(context, imageUri, targetSizeKB)
        Result.success(compressedUri)
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Comprime múltiplas imagens - Interface mais simples
     */
   /* suspend fun compressMultiple(
        context: Context,
        imageUris: List<Uri>,
        targetSizeKB: Float = 20f
    ): CompressMultipleResult {
        return compressMultipleImagesSimple(context, imageUris, targetSizeKB)
    }*/

    /**
     * Estima o tempo de compressão baseado no número de imagens
     */
    fun estimateCompressionTime(imageCount: Int): Long {
        // Baseado em testes: ~2-5 segundos por imagem dependendo do tamanho
        return imageCount * 3500L // 3.5 segundos por imagem em média
    }

    /**
     * Calcula o tamanho alvo ideal baseado no uso pretendido
     */
    fun calculateTargetSize(usage: ImageUsage): Float {
        return when (usage) {
            ImageUsage.THUMBNAIL -> 10f          // Para miniaturas
            ImageUsage.PROFILE_PICTURE -> 20f    // Para fotos de perfil
            ImageUsage.GALLERY_PREVIEW -> 50f    // Para preview de galeria
            ImageUsage.SOCIAL_MEDIA -> 100f      // Para redes sociais
            ImageUsage.EMAIL_ATTACHMENT -> 200f  // Para anexos de email
        }
    }

    /**
     * Valida se as URIs são válidas antes da compressão
     */
    suspend fun validateImages(
        context: Context,
        imageUris: List<Uri>
    ): ImageValidationSummary = withContext(Dispatchers.IO) {
        val validUris = mutableListOf<Uri>()
        val invalidUris = mutableListOf<Pair<Uri, String>>()
        var totalSizeKB = 0f

        imageUris.forEach { uri ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val size = inputStream.available().toLong()
                    inputStream.close()

                    if (size > 0) {
                        validUris.add(uri)
                        totalSizeKB += size / 1024f
                    } else {
                        invalidUris.add(uri to "Arquivo vazio")
                    }
                } else {
                    invalidUris.add(uri to "Não foi possível abrir o arquivo")
                }
            } catch (e: Exception) {
                invalidUris.add(uri to "Erro: ${e.message}")
            }
        }

        ImageValidationSummary(
            validUris = validUris,
            invalidUris = invalidUris,
            totalOriginalSizeKB = totalSizeKB
        )
    }
}

/**
 * Enum para diferentes tipos de uso da imagem
 */
enum class ImageUsage {
    THUMBNAIL,
    PROFILE_PICTURE,
    GALLERY_PREVIEW,
    SOCIAL_MEDIA,
    EMAIL_ATTACHMENT
}

/**
 * Data class para resumo de validação de imagens
 */
data class ImageValidationSummary(
    val validUris: List<Uri>,
    val invalidUris: List<Pair<Uri, String>>,
    val totalOriginalSizeKB: Float
) {
    val totalImages: Int get() = validUris.size + invalidUris.size
    val validCount: Int get() = validUris.size
    val invalidCount: Int get() = invalidUris.size
    val validationRate: Float get() = if (totalImages > 0) (validCount.toFloat() / totalImages) * 100f else 0f

    fun hasInvalidImages(): Boolean = invalidUris.isNotEmpty()
    fun hasValidImages(): Boolean = validUris.isNotEmpty()

    fun getInvalidImagesSummary(): String {
        return if (invalidUris.isEmpty()) {
            "✅ Todas as imagens são válidas"
        } else {
            buildString {
                appendLine("❌ Imagens inválidas encontradas:")
                invalidUris.forEachIndexed { index, (uri, error) ->
                    appendLine("${index + 1}. ${uri.lastPathSegment ?: "Imagem"}: $error")
                }
            }
        }
    }
}

data class CompressionBatchResult(
    val compressedUris: List<Uri>,
    val failedImages: List<Pair<Uri?, String>>,
    val totalOriginalSizeKB: Float,
    val totalCompressedSizeKB: Float,
    val compressionRatio: Float
)


/**
 * Função de extensão para facilitar o uso com ViewModel
 */
/*suspend fun List<Uri>.compressAll(
    context: Context,
    targetSizeKB: Float = 20f,
    onProgress: (MultipleCompressionProgress) -> Unit = {}
): List<Uri> {
    return compressAndValidateMultipleImages(
        context = context,
        imageUris = this,
        targetSizeKB = targetSizeKB,
        onProgress = onProgress
    )
}*/

/**
 * Função de extensão para uma única URI
 */
suspend fun Uri.compress(
    context: Context,
    targetSizeKB: Float = 20f,
    onProgress: (CompressionProgress) -> Unit = {}
): Uri {
    return compressAndValidateSingleImage(
        context = context,
        imageUri = this,
        targetSizeKB = targetSizeKB,
        onProgress = onProgress
    )
}



