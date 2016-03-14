package com.example.nomorenagginginator;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ChoreListActivity extends ListActivity {

	boolean isDeleting = false;
	ChoreAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chore_list);

		initListButton();
		//initChoreActivity();
		initSettingsButton();
		initDeleteButton();
		initAddChoreButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chore, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "chore");
		String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

		ChoreDataSource ds = new ChoreDataSource(this);
		ds.open();
		final ArrayList<Chores> chores = ds.getChores(sortBy, sortOrder);
		ds.close();

		if (chores.size() > 0) {

			adapter = new ChoreAdapter(this, chores);
			setListAdapter(adapter);
			ListView listView = getListView();
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View itemClicked,
						int position, long id) {
					Chores selectedChores = chores.get(position);
					if (isDeleting) {
						adapter.showDelete(position, itemClicked, ChoreListActivity.this, selectedChores);
					}
					else {
						Intent intent = new Intent(ChoreListActivity.this, ChoreActivity.class);
						intent.putExtra("choreId", selectedChores.getChoreID());
						startActivity(intent);
					}
				}
			});
		}
		else {
			Intent intent = new Intent(ChoreListActivity.this, ChoreActivity.class);
			startActivity(intent);
		}
	}

	private void initAddChoreButton() {
		Button newChore = (Button) findViewById(R.id.btnAddChore);
		newChore.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoreListActivity.this, ChoreActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initDeleteButton() {
		final Button deleteButton = (Button) findViewById(R.id.btnDelChore);
		deleteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				if (isDeleting) {
					deleteButton.setText("Delete");
					isDeleting = false;

				    adapter.notifyDataSetChanged();
				}
				else {
					deleteButton.setText("Done Deleting");
					isDeleting = true;
				}
            }
        });
	}


	private void initListButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
        list.setEnabled(false);
	}

	private void initChildActivity() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
        list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoreListActivity.this, ChildActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	private void initSettingsButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
        list.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ChoreListActivity.this, ContactSettingsActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}


}
