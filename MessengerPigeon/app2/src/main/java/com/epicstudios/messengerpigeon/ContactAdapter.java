package com.epicstudios.messengerpigeon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chewy on 4/4/2018.
 */

public class ContactAdapter extends BaseAdapter {
    private List<Pair<Long, String>> objects;
    private Activity context;
    private int layout;
    private HashMap<Long, Bitmap> photos;
    private List<Boolean> checked;

    ContactAdapter(Activity context, int layout, List<Pair<Long, String>> objects, HashMap photos) {
        //super(context, layout);//, objects);
        Log.d("AdapterContact:", "Created ContactAdapter with people "+ objects.toString());
        this.objects = objects;
        this.context = context;
        this.layout = layout;
        this.photos = photos;
        Log.d("AdapterContact:", "Created ContactAdapter with photos "+ photos.toString());
        checked = new ArrayList<>(objects.size());
        for(int x = 0; x < objects.size(); x++) { checked.add(false); }
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    public long getItemId(int position){  return position; }

    public Boolean isChecked(int position){ return checked.get(position); }
    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent){
        Log.d("AdapterContact:", "Called getView with position "+position);
        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = context.getLayoutInflater();
                //(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout, null);
        }
        //Log.d("AdapterContact:", "Called getView with view "+v.toString());
        Drawable draw = context.getResources().getDrawable(R.drawable.default_profile);

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Pair<Long, String> i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView tt = (TextView) v.findViewById(R.id.name);
            ImageView iv = (ImageView) v.findViewById(R.id.ContactPhoto);
            final CheckBox ckbx = (CheckBox) v.findViewById(R.id.chk_box);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tt != null){
                tt.setText(i.second);
            }
            if (iv != null) {
                if (photos.containsKey(i.first)) {
                    Log.d("AdapterContact:", "photos triggered with id " + i.first);
                    iv.setImageBitmap(photos.get(i.first));
                } else {
                    iv.setImageDrawable(draw);
                }
            }
            if (ckbx != null) {
                ckbx.setChecked(checked.get(position));
                ckbx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checked.set(position, ckbx.isChecked());
                    }
                });
            }

        }
        // the view must be returned to our activity
        return v;

    }
}
