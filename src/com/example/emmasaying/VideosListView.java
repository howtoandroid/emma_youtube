package com.example.emmasaying;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.api.services.youtube.model.SearchResult;
 

public class VideosListView extends ListView {
	 
    public VideosListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public VideosListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public VideosListView(Context context) {
        super(context);
    }
 
    public void setResults(List<SearchResult> SearchResults){
        ResultsAdapter adapter = new ResultsAdapter(getContext(), SearchResults);
        setAdapter(adapter);
    }
     
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }
}
