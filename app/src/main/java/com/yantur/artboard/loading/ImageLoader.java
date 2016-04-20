package com.yantur.artboard.loading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class ImageLoader extends AsyncTask<List<String>,Void,List<Bitmap>> {
    private Context context;
    private GridView gridView;
    private ImageAdapter adapter;
    List<Bitmap> bitmaps;

    public ImageLoader(Context context,GridView gridView) {
        this.context = context;
        this.gridView = gridView;
        bitmaps = new ArrayList<>();
    }

    @Override
    protected List<Bitmap> doInBackground(List<String>... params) {
        List<String> paths = params[0];
        for (String curr:
             paths) {
            bitmaps.add(decodeSampledBitmap(curr, 100, 100));
        }

        return bitmaps;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        if (bitmaps!=null) {
            adapter = new ImageAdapter(context,bitmaps);
            gridView.setAdapter(adapter);
        }
    }

    public List<Bitmap> getBitmaps(){
        return bitmaps;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmap(String path, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
