package com.example.daim.vkandrarchex.screen.photo;

import android.app.Activity;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daim.vkandrarchex.R;
import com.example.daim.vkandrarchex.content.Photo;
import com.example.daim.vkandrarchex.screen.general.LoadingDialog;
import com.example.daim.vkandrarchex.screen.general.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity implements LifecycleRegistryOwner,
        PhotoPagerAdapter.PhotoPagerAdapterListener {

    public static final String LOG_TAG = "PhotoActivity1";

    public static final String IMAGE_POSIION = "image_position";

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);


    @BindView(R.id.photo_view_pager)
    PhotoViewPager mPagerView;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayot;

    @BindView(R.id.photo_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.photo_text)
    TextView mPhoto_textView;

    @BindView(R.id.text_comments)
    TextView mCommentsView;

    @BindView(R.id.text_likes)
    TextView mLikesView;

    @BindView(R.id.text_repos)
    TextView mReposView;

    @BindView(R.id.meta_info_view)
    LinearLayout mMetaInfoLayout;

    private int mPhotoPosition;
    private boolean mMetaInfoFlag = true;
    private PhotoPagerAdapter mPagerAdapter;
    private PhotoViewModel mPhotoViewModel;
    private LoadingView mLoadingView;

    public static void navigate(@NonNull Activity activity, @NonNull int position) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra(IMAGE_POSIION, position);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setToolbarPadding();
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPhotoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        mPhotoViewModel.init();
        mPhotoViewModel.getPhotos().observe(this, photos->{mPagerAdapter.notifyDataSetChanged();});
        mPhotoViewModel.getIsLoading().observe(this, isLoading->{
            if(isLoading) mLoadingView.showLoading();
            else mLoadingView.hideLoading();
        });

        mToolbar.setNavigationOnClickListener((View v)->{onBackPressed();});
        mPhotoPosition = getIntent().getIntExtra(IMAGE_POSIION, 0);
        setMetaInfo(mPhotoPosition);

        mPagerAdapter = new PhotoPagerAdapter(mPhotoViewModel.getPhotos().getValue());
        mPagerAdapter.attachClickViewListener(this);
        mPagerView.setAdapter(mPagerAdapter);
        mPagerView.setCurrentItem(mPhotoPosition);

        setOnPagerChangeListener();
    }

    private void setOnPagerChangeListener(){

        mPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                setMetaInfo(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void setMetaInfo(int position){
        int photoNumb = position + 1;
        Photo photo = mPhotoViewModel.getPhotos().getValue().get(position);
        getSupportActionBar().setTitle(""+ photoNumb + " from " + mPhotoViewModel.getPhotoQuantity());
        mPhoto_textView.setText(photo.getText());
        mLikesView.setText(Integer.toString(photo.getLikes().getCount()));
        mReposView.setText(Integer.toString(photo.getReposts().getCount()));
        //mCommentsView.setText(photo.getComments().getCount().toString());
    }

    private void setToolbarPadding(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) return;
        int result  = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0){
            result = this.getResources().getDimensionPixelSize(resourceId);
            mAppBarLayot.setPadding(0, result, 0, 0);
        }
    }


    @Override
    public void onClickView() {
        if(mMetaInfoFlag){
            mAppBarLayot.setVisibility(View.INVISIBLE);
            mMetaInfoLayout.setVisibility(View.INVISIBLE);
        }
        else{
            mAppBarLayot.setVisibility(View.VISIBLE);
            mMetaInfoLayout.setVisibility(View.VISIBLE);
        }
        mMetaInfoFlag = !mMetaInfoFlag;
    }

    @Override
    public void loadPhoto(@NonNull ImageView target, @NonNull String url) {
        mPhotoViewModel.loadPhotoImage(target, url);
    }

    @Override
    public void paginationRequest() {
        mPhotoViewModel.loadPhotos();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
