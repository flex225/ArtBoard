package com.yantur.artboard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.yantur.artboard.loading.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View listLayout;
    private MyView myView;
    FloatingActionButton fab;
    FloatingActionMenu fam;
    ImageLoader imageLoader;
    Boolean isClosed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (MyView) findViewById(R.id.my_view);
        listLayout = findViewById(R.id.list_layout);
        addFab();

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        imageLoader = new ImageLoader(getApplicationContext(), gridView);
        imageLoader.execute(getImagesPath());

        gridView.setOnItemClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                Bitmap bitmap = myView.getBitmap();
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "image", null));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Choose App"));
                break;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);

        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Bitmap> bitmaps = imageLoader.getBitmaps();
        myView.addPhotoToCanvas(bitmaps.get(position));
        openClosePhotos(400);
        isClosed = !isClosed;
    }

    @Override
    public void onClick(View v) {

        if (v.getTag().equals("addCircle")) {
            Toast.makeText(getApplicationContext(), "Circle", Toast.LENGTH_LONG).show();
            myView.addCircle();


        } else if (v.getTag().equals("addPhoto")) {
            Toast.makeText(getApplicationContext(), "Photo", Toast.LENGTH_LONG).show();
            fam.close(true);
            if (isClosed) {
                openClosePhotos(-400);
            } else {
                openClosePhotos(400);
            }
            isClosed = !isClosed;

        }

    }

    private void openClosePhotos(float transition) {
        listLayout.animate().translationYBy(transition).start();
        myView.animate().translationYBy(transition).start();
        fab.animate().translationYBy(transition).start();
    }


    private void addFab() {
        ImageView icon1 = new ImageView(this);
        icon1.setImageResource(R.drawable.a);
        ImageView icon2 = new ImageView(this);
        icon2.setImageResource(R.drawable.a);
        ImageView icon3 = new ImageView(this);
        icon3.setImageResource(R.drawable.a);

        fab = new FloatingActionButton.Builder(this)
                .setContentView(icon1)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        SubActionButton addPhoto = itemBuilder.setContentView(icon2).build();
        SubActionButton addCircle = itemBuilder.setContentView(icon3).build();
        addCircle.setTag("addCircle");
        addPhoto.setTag("addPhoto");

        fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(addCircle)
                .addSubActionView(addPhoto)
                .attachTo(fab)
                .build();
        addPhoto.setOnClickListener(this);
        addCircle.setOnClickListener(this);
    }

    public ArrayList<String> getImagesPath() {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = getContentResolver().query(uri, null, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(projection[0]);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(projection[1]);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }

}
