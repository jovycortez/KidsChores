package com.example.nomorenagginginator;

import com.example.nomorenagginginator.DatePickerDialog.SaveDateListener;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
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

public class ChildActivity extends FragmentActivity implements SaveDateListener {
	
	private Contact currentContact;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child);
        
        initListButton();
		initChoreActivity();
        initSettingsButton();
        initToggleButton();
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();
        
		Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	initContact(extras.getInt("contactid"));
        }
        else {
        	currentContact = new Contact();
        }
        setForEditing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }
    
	private void initListButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ChildActivity.this, ContactListActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}
	
	private void initChoreActivity() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ChildActivity.this, ChoreListActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
	}
	private void initSettingsButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    			Intent intent = new Intent(ChildActivity.this, ContactSettingsActivity.class);
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
		Button saveButton = (Button) findViewById(R.id.buttonSave);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideKeyboard();
				ContactDataSource ds = new ContactDataSource(ChildActivity.this);
				ds.open();
				
				boolean wasSuccessful = false;
				if (currentContact.getContactID()==-1) {
					wasSuccessful = ds.insertContact(currentContact);
					int newId = ds.getLastContactId();
					currentContact.setContactID(newId);
				}
				else {
					wasSuccessful = ds.updateContact(currentContact);
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
		final EditText contactName = (EditText) findViewById(R.id.txtName);
		contactName.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				currentContact.setContactName(contactName.getText().toString());
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









		final EditText cellNumber = (EditText) findViewById(R.id.editCell);
		cellNumber.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				currentContact.setCellNumber(cellNumber.getText().toString());
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


		cellNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
	}
	
	private void setForEditing(boolean enabled) {
		EditText editName = (EditText) findViewById(R.id.txtName);
		EditText editCell = (EditText) findViewById(R.id.editCell);
		Button buttonChange = (Button) findViewById(R.id.btnChangeDuration);

		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		editName.setEnabled(enabled);
		editCell.setEnabled(enabled);

		buttonChange.setEnabled(enabled);
		buttonSave.setEnabled(enabled);
		
		if (enabled) {
			editName.requestFocus();
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
		EditText editName = (EditText) findViewById(R.id.txtName);
		imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
		EditText editCell = (EditText) findViewById(R.id.editCell);
		imm.hideSoftInputFromWindow(editCell.getWindowToken(), 0);

	}


	private void initContact(int id) {

		ContactDataSource ds = new ContactDataSource(ChildActivity.this);

		ds.open();
		currentContact = ds.getSpecificContact(id);
		ds.close();
		
		EditText editName = (EditText) findViewById(R.id.txtName);
		EditText editCell = (EditText) findViewById(R.id.editCell);
		TextView birthDay = (TextView) findViewById(R.id.textBirthday);
		
		editName.setText(currentContact.getContactName());
		editCell.setText(currentContact.getCellNumber());
		birthDay.setText(DateFormat.format("MM/dd/yyyy", currentContact.getBirthday().toMillis(false)).toString());		
	}


	@Override
	public void didFinishDatePickerDialog(Time selectedTime) {
		TextView birthDay = (TextView) findViewById(R.id.textBirthday);
		birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime.toMillis(false)).toString());		
	}
    
}
