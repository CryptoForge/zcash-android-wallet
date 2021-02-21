package cash.z.ecc.android.ui.scan

import android.content.ContentValues.TAG
import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import cash.z.ecc.android.sdk.ext.twig
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.common.HybridBinarizer

class QrAnalyzer(val scanCallback: (qrContent: String, image: ImageProxy) -> Unit) :
    ImageAnalysis.Analyzer {
    
    var enabled = true
    var inverted = false

    private val reader =  MultiFormatReader()

    override fun analyze(image: ImageProxy) {
        if (!enabled) return

        //YUV_420 is normally the input type here, but other YUV types are also supported in theory
        if (ImageFormat.YUV_420_888 != image.format && ImageFormat.YUV_422_888 != image.format && ImageFormat.YUV_444_888 != image.format) {
            Log.e(TAG, "Unexpected format: ${image.format}")
           // listener.onNoResult()
            return
        }
        val byteBuffer = image?.planes?.firstOrNull()?.buffer
        if (byteBuffer == null) {
          //  listener.onNoResult()
            return
        }

        val data = ByteArray(byteBuffer.remaining()).also { byteBuffer.get(it) }

        val width = image.width
        val height = image.height

        val source = PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false).let {
            if (inverted) it.invert() else it
        }
        val bitmap = BinaryBitmap(HybridBinarizer(source))


            try {
                val result = reader.decodeWithState(bitmap)
                onImageScan(result.toString(), image)

            } catch (e: Exception) {
                image.close()
                twig("Error Scanning:{${e}}")
            }
    }

    private fun onImageScan(result: String, image: ImageProxy) {
        scanCallback(result, image) ?: runCatching { image.close() }
    }

    private fun onImageScanFailure(e: Exception) {
        twig("Warning: Image scan failed")
    }


}
