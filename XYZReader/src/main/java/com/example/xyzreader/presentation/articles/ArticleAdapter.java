package com.example.xyzreader.presentation.articles;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.domain.Book;
import com.example.xyzreader.presentation.DynamicHeightNetworkImageView;
import com.example.xyzreader.presentation.ImageLoaderHelper;
import com.example.xyzreader.presentation.article_detail.ArticleDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static com.example.xyzreader.presentation.article_detail.ArticleDetailActivity.PAGE;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private static String TAG = ArticleAdapter.class.getSimpleName();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private static SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private static GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);
    private List<Book> mBooksList = new ArrayList<>();
    private static Context mContext;

    ArticleAdapter(Context context) {
        mContext = context;
    }

    void setBooks(List<Book> mBooksList) {
        this.mBooksList = mBooksList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.list_item_article, parent, false);
        return new ViewHolder(view);
    }

    private static Date parsePublishedDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, Objects.requireNonNull(ex.getMessage()));
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mBooksList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mBooksList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout constraintLayout;
        DynamicHeightNetworkImageView thumbnailView;
        TextView titleView;
        TextView subtitleView;
        Book mBook;

        ViewHolder(View view) {
            super(view);
            constraintLayout = view.findViewById(R.id.background);
            thumbnailView = view.findViewById(R.id.thumbnail);
            titleView = view.findViewById(R.id.article_title);
            subtitleView = view.findViewById(R.id.article_subtitle);
        }

        void bind(Book book) {
            constraintLayout.setOnClickListener(this);
            mBook = book;
            titleView.setText(book.getTitle());
            Date publishedDate = parsePublishedDate(book.getPublished_date());
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {

                subtitleView.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + "<br/>" + " by "
                                + book.getAuthor()));
            } else {
                subtitleView.setText(Html.fromHtml(
                        outputFormat.format(publishedDate)
                                + "<br/>" + " by "
                                + book.getAuthor()));
            }
            thumbnailView.setImageUrl(
                    book.getThumb(),
                    ImageLoaderHelper.getInstance(mContext).getImageLoader());
            thumbnailView.setAspectRatio(book.getAspect_ratio());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ArticleDetailActivity.class);
            Log.wtf("POSITION", String.valueOf(this.getAdapterPosition()));
            intent.putExtra(PAGE, this.getAdapterPosition());
            mContext.startActivity(intent);
        }
    }
}
