///**
// * 
// */
//package com.hupu.games.activity;
//
//import io.vov.vitamio.LibsChecker;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
//import io.vov.vitamio.MediaPlayer.OnInfoListener;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//import android.app.Activity;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.hupu.games.R;
//
///**
// * @author papa 视频播放act
// */
//public class HupuVideoActivity extends Activity implements
//		OnInfoListener, OnBufferingUpdateListener {
//	private String path = "http://m.youku.com/wap/pvs?id=XNzAyNTcyNDQw&format=3gphd";
//	private Uri uri;
//	private VideoView mVideoView;
//	private ProgressBar pb;
//	private TextView downloadRateView, loadRateView;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (!LibsChecker.checkVitamioLibs(this))
//			return;
//		// 设置全屏
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.layout_hupuvideo);
//		mVideoView = (VideoView) findViewById(R.id.buffer);
//	    pb = (ProgressBar) findViewById(R.id.probar);
//	    
//	    path = getIntent().getStringExtra("source");
//	    downloadRateView = (TextView) findViewById(R.id.download_rate);
//	    loadRateView = (TextView) findViewById(R.id.load_rate);
//	    
//	    mVideoView.setVideoURI(Uri.parse(path));
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
//		mVideoView.setOnInfoListener(this);
//		mVideoView.setOnBufferingUpdateListener(this);
//		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//			@Override
//			public void onPrepared(MediaPlayer mediaPlayer) {
//				// optional need Vitamio 4.0
//				mediaPlayer.setPlaybackSpeed(1.0f);
//			}
//		});
//	}
//
////	@Override
////	public void treatClickEvent(int id) {
////		super.treatClickEvent(id);
////
////		switch (id) {
////		case R.id.btn_back:
////			finish();
////			break;
////		}
////	}
//
//	@Override
//	public void onBufferingUpdate(MediaPlayer mp, int percent) {
//		// TODO Auto-generated method stub
//		loadRateView.setText(percent + "%");
//
//	}
//
//	@Override
//	public boolean onInfo(MediaPlayer mp, int what, int extra) {
//		// TODO Auto-generated method stub
//		switch (what) {
//		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//			if (mVideoView.isPlaying()) {
//				mVideoView.pause();
//				pb.setVisibility(View.VISIBLE);
//				downloadRateView.setText("");
//				loadRateView.setText("");
//				downloadRateView.setVisibility(View.VISIBLE);
//				loadRateView.setVisibility(View.VISIBLE);
//
//			}
//			break;
//		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//			mVideoView.start();
//			pb.setVisibility(View.GONE);
//			downloadRateView.setVisibility(View.GONE);
//			loadRateView.setVisibility(View.GONE);
//			break;
//		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//			downloadRateView.setText("" + extra + "kb/s" + "  ");
//			break;
//		}
//		return true;
//	}
//
//}
