package adapters;
/**
 * Created by mtmwi on 31.05.2016.
 */

import android.widget.CheckBox;


import android.content.Context;
import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
import android.widget.TextView;

import com.locateandgetlocated.locategetlocated.R;

import java.util.ArrayList;
import java.util.List;

import extra.Contact;

    public class SelectUserAdapter extends BaseAdapter {


        private ArrayList<Contact> contacts;
        Context context;
        ViewHolder viewHolder;

        public SelectUserAdapter(List<Contact> selectedUsers, Context context) {
            this.context = context;
            this.contacts = new ArrayList<Contact>();
            this.contacts.addAll(selectedUsers);
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int i) {
            return contacts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            View view = convertView;
            if (view == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.contacts_view, null);
                Log.e("Inside", "here--------------------------- In view1");
            } else {
                view = convertView;
                Log.e("Inside", "here--------------------------- In view2");
            }

            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) view.findViewById(R.id.name);
            viewHolder.check = (CheckBox) view.findViewById(R.id.check);
            viewHolder.phone = (TextView) view.findViewById(R.id.no);

            final Contact data = (Contact) contacts.get(i);
            viewHolder.title.setText(data.getName());
            viewHolder.check.setChecked(data.isChecked());
            viewHolder.phone.setText(data.getPhoneNumber());

            view.setTag(data);
            return view;
        }

        class ViewHolder {
            TextView title, phone;
            CheckBox check;
        }

}
