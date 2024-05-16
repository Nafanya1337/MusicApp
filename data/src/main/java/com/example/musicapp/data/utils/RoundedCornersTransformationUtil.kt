package com.example.musicapp.data.utils

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

class RoundedCornersTransformation(private val radius: Int) : BitmapTransformation() {
    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return TransformationUtils.roundedCorners(
            pool,
            toTransform,
            radius)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(("rounded corners $radius").toByteArray(Charsets.UTF_8))
    }
}