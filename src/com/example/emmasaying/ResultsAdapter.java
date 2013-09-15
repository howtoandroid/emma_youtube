package com.example.emmasaying;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;


public class ResultsAdapter extends BaseAdapter 
{
	List<SearchResult> SearchResults;
	private LayoutInflater mInflater;
	Context Context;

	public ResultsAdapter(Context context, List<SearchResult> SearchResults) {
		this.Context = context;
		this.SearchResults = SearchResults;
		this.mInflater = LayoutInflater.from(context);
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

		// Set the title
		TextView secondLine = (TextView) convertView.findViewById(R.id.title);

		// Set description (For now, ID)
		TextView title = (TextView) convertView.findViewById(R.id.secondLine);
		
		title.setText(singleVideo.getSnippet().getTitle());
		
		secondLine.setText(rId.getVideoId());
		
		title.setOnClickListener( new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            Log.v("status", "Onclick generated for position " + position);
	            launchResult(SearchResults.get(position).getId().getVideoId());
	        }
	    });

		return convertView;
	}
	
	public void launchResult(String videoID)
	{
		Log.d("status", "launchResults fired.");
		Intent ytPlayerIntent= new Intent(Context, Player_Activity.class);
		ytPlayerIntent.putExtra("VIDEO_ID", videoID);
		Context.startActivity(ytPlayerIntent);
	}
}
