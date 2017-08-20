package com.example.daim.vkandrarchex.screen.gallery;

import android.app.Activity;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.daim.vkandrarchex.R;
import com.example.daim.vkandrarchex.content.Photo;
import com.example.daim.vkandrarchex.screen.auth.AuthActivity;
import com.example.daim.vkandrarchex.screen.general.LoadingDialog;
import com.example.daim.vkandrarchex.screen.general.LoadingView;
import com.example.daim.vkandrarchex.screen.photo.PhotoActivity;
import com.example.daim.vkandrarchex.utils.PreferenceUtils;
import com.example.daim.vkandrarchex.widget.BaseAdapter;
import com.example.daim.vkandrarchex.widget.EmptyRecyclerView;
import com.example.daim.vkandrarchex.widget.PreCachingLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GalleryActivity extends AppCompatActivity implements LifecycleRegistryOwner,
        GalleryAdapter.GalleryAdapterListener, BaseAdapter.OnItemClickListener<Photo>{

    private final String LOG_TAG = "GalleryActivity";

    @BindView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;

    @BindView(R.id.empty)
    View mEmptyView;

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private GalleryAdapter mAdapter;

    private LoadingView mLoadingView;
    private GalleryViewModel mViewModel;

    public static void start(@NonNull Activity activity){
        Intent intent = new Intent(activity, GalleryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());


        int columns = getResources().getInteger(R.integer.columns_count);
        mRecyclerView.setLayoutManager(new PreCachingLayoutManager(getApplicationContext(), columns));
        mRecyclerView.setEmptyView(mEmptyView);

        mAdapter = createAdapter();
        mAdapter.attachToRecyclerView(mRecyclerView);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnPaginationRequest(this);

        mViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mViewModel.init();
        mViewModel.getPhotos().observe(this, photos->{mAdapter.refreshRecycler();});
        mViewModel.getIsLoading().observe(this, isLoading->{
            if(isLoading) mLoadingView.showLoading();
            else mLoadingView.hideLoading();
        });
        mAdapter.setData(mViewModel.getPhotos().getValue());

        mViewModel.loadPhotos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_exit){
            PreferenceUtils.saveToken("");
            AuthActivity.start();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private GalleryAdapter createAdapter(){
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.dimen.rows_count, typedValue, true);
        float rows_count = typedValue.getFloat();
        int actionBarHeight = getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)
                ? TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())
                : 0;

        int columns = getResources().getInteger(R.integer.columns_count);
        int imageHeight = (int) ((getResources().getDisplayMetrics().heightPixels - actionBarHeight) / rows_count);
        int imageWidth = getResources().getDisplayMetrics().widthPixels / columns;

        return new GalleryAdapter(imageHeight, imageWidth);
    }

    @Override
    public void paginationRequest() {
        mViewModel.loadPhotos();
    }

    @Override
    public void photoImageRequest(@NonNull ImageView imageView, @NonNull String url) {
        mViewModel.loadPhotoImage(imageView, url);
    }

    @Override
    public void onItemClick(@NonNull int position) {
        PhotoActivity.navigate(this, position);
    }
}
