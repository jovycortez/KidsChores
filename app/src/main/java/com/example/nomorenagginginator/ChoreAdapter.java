package com.example.nomorenagginginator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ChoreAdapter extends ArrayAdapter<Chores> {

    private ArrayList<Chores> items;
    private Context adapterContext;

    public ChoreAdapter(Context context, ArrayList<Chores> items) {
            super(context, R.layout.chore_list_item, items);
            adapterContext = context;
            this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	try {
            Chores chore = items.get(position);
            
            if (v == null) {
            		LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            		v = vi.inflate(R.layout.chore_list, null);
            }

            TextView txtchore = (TextView) v.findViewById(R.id.txtChore);
            TextView frequency = (TextView) v.findViewById(R.id.txtFrequency);
        	Button b = (Button) v.findViewById(R.id.buttonDeleteChore);
            	
        	txtchore.setText(chore.getChore());

            b.setVisibility(View.INVISIBLE);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		e.getCause();
    	}
            return v;
    }
    
    public void showDelete(final int position, final View convertView, final Context context, final Chores chore) {
    	View v = convertView;
    	final Button b = (Button) v.findViewById(R.id.buttonDeleteChore);

    	if (b.getVisibility()==View.INVISIBLE) {
    		b.setVisibility(View.VISIBLE);
    		b.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				hideDelete(position, convertView, context);
    				items.remove(chore);
    				deleteOption(chore.getChoreID(), context);
    			}

    		});
    	}
    	else {
			hideDelete(position, convertView, context);
    	}
    }

	private void deleteOption(int choreToDelete, Context context) {
		ChoreDataSource db = new ChoreDataSource(context);
		db.open();
		db.deleteChore(choreToDelete);
		db.close();	
		this.notifyDataSetChanged();
	}
 
	private void hideDelete(int position, View convertView, Context context) {
      View v = convertView;
      final Button b = (Button) v.findViewById(R.id.buttonDeleteChore);
	  b.setVisibility(View.INVISIBLE);
	  b.setOnClickListener(null);
 }

}
