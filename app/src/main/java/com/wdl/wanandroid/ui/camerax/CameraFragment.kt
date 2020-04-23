package com.wdl.wanandroid.ui.camerax

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentCameraBinding

/**
 * A simple [Fragment] subclass.
 */
class CameraFragment : BaseFragment<FragmentCameraBinding>() {

    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preView: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var camera: Camera? = null

    private val displayManager: DisplayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        container = view as ConstraintLayout
        viewFinder = mBinding!!.viewFinder

        viewFinder.post {
            displayId = viewFinder.display.displayId

            updateCameraUI()
        }
    }

    private fun updateCameraUI() {
        // Remove previous UI if any
        container.findViewById<ConstraintLayout>(R.id.camera_ui_container)?.let {
            container.removeView(it)
        }

        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)


    }

    override fun getLayoutId(): Int = R.layout.fragment_camera

}
