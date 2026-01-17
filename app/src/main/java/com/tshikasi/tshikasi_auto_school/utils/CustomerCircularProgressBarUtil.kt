package com.tshikasi.tshikasi_auto_school.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tshikasi.tshikasi_auto_school.R

@Composable
fun CustomerCircularProgressBarUtil(text:String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        CustomCircularProgressWithImageAndTextIndeterminate(
            text = text, //serviceProviderViewModel.loadingMessage.value!!,
            size = 90.dp,
            strokeWidth = 18.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
            backgroundColor = MaterialTheme.colorScheme.primary,
            image = painterResource(id = R.drawable.baseline_cancel_24)
        )
    }
}
@Composable
private fun CustomCircularProgressWithImageAndTextIndeterminate(
    size: Dp,
    strokeWidth: Dp,
    color: Color,
    backgroundColor: Color,
    image: Painter,
    text: String,
    imageSizeFraction: Float = 0.6f,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            // Fundo (cinza)
            CircularProgressIndicator(
                color = backgroundColor,
                strokeWidth = strokeWidth,
                progress = 1f
            )

            // Barra girando (cor principal)
            CircularProgressIndicator(
                color = color,
                strokeWidth = strokeWidth,
                modifier = Modifier.size(size)
            )

            // Imagem no centro
            /* Image(
                 painter = image,
                 contentDescription = null,
                 modifier = Modifier.size(size * imageSizeFraction)
             )*/
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto abaixo
        Text(
            text = text,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomLinearProgressWithUploadImageTextUtil(
    uploadProgress: Float,
    loadingMessage: String,
    ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        CustomLinearProgressWithUploadImageText(uploadProgress = uploadProgress, loadingMessage = loadingMessage)
    }
}

@Composable
private fun CustomLinearProgressWithUploadImageText(
    uploadProgress: Float,
    loadingMessage: String,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = Color.LightGray,
    showPercentage: Boolean = true,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator(
            progress = { uploadProgress / 100f },
            color = progressColor,
            trackColor = backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (showPercentage) {
            /*Text(
                text = stringResource(R.string.uploading_image_progress, uploadProgress.toInt()),
                style = textStyle
            )*/
            Text(
                text = loadingMessage,
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

       /* Text(
            text = loadingMessage,
            style = textStyle,
            textAlign = TextAlign.Center
        )*/
    }
}


