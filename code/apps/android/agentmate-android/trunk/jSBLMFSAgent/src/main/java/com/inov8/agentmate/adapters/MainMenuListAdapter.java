package com.inov8.agentmate.adapters;

import java.util.ArrayList;

import com.inov8.jsblmfs.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuListAdapter extends ArrayAdapter<String>  
{
	private final Context mContext;
	
	private int selectedPos = 0;
	
	private ViewHolder holder = null;
	
	private ArrayList<String> menu_text_list;
	private ArrayList<Integer> menu_images_list;

	public MainMenuListAdapter(Context context, int textViewResourceId, 
			ArrayList<String> menu_items, ArrayList<Integer> menu_item_images)
	{
		super(context, textViewResourceId, menu_items);
		
		mContext = context;
		
		this.menu_text_list = menu_items;	
		this.menu_images_list = menu_item_images;	
		
	}
		

	public void setSelectedPosition(int pos) 
    {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() 
    {
        return selectedPos;
    }  
    
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
	    holder = null;
	    try
	    {
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_row, parent, false);
	
				holder = new ViewHolder();
				
				holder.menu_textview = (TextView) convertView.findViewById(R.id.menu_text);
				holder.menu_imageview = (ImageView) convertView.findViewById(R.id.menu_image);
		       
				convertView.setTag(holder);
			} 
			else 
			{
			    holder = (ViewHolder) convertView.getTag();		    
			}		
			
			holder.menu_textview.setText(menu_text_list.get(position));
			holder.menu_imageview.setImageResource(menu_images_list.get(position));				
	    }
	    catch(Exception ex)
	    {
	    	ex.getMessage();
	    }

		return convertView;
	}

	class ViewHolder 
	{
		public TextView menu_textview;
	    
	    public ImageView menu_imageview;
	}
	
	
}
