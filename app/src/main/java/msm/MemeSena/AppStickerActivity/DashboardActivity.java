package msm.MemeSena.AppStickerActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.ads.InterstitialAd;

import msm.MemeSena.Adapter.CustomViewPagerAdapter;
import msm.MemeSena.BuildConfig;
import msm.MemeSena.Utils.AdmobAdsClass;
import msm.MemeSena.Base.BaseActivity;
import msm.MemeSena.Utils.CustomEnableDisbaleSwipeViewPager;
import msm.MemeSena.Utils.DataArchiver;
import msm.MemeSena.Fragments.FragStickerMakerList;
import msm.MemeSena.Fragments.FragServerStickersListAndSavedStickersList;
import msm.MemeSena.Model.TabDetailsModel;
import msm.MemeSena.R;
import msm.MemeSena.Utils.GlobalFun;

public class DashboardActivity extends BaseActivity {
    DrawerLayout drawer;
    CustomEnableDisbaleSwipeViewPager mViewPager;
    BottomNavigationView bottomNavigationView;
    GlobalFun globalFun;
    CustomViewPagerAdapter customViewPagerAdapter;
    TextView txtVersionCode;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardactivity);

        interstitialAd = new InterstitialAd(this, "2987994584650084_2988001724649370");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog()
                    .penaltyDeath().build());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalFun = new GlobalFun();

        //----------- setup drawer
        txtVersionCode = findViewById(R.id.txtVersionCode);
        txtVersionCode.setText("App : v" + BuildConfig.VERSION_NAME);
        drawer = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.labal_open, R.string.labal_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView drawerNavBar = (NavigationView) findViewById(R.id.drawerNavigationBar);
        drawerNavBar.setNavigationItemSelectedListener(drawerOnNavigationItemSelectedListener);

        //----------- Bottom navigation
        mViewPager = findViewById(R.id.mViewPager);
        mViewPager.setSwipeEnabled(false);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        setupViewPagerWithBottomNavigationbar();

        //---------- setup admob
        AdmobAdsClass admobObj = new AdmobAdsClass();
        admobObj.loadIntrestrialAds(this);

        /*AdView mAdView = findViewById(R.id.adView);
        if (admobObj.isNetworkAvailable(this)) {
            //------- init banner ads
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mAdView.setVisibility(View.GONE);
        }*/

        //------------ import zip file
        if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                if (uri != null) {
                    DataArchiver.importZipFileToStickerPack(uri, this);
                }
            }
        }
    }

    NavigationView.OnNavigationItemSelectedListener drawerOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            drawer.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()) {
                case R.id.menu_findmoreapps:
                    globalFun.moreApp(DashboardActivity.this);
                    break;
                case R.id.menu_shareapp:
                    globalFun.shareApp(DashboardActivity.this);
                    break;
                case R.id.menu_rateapp:
                    globalFun.RateUsApp(DashboardActivity.this, getPackageName());
                    break;
                case R.id.menu_privacypolicy:
                    globalFun.privacyPolicy(DashboardActivity.this);
                    break;
                case R.id.menu_insta:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/memesenaofficial/")));
                    break;
                case R.id.menu_twitter:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/memesenamedia?s=09")));
                case R.id.menu_telegram:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/memesena")));
                case R.id.menu_website:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.memesena.com")));
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
           ;
            interstitialAd.loadAd();
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPagerWithBottomNavigationbar() {
        customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(new TabDetailsModel("", new FragServerStickersListAndSavedStickersList()));
        customViewPagerAdapter.addFragment(new TabDetailsModel("", new FragStickerMakerList()));

        mViewPager.setAdapter(customViewPagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_stickersList:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_stickermaker:
                        interstitialAd.loadAd();
                        mViewPager.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });
    }

    /*SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sticker_list_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        if (mViewPager.getCurrentItem() == 0)
            ((FragStickerMakerList) customViewPagerAdapter.getItem(0)).SerchingQueryText(text);
        else if (mViewPager.getCurrentItem() == 1)
            ((FragServerStickersListAndSavedStickersList) customViewPagerAdapter.getItem(1)).SerchingQueryText(text);
        return false;
    }

    public void resetSearchText() {
        try {
            //searchView.setQuery("", true);
            searchView.onActionViewCollapsed();
        } catch (Exception e) {
        }
    }*/
}