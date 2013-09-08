package com.example.emmasaying;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main_Screen extends Activity implements OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__screen);
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
	
	/** Global instance properties filename. */
	  private static String PROPERTIES_FILENAME = "youtube.properties";

	  /** Global instance of the HTTP transport. */
	  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	  /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
	  private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	  /** Global instance of Youtube object to make all API requests. */
	  private static YouTube youtube;
	  
	  public static final String DEVELOPER_KEY = "AIzaSyBlZu96jbKmUcdOFoWt7x6fBk1fgVBQcTY";
	  
	  public void process_Search(View view)
	  {
		  try {
			  youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				  public void initialize(HttpRequest request) throws IOException {}
		      }).setApplicationName("emaa-saying").build();
			  
			  EditText SearchBox = (EditText)findViewById(R.id.searchBox);
			  String Query = SearchBox.getText().toString();
			  	Log.d("status", "Contents: " + Query); //// adb logcat -s "TAGNAME"
		
			  // Sets the "part" parameters.
		      YouTube.Search.List search = youtube.search().list("id,snippet");
		      // Sets the remaining parameters.
			  search.setKey(DEVELOPER_KEY);
			  search.setQ(Query);
			  search.setType("video");
		      search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
		      search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
		      search.setChannelId("UCNE227nPPd04KCtOK7_T7UQ");
		      search.setOrder("relevance");
		      
		      SearchListResponse searchResponse = search.execute();
	
		      List<SearchResult> searchResultList = searchResponse.getItems();

		      if (searchResultList != null) {
		        resultsLog(searchResultList.iterator(), Query);
		      }
		      
			  startActivity(new Intent(this, Results_Activity.class));
		  } catch (GoogleJsonResponseException e) {
			  System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					  + e.getDetails().getMessage());
		  } catch (IOException e) {
			  System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		  } catch (Throwable t) {
			  t.printStackTrace();
		  }
	  }
	  
	  private static void resultsLog(Iterator<SearchResult> iteratorSearchResults, String query) 
	  {
		Log.d("status", "\n=============================================================");
		Log.d("status",
		    "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
		Log.d("status", "=============================================================\n");
		
		if (!iteratorSearchResults.hasNext()) 
		{
		  Log.d("status", "There aren't any results for your query.");
		}
		
		while (iteratorSearchResults.hasNext()) 
		{
		
		  SearchResult singleVideo = iteratorSearchResults.next();
		  ResourceId rId = singleVideo.getId();
		
		  // Double checks the kind is video.
		  if (rId.getKind().equals("youtube#video")) 
		  {
		    Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");
		
		    Log.d("status"," Video Id" + rId.getVideoId());
		    Log.d("status"," Title: " + singleVideo.getSnippet().getTitle());
		    Log.d("status"," Thumbnail: " + thumbnail.getUrl());
		    Log.d("status","\n-------------------------------------------------------------\n");
		  }
		}
	  }
}
