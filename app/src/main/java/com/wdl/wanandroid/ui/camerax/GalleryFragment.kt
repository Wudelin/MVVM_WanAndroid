package com.wdl.wanandroid.ui.camerax

import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wdl.wanandroid.R
import com.wdl.wanandroid.base.BaseFragment
import com.wdl.wanandroid.databinding.FragmentGalleryBinding
import com.wdl.wanandroid.utils.FileProvider7
import com.wdl.wanandroid.utils.getGlobalViewModel
import com.wdl.wanandroid.utils.showImmersive
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private val args: GalleryFragmentArgs by navArgs()
    private val photo: File by lazy {
        File(args.PREVIEWPATH)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.apply {
            holder = this@GalleryFragment
            Glide.with(requireContext()).load(photo).into(ivPreview)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_gallery

    fun onAction(view: View) {
        when (view.id) {
            R.id.ib_back -> {
                Navigation.findNavController(view).navigateUp()
            }
            // 分享
            R.id.ib_share -> {
                val intent = Intent().apply {
                    val mediaType =
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(photo.extension)
                    val uri = FileProvider7.getUriForFile(requireContext(), photo)
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = mediaType
                    action = Intent.ACTION_SEND
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                startActivity(Intent.createChooser(intent, getString(R.string.share_hint)))
            }
            R.id.ib_delete -> {
                AlertDialog.Builder(view.context, android.R.style.Theme_Material_Dialog)
                    .setTitle(getString(R.string.delete_title))
                    .setMessage(getString(R.string.delete_dialog))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes) { _, _ ->

                        // Delete current photo
                        photo.delete()

                        // Send relevant broadcast to notify other apps of deletion
                        MediaScannerConnection.scanFile(
                            view.context, arrayOf(photo.absolutePath), null, null
                        )

                        Navigation.findNavController(
                            requireActivity(),
                            R.id.fragment_container_view
                        ).navigateUp()

                    }.setNegativeButton(android.R.string.no, null)
                    .create().showImmersive()
            }
            R.id.ib_confirm -> {
                val userInfo = getGlobalViewModel().userInfo.value
                userInfo?.icon = args.PREVIEWPATH
                getGlobalViewModel().userInfo.value = userInfo
                Navigation.findNavController(
                    requireActivity(),
                    R.id.fragment_container_view
                ).navigateUp()
            }
            else -> {
            }
        }
    }
}
