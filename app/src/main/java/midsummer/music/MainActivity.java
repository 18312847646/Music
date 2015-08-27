package midsummer.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends InitializeView
{
	// 进度条
	@ViewById
	SeekBar seekBar;
	// 时间
	@ViewById
	TextView music_progress, music_duration;
	// 开始暂停、上一首、下一首按钮
	@ViewById
	ImageButton play_pause_music, pre, next;
	// 音乐核心服务
	MusicService musicService;
	// 媒体
	MediaPlayer mediaPlayer;
	// 音乐名
	@ViewById
	TextView musicName;
	// 音乐数组
	List<MusicInfo> musics = new ArrayList<MusicInfo>();
	
	int index;
	private MyServiceConnection myServiceConnection;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			// 当前音乐刻度
			seekBar.setProgress(mediaPlayer.getCurrentPosition());
			music_progress.setText(millsSecondToString(mediaPlayer.getCurrentPosition()));
			handler.postDelayed(runnable, 100);
		}
	};
	
	@AfterViews
	public void mainActivity()
	{
		initializeview();
		// 从手机里获取音乐
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
		while (cursor != null && cursor.moveToNext())
		{
			MusicInfo info = new MusicInfo();
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			// 音乐物理路径
			info.setMusicPath(path);
			String musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			info.setMusicName(musicName);
			int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			info.setDuration(duration);
			if (!musicName.equals("") && !path.equals("") && duration != 0)
			{
				musics.add(info);
				Log.i("77.", "musicPath:" + info.getMusicPath());
			}
		}
		Log.i("95120", "开始");
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if (fromUser)
				{
					if (mediaPlayer != null)
					{
						mediaPlayer.seekTo(progress);
					}
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				
			}
		});
		// 开启服务
		Intent intent = new Intent(this, MusicService.class);
		this.startService(intent);
		
		// 绑定服务
		myServiceConnection = new MyServiceConnection();
		this.bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.i("95120", "onStop ");
	}
	
	@Click({R.id.pre, R.id.play_pause_music, R.id.next})
	public void click(View v)
	{
		switch (v.getId())
		{
			case R.id.pre:
				// 上一首
				playMusicByStatu(1);
				break;
			case R.id.next:
				// 下一首
				playMusicByStatu(2);
				break;
			case R.id.play_pause_music:
				if (mediaPlayer != null)
				{
					if (mediaPlayer.isPlaying())
					{
						// 暂停
						musicService.pause();
						play_pause_music.setImageResource(R.drawable.ic_action_playback_play);
					} else
					{
						// 改变图标
						musicService.continueMusic();
						play_pause_music.setImageResource(R.drawable.ic_action_playback_pause);
					}
				}
				break;
			default:
				break;
		}
	}
	
	
	public void playMusicByStatu(int statu)
	{
		switch (statu)
		{
			case 0:
				// 直接播放当前音乐
				index = index;
				break;
			case 1:
				// 上一首
				index--;
				if (index == -1)
				{
					index = musics.size() - 1;
				}
				break;
			case 2:
				// 下一首
				index++;
				if (index == musics.size())
				{
					index = 0;
				}
				break;
		}
		Log.i("95120", "playMusicByStatu ");
		playMusic(index);
	}
	
	/**
	 * 根据音乐数组下标播放音乐
	 *
	 * @param index2
	 */
	private void playMusic(int index2)
	{
		if (musics.size() > 0)
		{
			// 播放音乐
			musicService.play(musics.get(index2).getMusicPath());
			// 设置音乐名
			musicName.setText(musics.get(index2).getMusicName());
			music_duration.setText(millsSecondToString(musics.get(index2).getDuration()));
		}
		// 设置进度条最大值
		seekBar.setMax(mediaPlayer.getDuration());
	}
	
	/**
	 * 把毫秒转出标准秒钟格式
	 *
	 * @param mills
	 * @return
	 */
	public String millsSecondToString(int mills)
	{
		int seconds = mills / 1000;
		int second = seconds % 60;
		int munite = (seconds - second) / 60;
		DecimalFormat decimalFormat = new DecimalFormat("00");
		return decimalFormat.format(munite) + ":" + decimalFormat.format(second);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (myServiceConnection != null)
		{
			this.unbindService(myServiceConnection);
		}
	}
	
	private class MyServiceConnection implements ServiceConnection
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			// 绑定成功
			MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
			musicService = myBinder.getMusicService();
			Log.i("77.", "onServiceConnected");
			// 开始播放第一首歌
			mediaPlayer = musicService.mediaPlayer;
			if (!mediaPlayer.isPlaying())
			{
				// 默认播放音乐
				playMusicByStatu(0);
				play_pause_music.setImageResource(R.drawable.ic_action_playback_pause);
				mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
				{
					@Override
					public void onPrepared(MediaPlayer mp)
					{
						// 当音乐播放完了播放下一首歌
						playMusicByStatu(1);
					}
				});
			} else
			{
				play_pause_music.setImageResource(R.drawable.ic_action_playback_pause);
			}
			seekBar.setMax(mediaPlayer.getDuration());
			// 停止循环
			handler.removeCallbacks(runnable);
			handler.post(runnable);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			
		}
	}
}





































