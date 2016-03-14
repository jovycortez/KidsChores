package com.example.nomorenagginginator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.nomorenagginginator.DatePickerDialog.SaveDateListener;

public class ChoreActivity extends FragmentActivity implements SaveDateListener {

	private Chores currentChore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chore);

        initListButton();
		initChoreButton();
        initSettingsButton();
        initToggleButton();
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();

		Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	initChore(extras.getInt("choreId"));
        }
        else {
			currentChore = new Chores();
        }
        setForEditing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chore, menu);
        return true;
    }


	private void initListButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
        list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoreActivity.this, ContactListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}


	private void initChoreButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
        list.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ChoreActivity.this, ChoreListActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}
	private void initSettingsButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
        list.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ChoreActivity.this, ContactSettingsActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}

	private void initToggleButton() {
		final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
		editToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setForEditing(editToggle.isChecked());
			}

		});
	}

	private void initSaveButton() {
		Button saveButton = (Button) findViewById(R.id.btnSaveChore);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideKeyboard();
				ChoreDataSource ds = new ChoreDataSource(ChoreActivity.this);
				ds.open();
				
				boolean wasSuccessful = false;
				if (currentChore.getChoreID()==-1) {
					wasSuccessful = ds.insertChore(currentChore);
					int newId = ds.getLastChoreId();
					currentChore.setChoreID(newId);
				}
				else {
					wasSuccessful = ds.updateChore(currentChore);
				}
				ds.close();
				
				if (wasSuccessful) {
					ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
		    		editToggle.toggle();
					setForEditing(false);
				}
			}
		});
	}

	private void initTextChangedEvents(){
		final EditText choreName = (EditText) findViewById(R.id.txtChore);
		choreName.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				currentChore.setChore(choreName.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				//  Auto-generated method stub

			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//  Auto-generated method stub		
			}
		});


		final EditText frequency = (EditText) findViewById(R.id.txtFreq);
		frequency.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				currentChore.setFrequency(frequency.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}
		});


		frequency.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
	}
	
	private void setForEditing(boolean enabled) {
		EditText NameChore = (EditText) findViewById(R.id.txtChore);
		EditText editFrequency = (EditText) findViewById(R.id.txtFreq);
		Button buttonChange = (Button) findViewById(R.id.btnChangeDuration);

		Button buttonSave = (Button) findViewById(R.id.btnSaveChore);
		NameChore.setEnabled(enabled);
		editFrequency.setEnabled(enabled);

		buttonChange.setEnabled(enabled);
		buttonSave.setEnabled(enabled);
		
		if (enabled) {
			NameChore.requestFocus();
		}
		else {
			ScrollView s = (ScrollView) findViewById(R.id.scrollView1);
			s.fullScroll(ScrollView.FOCUS_UP);
			s.clearFocus();
		}
		
	}

	private void initChangeDateButton() {
		Button changeDate = (Button) findViewById(R.id.btnChangeDuration);
		changeDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		    	FragmentManager fm = getSupportFragmentManager();
		        DatePickerDialog datePickerDialog = new DatePickerDialog();
		        datePickerDialog.show(fm, "DatePick");
			}			
		});
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		EditText editName = (EditText) findViewById(R.id.txtChore);
		imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
		EditText editCell = (EditText) findViewById(R.id.txtFreq);
		imm.hideSoftInputFromWindow(editCell.getWindowToken(), 0);

	}


	private void initChore(int id) {

		ChoreDataSource ds = new ChoreDataSource(ChoreActivity.this);

		ds.open();
		currentChore = ds.getSpecificChore(id);
		ds.close();
		
		EditText Chore = (EditText) findViewById(R.id.txtChore);
		EditText Frequency = (EditText) findViewById(R.id.txtFreq);
		TextView Duration = (TextView) findViewById(R.id.txtDuration);

		Chore.setText(currentChore.getChore());
		Frequency.setText(currentChore.getFrequency());
		Duration.setText(DateFormat.format("MM/dd/yyyy", currentChore.getDuration().toMillis(false)).toString());
	}


	@Override
	public void didFinishDatePickerDialog(Time selectedTime) {
		TextView Duration = (TextView) findViewById(R.id.txtDuration);
		Duration.setText(DateFormat.format("MM/dd/yyyy", selectedTime.toMillis(false)).toString());
	}
    
}
