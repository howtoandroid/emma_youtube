package com.example.emmasaying;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

public class ResultsAdapter extends BaseAdapter

{
	List<SearchResult> SearchResults;
	private LayoutInflater mInflater;
	Context Context;
	public static final String DEVELOPER_KEY = "AIzaSyBlZu96jbKmUcdOFoWt7x6fBk1fgVBQcTY";
	SearchResult singleVideo;
	ResourceId rId;
	int pos;


	public ResultsAdapter(Context context, List<SearchResult> SearchResults) {
		this.Context = context;
		this.SearchResults = SearchResults;
		this.mInflater = LayoutInflater.from(context);
	}

	// The getView() method is responsible for returning a View, representing the row
	// for the supplied position in the adapter data.
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		 if(convertView == null)
		{
			// Layout for each row.
			convertView = mInflater.inflate(R.layout.list_item_user_video, null);
		}

		SearchResult singleVideo = SearchResults.get(position);
		ResourceId rId = singleVideo.getId();

		LoaderImageView thumb = (LoaderImageView) convertView.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		
		title.setText(singleVideo.getSnippet().getTitle());
		Log.d("status", "Description: " + singleVideo.getSnippet().getDescription());
		Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

        // Load the thumbnail into the imageview
        thumb.setImageDrawable(thumbnail.getUrl());
        
        


		// Used to set listeners for objects within each row.
		/* title.setOnClickListener( new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		            Log.v("status", "Onclick generated for position " + position);
		            launchResult(SearchResults.get(position).getId().getVideoId());
		        }
		    });
		 */

		return convertView;
	}
	
	@Override
	public int getCount() {
		return SearchResults.size();
	}

	@Override
	public SearchResult getItem(int position) {
		return SearchResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// Local version used for individual onClick listeners within rows.
	/* public void launchResult(String videoID)
	{
		Log.d("status", "launchResults fired.");
		Intent ytPlayerIntent= new Intent(Context, Player_Activity.class);
		ytPlayerIntent.putExtra("VIDEO_ID", videoID);
		Context.startActivity(ytPlayerIntent);
	}
	*/
}
