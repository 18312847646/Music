package midsummer.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * 项目名称：Mu
 * 类描述：
 * 创建人：77.
 * 创建时间：2015/8/26 0026 20:44
 * 修改人：77.
 * 修改时间：2015/8/26 0026 20:44
 * 修改备注：
 */
public class MusicService extends Service
{
	public MediaPlayer mediaPlayer = new MediaPlayer();
	private MyBinder myBinder = new MyBinder();
	
	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		return myBinder;
	}
	
	// 音乐播放相关函数
	public void play(String path)
	{
		mediaPlayer.reset();
		try
		{
			// 设置音乐路径
			mediaPlayer.setDataSource(path);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			// 缓冲
			mediaPlayer.prepare();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				// 开始播放
				mp.start();
			}
		});
	}
	
	public void pause()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.pause();
		}
	}
	
	public void stop()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.stop();
			// 释放
			mediaPlayer.reset();
		}
	}
	
	public void continueMusic()
	{
		mediaPlayer.start();
	}
	
	public class MyBinder extends Binder
	{
		public MusicService getMusicService()
		{
			return MusicService.this;
		}
	}
}



































