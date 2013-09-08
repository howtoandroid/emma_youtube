package com.example.emmasaying;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class Player_Activity extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener {
	public static final String DEVELOPER_KEY = "AIzaSyBlZu96jbKmUcdOFoWt7x6fBk1fgVBQcTY";
	public static String VIDEO_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_activity);
		Intent callingIntent = getIntent();
		VIDEO_ID = callingIntent.getStringExtra("VIDEO_ID");
		
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayer);
        youTubePlayerView.initialize(DEVELOPER_KEY, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_activity, menu);
		return true;
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		Toast.makeText(getApplicationContext(), "onInitializationFailure()", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInitializationSuccess(Provider p, YouTubePlayer ytPlayer,
			boolean wasRestored) 
	{
		 if (!wasRestored) {
		        ytPlayer.cueVideo(VIDEO_ID);
		      }		
	}
	
}
