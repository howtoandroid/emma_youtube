package com.example.emmasaying;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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

public class Results_Activity extends Activity implements OnItemClickListener {

	public String Query;
	public static final String DEVELOPER_KEY = "AIzaSyBlZu96jbKmUcdOFoWt7x6fBk1fgVBQcTY";
	List<SearchResult> searchResultList;

	/** Global instance properties filename. */
	public static String PROPERTIES_FILENAME = "youtube.properties";

	/** Global instance of the HTTP transport. */
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of the max number of videos we want returned (50 = upper limit per page). */
	public static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	/** Global instance of Youtube object to make all API requests. */
	public static YouTube youtube;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_);
		
		// Get query from Main Screen search box
		Intent callingIntent = getIntent();
		Query = callingIntent.getStringExtra("query");
		
		// Populate Listview
		ListView resultsListView = (ListView) findViewById(R.id.list);
		searchResultList = populateResults(Query);
		
		resultsListView.setAdapter(new ResultsAdapter(this, searchResultList));
		resultsListView.setOnItemClickListener (this);

		
		if (searchResultList != null) {
			resultsLog(searchResultList.iterator(), Query);
		}
		
		/* while (searchResultList.iterator().hasNext()) 
		{
			SearchResult singleVideo = searchResultList.iterator().next();
			ResourceId rId = singleVideo.getId();
			Log.d("status", rId.getVideoId());
		}
		*/

		/*ListView resultsList = (ListView)findViewById(R.id.resultsList);
	    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getBaseContext(), R.layout.listitem, acNames.get(0));
	    resultsList.setAdapter(adapter1);
	    results = getIntent().getStringArrayListExtra("results);
	    List<String> firstColumn = new ArrayList<String>();
	    for (List<String> row : acNames) {
	        firstColumn.add(row.get(0));
	    }
	    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getBaseContext(), R.layout.listitem, firstColumn);
		 */
	}

	public List<SearchResult> populateResults(String query)
	{
		try {
			// Build YouTube API object
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {}
			}).setApplicationName("emma-saying").build();

			// Sets the "part" parameters.
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Sets the remaining parameters.
			search.setKey(DEVELOPER_KEY);
			search.setQ(query);
			search.setType("video");
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			search.setChannelId("UCNE227nPPd04KCtOK7_T7UQ");
			search.setOrder("relevance");
			
			// Sends search and holds results in searchResponse
			SearchListResponse searchResponse = search.execute();
			
			// Returns List<SearchResult>
			return searchResponse.getItems();

		} catch (GoogleJsonResponseException e) {
			Log.d("status", "There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (IOException e) {
			Log.d("status", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			Log.d("status", "Throwable Error");
			t.printStackTrace();
		}
		return null;
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
	
	public void onListItemClick(ListView parent, View v,
			int position, long id) {
		Log.d("status", "OnListItemClick fired.");
		launchResult(searchResultList.get(position).getId().getVideoId());
			}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results_, menu);
		return true;
	}

	public void launchResult(String videoID)
	{
		Log.d("status", "launchResults fired with ID " + videoID);
		Intent ytPlayerIntent= new Intent(this, Player_Activity.class);
		ytPlayerIntent.putExtra("VIDEO_ID", videoID);
		startActivity(ytPlayerIntent);
	}
	public void launchPlayer(View view) 
	{
		Log.d("status", "launchPlayer fired.");
		//ListView w = (ListView) view;
		Intent ytPlayerIntent= new Intent(this, Player_Activity.class);
		
		ytPlayerIntent.putExtra("VIDEO_ID", "mYeEy5Xhx4A");
		startActivity(ytPlayerIntent);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		  Log.d("status", "OnItemClick from ResultsActivity fired at position " + position);
		SearchResult singleVideo = searchResultList.get(position);
		  Log.d("status","Result obtained");
		ResourceId singleVideoId = singleVideo.getId();
		  Log.d("status","Resource ID obtained");
		String vidID = singleVideoId.getVideoId();
		  Log.d("status", "Video ID: " + vidID);
		launchResult(vidID);
	}
}
