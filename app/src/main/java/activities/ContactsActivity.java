package activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.locateandgetlocated.locategetlocated.R;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.AdapterSingleton;
import adapters.CustomRequestAdapter;
import adapters.SelectUserAdapter;
import database.DBHandler;
import database.Device;
import extra.Contact;

public class ContactsActivity extends AppCompatActivity {

        // ArrayList
        ArrayList<Contact> contacts;
    int type = 0;
        // Contact List
        ListView listView;
        // Cursor to load contacts list
        Cursor phones;
    final AdapterSingleton adapterSingleton = AdapterSingleton.getmInstance();
    public DBHandler dbHandler;

        // Pop up
        ContentResolver resolver;
        SelectUserAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contacts);
            setTitle("Wybierz kontakty");
            type = getIntent().getIntExtra("type", 0);
            dbHandler = new DBHandler(this, null, null, 1);

            contacts = new ArrayList<Contact>();
            resolver = this.getContentResolver();
            listView = (ListView) findViewById(R.id.contacts_list);

            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            new LoadContact().execute();

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                for (Contact selectedContact : contacts)
                {
                    if(selectedContact.isChecked())
                    {
                        Device device = new Device(selectedContact.getPhoneNumber(), selectedContact.getName(), type == 0 ? 1 : 2);
                        dbHandler.addDevice(device);
                        adapterSingleton.notifyCustomAdapters();
                    }
                }

                Toast.makeText(getApplicationContext(), "Dodano urządzenia.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DevicesActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        // Load data on background
        class LoadContact extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                // Get Contact list from Phone

                if (phones != null) {
                    Log.e("count", "" + phones.getCount());
                    if (phones.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                    }

                    while (phones.moveToNext()) {
                        Bitmap bit_thumb = null;
                        String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                        String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                        try {
                            if (image_thumb != null) {
                                bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                            } else {
                                Log.e("No Image Thumb", "--------------");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Contact contact = new Contact();
                        contact.setName(name);
                        contact.setPhoneNumber(phoneNumber);
                        contact.setChecked(false);
                        contacts.add(contact);
                    }
                } else {
                    Log.e("Cursor close 1", "----------------");
                }
                phones.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new SelectUserAdapter(contacts, ContactsActivity.this);
                listView.setAdapter(adapter);

                // Select item on listclick
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CheckBox check = (CheckBox) view.findViewById(R.id.check);
                        if(check.isChecked())
                        {
                            check.setChecked(false);
                            contacts.get(position).setChecked(false);
                        }
                        else
                        {
                            check.setChecked(true);
                            contacts.get(position).setChecked(true);
                        }
                    }
                });
                listView.setFastScrollEnabled(true);
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            phones.close();
        }




}
