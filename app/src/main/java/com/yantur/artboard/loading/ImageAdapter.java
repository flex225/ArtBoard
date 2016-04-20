package com.yantur.artboard.loading;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yantur.artboard.R;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<Bitmap> {

    public ImageAdapter(Context context, List<Bitmap> resource) {
        super(context, R.layout.list_img_row, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.list_img_row, parent, false);

        Bitmap image = getItem(position);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageView0);

        imageView.setImageBitmap(image);

        return customView;
    }
}
