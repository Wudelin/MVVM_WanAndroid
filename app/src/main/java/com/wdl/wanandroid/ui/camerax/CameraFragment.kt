package com.wdl.wanandroid.ui.camerax

import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentCameraBinding
import com.wdl.wanandroid.ui.MainActivity
import com.wdl.wanandroid.utils.WLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 * 自定义相机
 */

val EXTENSION_WHITELIST = arrayOf("JPG")

class CameraFragment : BaseFragment<FragmentCameraBinding>() {

    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preView: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var camera: Camera? = null
    private lateinit var cameraExecutor: ExecutorService

    private val displayManager: DisplayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        container = view as ConstraintLayout
        viewFinder = mBinding!!.viewFinder
        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        viewFinder.post {
            displayId = viewFinder.display.displayId

            updateCameraUI()

            bindCameraUseCases()
        }
    }

    /**
     * 声明并绑定预览
     */
    private fun bindCameraUseCases() {

        val metrics = DisplayMetrics().also {
            viewFinder.display.getMetrics(it)
        }
        WLogger.d("Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        WLogger.d("screenAspectRatio: $screenAspectRatio")

        // 旋转角度
        val rotation = viewFinder.display.rotation

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            preView = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

            imageCapture =
                ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .setTargetAspectRatio(screenAspectRatio)
                    .setTargetRotation(rotation)
                    .build()

            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()

            try {
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preView, imageCapture)

                // 读取相机流到预览界面
                preView?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
            } catch (e: Exception) {

            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     * 返回最佳尺寸
     */
    private fun aspectRatio(widthPixels: Int, heightPixels: Int): Int {
        val previewRatio = max(widthPixels, heightPixels) / min(widthPixels, heightPixels)
        return if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }

    private fun updateCameraUI() {
        // Remove previous UI if any
        container.findViewById<ConstraintLayout>(R.id.camera_ui_container)?.let {
            container.removeView(it)
        }

        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        lifecycleScope.launch(Dispatchers.IO) {
            outputDirectory.listFiles { file ->
                EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
            }?.max()?.let {
                setGalleryThumbnail(Uri.fromFile(it))
            }
        }

        // 拍照键
        controls.findViewById<ImageButton>(R.id.camera_capture_button).setOnClickListener {
            imageCapture?.let { imageCapture ->
                // 创建保存文件
                val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
                val metadata = ImageCapture.Metadata().apply {
                    // 使用前置摄像头
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                // 创建包含文件+元数据的输出选项对象
                val outputOptions =
                    ImageCapture.OutputFileOptions.Builder(photoFile).setMetadata(metadata).build()

                // 拍照
                imageCapture.takePicture(
                    outputOptions,
                    cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                            WLogger.e("Photo capture succeeded: $savedUri")

                            // We can only change the foreground Drawable using API level 23+ API
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // Update the gallery thumbnail with latest picture taken
                                setGalleryThumbnail(savedUri)
                            }

                            // Implicit broadcasts will be ignored for devices running API level >= 24
                            // so if you only target API level 24+ you can remove this statement
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                requireActivity().sendBroadcast(
                                    Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri)
                                )
                            }
                            lifecycleScope.launch(Dispatchers.IO) {
                                // 插入图库
                                val mimeType = MimeTypeMap.getSingleton()
                                    .getMimeTypeFromExtension(savedUri.toFile().extension)
                                MediaScannerConnection.scanFile(
                                    requireContext(), arrayOf(savedUri.toString()),
                                    arrayOf(mimeType)
                                ) { _, uri ->
                                    WLogger.e("Image capture scanned into media store: $uri")
                                }
                            }

                            Navigation.findNavController(
                                requireActivity(), R.id.fragment_container_view
                            ).navigate(
                                CameraFragmentDirections
                                    .actionCameraFragmentToGalleryFragment(photoFile.absolutePath)
                            )

                        }

                        override fun onError(exception: ImageCaptureException) {
                            WLogger.e("Photo capture failed: ${exception.message}")
                        }

                    })
            }
        }


        // 切换摄像头
        controls.findViewById<ImageButton>(R.id.camera_switch_button).setOnClickListener {
            lensFacing = if (CameraSelector.LENS_FACING_BACK == lensFacing) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            bindCameraUseCases()
        }
    }

    /**
     * 取出拍照文件夹中的最后一项赋值给界面
     */
    private fun setGalleryThumbnail(fromFile: Uri?) {
        val thumbnail = container.findViewById<ImageButton>(R.id.photo_view_button)
        thumbnail.post {
            thumbnail.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())
            Glide.with(thumbnail)
                .load(fromFile)
                .apply(RequestOptions.circleCropTransform())
                .into(thumbnail)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_camera

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }


    companion object {

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }

}
