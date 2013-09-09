package com.example.emmasaying;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;


public class ResultsAdapter extends BaseAdapter 
{
	List<SearchResult> SearchResults;
	private LayoutInflater mInflater;

	    public ResultsAdapter(Context context, List<SearchResult> SearchResults) {
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
	 
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
		    if(convertView == null)
		    {
		    	// Layout for each row.
		    	convertView = mInflater.inflate(R.layout.list_item_user_video, null);
		    }
		     
	    	SearchResult singleVideo = SearchResults.get(position);
	    	ResourceId rId = singleVideo.getId();
		    
	    	// Set the title
		    TextView title = (TextView) convertView.findViewById(R.id.title);
		    title.setText(singleVideo.getSnippet().getTitle());
		    
		    // Set description (For now, ID)
		    TextView secondLine = (TextView) convertView.findViewById(R.id.secondLine);
		    secondLine.setText(rId.getVideoId());

		    return convertView;
	}
}
