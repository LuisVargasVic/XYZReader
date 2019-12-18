package com.example.xyzreader.ui.articles;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.xyzreader.R;
import com.example.xyzreader.domain.Book;
import com.example.xyzreader.remote.ConnectionListener;
import com.example.xyzreader.remote.MainReceiver;
import com.example.xyzreader.remote.RemoteListener;
import com.example.xyzreader.ui.ArticleDetailActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, RemoteListener, ConnectionListener {

    private static final String TAG = ArticleListActivity.class.toString();
    private ArticleViewModel viewModel;
    private BroadcastReceiver mReceiver;
    private ArticleAdapter mAdapter;
    private static ConnectionListener connectionListener;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        // Initialize connection receiver before making requests
        mReceiver = new MainReceiver();
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        connectionListener = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ArticleAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Make requests
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);

    }

    @Override
    public void onRefresh() {
        viewModel.refresh( this);
    }

    public static void setMainConnection(Boolean connection) {
        connectionListener.connection(connection);
    }

    @Override
    public void connection(Boolean connection) {
        viewModel.refresh( this);
        if (connection) snackbar = Snackbar.make(mSwipeRefreshLayout, getString(R.string.books_empty), Snackbar.LENGTH_INDEFINITE);
        else snackbar = Snackbar.make(mSwipeRefreshLayout, getString(R.string.books_connection), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void preExecute() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void postExecute() {
        mSwipeRefreshLayout.setRefreshing(false);
        viewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");
                mAdapter.setBooks(books);
                if (snackbar != null) snackbar.dismiss();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }
}
