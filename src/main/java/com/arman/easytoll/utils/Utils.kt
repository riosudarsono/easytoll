package com.arman.easytoll.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.arman.easytoll.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.stfalcon.frescoimageviewer.ImageViewer
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

object Utils {

    fun setGlide(context: Context, urlImage: String?, imageView: ImageView, placeholder: Int) {
        if (!urlImage.isNullOrEmpty()) {
            Glide.with(context)
                .asBitmap()
                .load(urlImage)
                .disallowHardwareConfig()
                .centerCrop().fitCenter()
                .dontTransform().dontAnimate()
                .thumbnail(0.3f)
                .placeholder(placeholder)
                .timeout(40 * 60 * 1000)
                .into(imageView)
        }
    }

    fun setGlideCircle(context: Context, urlImage: String?, imageView: ImageView) {
        if (!urlImage.isNullOrEmpty()) {
            Glide.with(context)
                .asBitmap()
                .load(urlImage)
                .disallowHardwareConfig()
                .centerCrop().fitCenter()
                .dontTransform().dontAnimate()
                .thumbnail(0.3f)
                .placeholder(R.drawable.ic_image_black)
                .apply(RequestOptions.circleCropTransform())
                .timeout(40 * 60 * 1000)
                .into(imageView)
        }
    }

    fun setGlideRoundedCorners(context: Context, urlImage: String?, imageView: ImageView) {
        if (!urlImage.isNullOrEmpty()) {
            Glide.with(context)
                .asBitmap()
                .load(urlImage)
                .disallowHardwareConfig()
                .thumbnail(0.3f)
                .centerCrop().fitCenter()
                .dontAnimate().dontTransform()
                .placeholder(R.drawable.ic_image_black)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .timeout(40 * 60 * 1000)
                .into(imageView)
        }
    }

    fun imageOverLay(context: Context, imageFirst: String?, position: Int?, listImage: List<String?>) {
        if (imageFirst.isNullOrEmpty()) {
            Toast.makeText(context, "Gambar Tidak Ada", Toast.LENGTH_SHORT).show()
        } else {
            val imageOverlayView: ImageOverlayView? = null
            val imageRequestBuilder: ImageRequestBuilder? = null
            ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageFirst)).isProgressiveRenderingEnabled = true
            val hierarchyBuilder: GenericDraweeHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.resources)
                    .setFailureImage(R.drawable.ic_image_black)
                    .setProgressBarImage(R.drawable.ic_image_black)
                    .setPlaceholderImage(R.drawable.ic_image_black)
            ImageViewer.Builder<Any?>(context, listImage)
                .setStartPosition(position!!)
                .hideStatusBar(true)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                .setOverlayView(imageOverlayView)
                .setCustomImageRequestBuilder(imageRequestBuilder)
                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                .show()
        }
    }

    fun imageOverLay1(context: Context, urlImage: String?) {
        if (urlImage.isNullOrEmpty()) {
            Toast.makeText(context, "Gambar Tidak Ada", Toast.LENGTH_SHORT).show()
        } else {
            val imageOverlayView: ImageOverlayView? = null
            val imageRequestBuilder: ImageRequestBuilder? = null
            ImageRequestBuilder.newBuilderWithSource(Uri.parse(urlImage)).isProgressiveRenderingEnabled = true
            val hierarchyBuilder: GenericDraweeHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.resources)
                    .setFailureImage(R.drawable.ic_image_black)
                    .setProgressBarImage(R.drawable.ic_image_black)
                    .setPlaceholderImage(R.drawable.ic_image_black)
            val image = arrayOf<String?>(urlImage)
            ImageViewer.Builder<Any?>(context, image)
                .setStartPosition(0)
                .hideStatusBar(true)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                .setOverlayView(imageOverlayView)
                .setCustomImageRequestBuilder(imageRequestBuilder)
                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                .show()
        }
    }

    fun hideSoftKeyboard(act: Activity) {
        val imm = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = act.currentFocus
        if (view == null) {
            view = View(act)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSoftKeyboard(view: View, act: Activity) {
        view.requestFocus()
        view.postDelayed({
            val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }, 400)
    }

    fun formatOtpNumber(_number: String): String{
        var regex  = if (_number.length <= 9) "(\\d{3})(\\d{3})(\\d+)"
        else "(\\d{3})(\\d{4})(\\d+)"
        return "+62"+ _number.replaceFirst(regex.toRegex(), "$1-$2-$3")
    }

    fun getSizeFile(activity: Activity, uri: Uri?): Long {
        var fileSizeInBytes = 0L
        try {
            val cr = activity.contentResolver
            val `is` = cr.openInputStream(uri!!)
            fileSizeInBytes = `is`!!.available().toLong()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val fileSizeInKB = fileSizeInBytes / 1024
        //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024
    }

    fun convertBase64(context: Context, mF: String?): String? {
        return try {
            val options = BitmapFactory.Options()
            val ist =
                context.contentResolver.openInputStream(Uri.parse(mF))
            var bm = BitmapFactory.decodeStream(ist, null, options)
            val byteArrayOutputStream =
                ByteArrayOutputStream()
            bm!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (ex: Exception) {
            Log.e("err", ex.message)
            null
        }
    }

}