package com.qhn.mybase.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import com.g.base.help.tryCatch


fun MediaPlayer.playMedia(context: Context, audioPath: String,
                          isLoop: Boolean = false,
                          onCompletionListener: ((p0: MediaPlayer?) -> Unit)? = null,
                          onErrorListener: ((p0: MediaPlayer?, p1: Int, p2: Int) -> Boolean)? = null) {
    tryCatch {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd(audioPath)
        setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
        isLooping = isLoop
        setOnCompletionListener { p0 -> onCompletionListener?.invoke(p0) }
        if (onErrorListener != null) {
            setOnErrorListener { p0, p1, p2 -> onErrorListener.invoke(p0, p1, p2) }
        }
        prepare()
        start()
    }
}

fun MediaPlayer.stopMedia() {
    if (isPlaying){
        stop()
        release()
    }
}