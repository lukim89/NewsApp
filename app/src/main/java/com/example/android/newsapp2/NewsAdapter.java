package com.example.android.newsapp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private boolean mThumbnails;

    NewsAdapter(Context context, List<News> news, boolean thumbnails) {
        super(context, 0, news);
        mThumbnails = thumbnails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        View listItemView = convertView;
        News currentNews = getItem(position);

        TextView sectionTextView = listItemView.findViewById(R.id.news_section_text_view);
        String section = currentNews.getSection();
        sectionTextView.setText(section);

        TextView titleTextView = listItemView.findViewById(R.id.news_title_text_view);
        String title = currentNews.getTitle();
        titleTextView.setText(title);

        ImageView thumbnailImageView = listItemView.findViewById(R.id.news_thumbnail_image_view);
        if (mThumbnails) {
            if (!(currentNews.getThumbnail() == null)) {
                thumbnailImageView.setVisibility(View.VISIBLE);
                String thumbnail = currentNews.getThumbnail();
                new DownloadImageTask(thumbnailImageView)
                        .execute(thumbnail);
            }
        } else {
            thumbnailImageView.setVisibility(View.GONE);
        }

        TextView contentTextView = listItemView.findViewById(R.id.news_trail_text_view);
        String trailText = currentNews.getTrailText();
        contentTextView.setText(stripHtml(trailText));

        TextView authorTextView = listItemView.findViewById(R.id.news_author_text_view);
        if (!(currentNews.getAuthor() == null)) {
            String author = currentNews.getAuthor();
            authorTextView.setText(author);
        } else authorTextView.setVisibility(View.GONE);

        TextView dateTextView = listItemView.findViewById(R.id.news_date_text_view);
        if (!(currentNews.getDate() == null)) {
            String date = currentNews.getDate();
            dateTextView.setText(date);
        } else dateTextView.setVisibility(View.GONE);

        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;

        DownloadImageTask(ImageView imageView) {
            this.mImageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("Error no image", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap downloadedBitmap) {
            mImageView.setAdjustViewBounds(true);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageView.setImageBitmap(downloadedBitmap);
        }
    }

    public String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.escapeHtml(html);
        }
    }
}
