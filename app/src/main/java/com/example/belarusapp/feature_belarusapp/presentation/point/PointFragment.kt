package com.example.belarusapp.feature_belarusapp.presentation.point

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SeekBar

import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.belarusapp.R
import com.example.belarusapp.databinding.FragmentPointBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar.SECOND

private const val ROUNDED_CORNERS = 16

@AndroidEntryPoint
class PointFragment : Fragment(R.layout.fragment_point), MediaPlayer.OnPreparedListener,
    SeekBar.OnSeekBarChangeListener {
    private val args: PointFragmentArgs by navArgs()
    private val binding: FragmentPointBinding by viewBinding()

    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())

    private val mediaPlayer = MediaPlayer()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("123", "GOT ${args.point.id_point}")
        preparePlayer()
        mediaPlayerSet()
        with(binding)
        {
            tvName.text = args.point.name
            Glide.with(ivPoint).load(args.point.photo).apply(
                RequestOptions()
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .transform(RoundedCorners(ROUNDED_CORNERS))
            ).into(ivPoint)
            tvDesc.text = args.point.text.removeP()
            tvDate.text = "Дата: " + args.point.creation_date
        }
    }

    fun preparePlayer() {
        mediaPlayer.setOnPreparedListener(this)
        binding.seekBar.setOnSeekBarChangeListener(this)
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(args.point.sound)
            prepareAsync()
        }
    }

    fun mediaPlayerSet() {
        binding.btnSound.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.btnSound.text = "PLAY"
            } else {
                try {
                    mediaPlayer.start()
                    binding.btnSound.text = "PAUSE"

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.release()

    }

    private fun timeInString(seconds: Int): String {
        return String.format(
            "%02d:%02d",
            (seconds / 3600 * 60 + ((seconds % 3600) / 60)),
            (seconds % 60)
        )
    }

    private fun initializeSeekBar() {
        with(binding) {
            seekBar.max = mediaPlayer.seconds
            textProgress.text = "00:00"
            textTotalTime.text = timeInString(mediaPlayer.seconds)
        }
    }

    private fun updateSeekBar() {
        runnable = Runnable {
            binding.textProgress.text = timeInString(mediaPlayer.currentSeconds)
            binding.seekBar.progress = mediaPlayer.currentSeconds
            handler.postDelayed(runnable, SECOND.toLong())
        }
        handler.postDelayed(runnable, SECOND.toLong())
    }

    override fun onPrepared(p0: MediaPlayer?) {
        initializeSeekBar()
        updateSeekBar()
    }

    override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser)
            mediaPlayer.seekTo(progress * SECOND)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}

private val MediaPlayer.seconds: Int
    get() {
        return this.duration / 1000
    }

private val MediaPlayer.currentSeconds: Int
    get() {
        return this.currentPosition / 1000
    }

private fun String.removeP(): String {
    return replace("</p> <p>".toRegex(), "")
}