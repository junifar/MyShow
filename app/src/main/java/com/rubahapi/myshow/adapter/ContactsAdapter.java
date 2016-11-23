package com.rubahapi.myshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rubahapi.myshow.R;
import com.rubahapi.myshow.data.Contact;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prasetia on 11/22/2016.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> mContacts;
    private Context mContext;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        mContacts = contacts;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        Contact contact = mContacts.get(position);

//        TextView textView = holder.nameTextView;
//        textView.setText(contact.getmName());

//        TextView textView1 = holder.onlineTextView;
//        String isOnline = (contact.ismOnline()) ? "Online" : "Offline";
//        textView1.setText(contact.getmImagePath());

        ImageView imageView = holder.showImageView;
        Picasso.with(mContext).load(contact.getmImagePath()).into(imageView);

//        Button button = holder.messageButton;
//        button.setText("Message");

//        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount(){
        return mContacts.size();
    }

//    private Context getContext(){
//        return  mContext;
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView nameTextView;
//        public TextView onlineTextView;
        public ImageView showImageView;

//        public Button messageButton;
        public ViewHolder(View itemView) {
            super(itemView);

//            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
//            onlineTextView = (TextView) itemView.findViewById(R.id.online_status);
//            messageButton = (Button) itemView.findViewById(R.id.message_button);
            showImageView = (ImageView) itemView.findViewById(R.id.image_view);
        }

    }

}
