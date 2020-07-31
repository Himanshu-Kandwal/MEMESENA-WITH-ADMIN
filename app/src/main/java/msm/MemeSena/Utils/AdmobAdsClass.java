package msm.MemeSena.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import msm.MemeSena.Interface.admobCloseEvent;
import msm.MemeSena.R;

import static msm.MemeSena.Base.BaseActivity.adDisplayCount;
import static msm.MemeSena.Base.BaseActivity.getAdDisplayIntervalCount;

public class AdmobAdsClass {
    static InterstitialAd mInterstitialAd;

    public static void loadIntrestrialAds(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                mInterstitialAd = new InterstitialAd(context);
                mInterstitialAd.setAdUnitId(context.getString(R.string.interstitialAdmob_Id));
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        //Toast.makeText(context, "Fail to display ads", Toast.LENGTH_SHORT).show();
                        mInterstitialAd = null;
                    }
                });
            } catch (Exception e) {
            }
        } else {
            mInterstitialAd = null;
        }
    }

    public void showIntrestrialAds(Context context, admobCloseEvent closeEvent) {
        try {
            if (!isNetworkAvailable(context) || mInterstitialAd == null) {
                closeEvent.setAdmobCloseEvent();
                return;
            }
            if (adDisplayCount % getAdDisplayIntervalCount() == 0) {
                adDisplayCount = 0;
                if (mInterstitialAd != null) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                adDisplayCount++;
                                closeEvent.setAdmobCloseEvent();
                                loadIntrestrialAds(context);
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                adDisplayCount++;
                                closeEvent.setAdmobCloseEvent();
                            }
                        });
                    } else {
                        closeEvent.setAdmobCloseEvent();
                        loadIntrestrialAds(context);
                    }
                } else {
                    closeEvent.setAdmobCloseEvent();
                    loadIntrestrialAds(context);
                }
            } else {
                closeEvent.setAdmobCloseEvent();
                adDisplayCount++;
            }
        } catch (Exception e) {
            closeEvent.setAdmobCloseEvent();
            loadIntrestrialAds(context);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}