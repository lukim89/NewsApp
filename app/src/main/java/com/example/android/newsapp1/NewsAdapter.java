package com.example.android.newsapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
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
        String title = currentNews.getNewsTitle();
        titleTextView.setText(title);

        TextView authorTextView = listItemView.findViewById(R.id.news_author);
        String author = currentNews.getNewsAuthor();
        authorTextView.setText(author);


        return listItemView;
    }
}
