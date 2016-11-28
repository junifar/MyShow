package com.rubahapi.myshow;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_ID = "ID";
    public static final int LOADER_RAMALAN_DETAIL = 200;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.ID = getIntent().getIntExtra(EXTRA_ID, 0);

        getLoaderManager().initLoader(LOADER_RAMALAN_DETAIL, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if(i == LOADER_RAMALAN_DETAIL){
            Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
            return new CursorLoader(MovieDetail.this,
                    uri,
                    LatestMovieActivity.MOVIE_COLUMNS,null,null,null);
        }
        return null;
    }

    private int getArrayPosition(String value){
        int i = 0;
        for (String movieColumn : LatestMovieActivity.MOVIE_COLUMNS){
            if(movieColumn == value){
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
//        TextView textView = (TextView) findViewById(textView);
        cursor.moveToPosition(this.ID);
//        textView.setText(cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH)));

        Context context = imageView.getContext();
        Picasso.with(context).load("http://image.tmdb.org/t/p/w500" + cursor.getString(getArrayPosition(MovieDBHelper.COLUMN_IMAGE_PATH))).into(imageView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
