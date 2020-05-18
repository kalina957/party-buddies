package com.example.partybuddies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.partybuddies.R;

public class ImageAdapterBand extends BaseAdapter {
    Context ctx;
    String[] images;
    ImageAdapterBand(Context ctx,String[] images){
    this.ctx = ctx;
    this.images = images;
            }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return images[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View gridView = view;
        if(gridView == null){
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.band_image_layout,null);

        }
        ImageView img = (ImageView)gridView.findViewById(R.id.imgBand);
        Glide.with(img).load(images[i]).into(img);
        return gridView;
    }
}
