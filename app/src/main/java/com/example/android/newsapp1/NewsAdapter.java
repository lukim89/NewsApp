package com.example.android.newsapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.news_title);
        String title = currentNews.getTitle();
        titleTextView.setText(title);

        TextView contentTextView = listItemView.findViewById(R.id.news_trail_text);
        String trailText = currentNews.getTrailText();
        contentTextView.setText(trailText);

        TextView sectionTextView = listItemView.findViewById(R.id.news_section);
        String section = currentNews.getSection();
        sectionTextView.setText(section);

        TextView authorTextView = listItemView.findViewById(R.id.news_author);
        if (!(currentNews.getAuthor() == null)) {
            String author = currentNews.getAuthor();
            authorTextView.setText(author);
        } else authorTextView.setVisibility(View.GONE);

        TextView dateTextView = listItemView.findViewById(R.id.news_date);
        if (!(currentNews.getDate() == null)) {
            String date = currentNews.getDate();
            dateTextView.setText(date);
        } else dateTextView.setVisibility(View.GONE);

        return listItemView;
    }
}