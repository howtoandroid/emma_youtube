package com.example.emmasaying;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

public class Main_Screen extends YouTubeBaseActivity implements OnSharedPreferenceChangeListener,
YouTubePlayer.OnInitializedListener {

	public static final String DEVELOPER_KEY = "AIzaSyBlZu96jbKmUcdOFoWt7x6fBk1fgVBQcTY";
	public static String LATEST_UPLOAD_ID;
	
	/** Global instance properties filename. */
	public static String PROPERTIES_FILENAME = "youtube.properties";

	/** Global instance of the HTTP transport. */
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of the max number of videos we want returned (50 = upper limit per page). */
	public static final long MAX_RESULTS = 1;

	/** Global instance of Youtube object to make all API requests. */
	public static YouTube youtube;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__screen);
		
		LATEST_UPLOAD_ID = getLatestUpload();
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.mainscreen_youtubeplayer);
        youTubePlayerView.initialize(DEVELOPER_KEY, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main__screen, menu);
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	  
	public void process_Search(View view)
	{
		EditText SearchBox = (EditText)findViewById(R.id.searchBox);
		String Query = SearchBox.getText().toString();
		  Log.d("status", "Contents: " + Query); // adb logcat -s "TAGNAME"
		Intent resultsIntent = new Intent(this, Results_Activity.class);
		resultsIntent.putExtra("query", Query);
		startActivity(resultsIntent);
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
		if (!wasRestored)
		{
			// Main screen always plays the latest upload, ID is fetched in onCreate().
			ytPlayer.cueVideo(LATEST_UPLOAD_ID);
		}		
	}

	/* 
	 * Returns the ID of the user's latest upload.
	 * #TODO - Should be refactored into superclass
	 */
	public String getLatestUpload()
	{
		try 
		{
			// YouTube object used to make all API requests.
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {}
			}).setApplicationName("emma-saying").build();

			
			 // Builds a channel request. Returned with that data is the play list 
			 // id for the uploaded videos. 
			YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");

			// Return channels associated with this user
			channelRequest.setId("UCNE227nPPd04KCtOK7_T7UQ");
			channelRequest.setKey(DEVELOPER_KEY);
			// Only one result is needed- the upload channel
			channelRequest.setMaxResults(MAX_RESULTS);

			// Limits the results to only the data we need which makes things more efficient.
			channelRequest.setFields("items/contentDetails");

			// Send the channel request and store it in a response.
			ChannelListResponse channelResult = channelRequest.execute();

			/*
			 * Gets the list of channels associated with the user. 
			 * This sample pulls videos from the upload channel.
			 * Populates the channel list with videos and metadata
			 */
			List<Channel> channelsList = channelResult.getItems();

			// Gets user's default channel id (first channel in list, i.e. upload channel).
			// #TODO - Find out if this can be changed if the user creates new channels
			String uploadPlaylistId =
					channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();

			/*
			 * Now that we have the playlist id for uploads, we will request a playlistItems
			 * request, associated with that playlist id, so we can get information on each video uploaded.
			 */
			YouTube.PlaylistItems.List playlistItemRequest =
					youtube.playlistItems().list("id,contentDetails,snippet");
			
			// Set request parameters.
			playlistItemRequest.setPlaylistId(uploadPlaylistId);
			playlistItemRequest.setMaxResults(MAX_RESULTS);
			playlistItemRequest.setKey(DEVELOPER_KEY);
			
			// Limits the results to only the data needed.
			playlistItemRequest.setFields(
					"items(contentDetails/videoId,snippet/title,snippet/publishedAt)");
			
			// Send the playlist request and store it in a response
			PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

			// A list that will hold videos + metadata retrieved from latest uploads.
			List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
			
			// Add the results of the request to our local list
			playlistItemList.addAll(playlistItemResult.getItems());

			// Get the first video + metadata from results list
			PlaylistItem playlistItem = playlistItemList.iterator().next();
			
			// Return the video ID associated with that above video
			return playlistItem.getContentDetails().getVideoId();

		} catch (GoogleJsonResponseException e) {
			e.printStackTrace();
			Log.d("status", "There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());

		} catch (Throwable t) {
			t.printStackTrace();
			Log.d("status", "throwable Error");
		}
		// Should never be reached.
		Log.d("status", "Fatal error");
		return null;
	}
	  
}
