package com.example.xyzreader.presentation.article_detail;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.legacy.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.xyzreader.R;
import com.example.xyzreader.domain.Book;

import java.util.List;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    public static final String PAGE = "page";
    private long mStartId;

    private int mSelectedItemId;
    private int mSelectedItemUpButtonFloor = Integer.MAX_VALUE;
    private int mTopInset;
    ArticleDetailViewModel viewModel;

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private Toolbar mUpButton;
    private ConstraintLayout mContainerProgressBar;
    private ConstraintLayout mContainerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        viewModel = ViewModelProviders.of(this).get(ArticleDetailViewModel.class);

        mContainerProgressBar = findViewById(R.id.container_progress_bar);

        mSelectedItemId = getIntent().getIntExtra(PAGE, 0);

        viewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                Log.d(TAG, "Updating book from LiveData in ViewModel");
                mPagerAdapter = new MyPagerAdapter(getFragmentManager(), books);
                mPager = (ViewPager) findViewById(R.id.pager);
                mPager.setAdapter(mPagerAdapter);
                mPager.setPageMargin((int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
                mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));

                mPager.setCurrentItem(mSelectedItemId);

                mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrollStateChanged(int state) {
                        super.onPageScrollStateChanged(state);
                        mUpButton.animate()
                                .alpha((state == ViewPager.SCROLL_STATE_IDLE) ? 1f : 0f)
                                .setDuration(300);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        mSelectedItemId = position;
                        updateUpButtonPosition();
                    }
                });
                hide();
            }
        });



        mUpButton = findViewById(R.id.up_container);

        mUpButton.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSupportNavigateUp();
            }
        });
    }

    public void hide() {
        mContainerProgressBar.setVisibility(View.GONE);
    }

    public void onUpButtonFloorChanged(long itemId, ArticleDetailFragment fragment) {
        if (itemId == mSelectedItemId) {
            mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
            updateUpButtonPosition();
        }
    }

    private void updateUpButtonPosition() {
        int upButtonNormalBottom = mTopInset + mUpButton.getHeight();
        mUpButton.setTranslationY(Math.min(mSelectedItemUpButtonFloor - upButtonNormalBottom, 0));
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        private List<Book> mBooks;

        public MyPagerAdapter(FragmentManager fm, List<Book> books) {
            super(fm);
            mBooks = books;
        }

        @Override
        public Fragment getItem(int position) {
            return ArticleDetailFragment.newInstance(mBooks.get(position));
        }

        @Override
        public int getCount() {
            return mBooks.size();
        }
    }
}
