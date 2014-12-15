/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.camera;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Manages beeps and vibrations for {@link CaptureActivity}.
 */
public final class BeepManager implements MediaPlayer.OnCompletionListener,
		MediaPlayer.OnErrorListener {

	private static final String TAG = BeepManager.class.getSimpleName();

	private static final float BEEP_VOLUME = 0.10f;

	private MediaPlayer mediaPlayer;
	private boolean playBeep = CameraSettings.isBEEP();

	public BeepManager(Context context) {
		this.mediaPlayer = null;
		mediaPlayer = buildMediaPlayer(context);
	}

	public synchronized void playBeepSound() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
	}

	private MediaPlayer buildMediaPlayer(Context context) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);

		AssetFileDescriptor file = null;
		try {
			file = context.getAssets().openFd("beep.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (file != null)
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException ioe) {
				Log.w(TAG, ioe);
				mediaPlayer = null;
			}
		return mediaPlayer;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// When the beep has finished playing, rewind to queue up another one.
		mp.seekTo(0);
	}

	@Override
	public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
		if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
		} else {
			mp.release();
			mediaPlayer = null;
		}
		return true;
	}

}
