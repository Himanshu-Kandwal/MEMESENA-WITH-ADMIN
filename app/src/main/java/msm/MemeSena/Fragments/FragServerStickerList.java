package msm.MemeSena.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import msm.MemeSena.Adapter.ExploreListAdapter;
import msm.MemeSena.Api.ApiService;
import msm.MemeSena.Base.BaseFragment;
import msm.MemeSena.Interface.SerchingInterface;
import msm.MemeSena.Model.ApiStickersListResponse;
import msm.MemeSena.R;
import msm.MemeSena.Utils.AdmobAdsClass;
import msm.MemeSena.Utils.GlobalFun;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FragServerStickerList extends BaseFragment implements SerchingInterface {
    ApiService apiService;
    RecyclerView rvExplore;
    ExploreListAdapter exploreAdapter;
    ArrayList<ApiStickersListResponse.Data> ArrStickerPacks = new ArrayList<>();
    AdmobAdsClass admobAdsClass;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView txtErrorMessage;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_serverstickerslist;
    }

    @Override
    protected void initViewsHere() {
        apiService = getApiClientObj();

        admobAdsClass = new AdmobAdsClass();

        txtErrorMessage = getView().findViewById(R.id.txtErrorMessage);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        rvExplore = getView().findViewById(R.id.rvExplore);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvExplore.setLayoutManager(linearLayoutManager);
        exploreAdapter = new ExploreListAdapter(getActivity(), ArrStickerPacks, admobAdsClass);
        rvExplore.setAdapter(exploreAdapter);

        // getAllStickersList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // getAllStickersList();
                callApi();

            }
        });
        APICALL();
    }

    private void APICALL() {
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("offline", Context.MODE_PRIVATE);
        String data = preferences.getString("data", "");
        if (data.equals("")) {

            swipeRefreshLayout.setRefreshing(true);

            if (GlobalFun.isInternetConnected(getActivity())) {

                callApi();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                showErrorTextBox("Fail to get response Please Check Your Internet Connection");
            }
        } else {
           Offlinedata();
        }
    }

    @SuppressLint("CheckResult")
    private void callApi(){
        if (GlobalFun.isInternetConnected(getActivity())){
            Toast.makeText(getContext(), "Api Call", Toast.LENGTH_SHORT).show();
            apiService.getAllStickers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<JsonObject>() {
                        @Override
                        public void onSuccess(JsonObject response) {
                            txtErrorMessage.setVisibility(View.GONE);
                            rvExplore.setVisibility(View.VISIBLE);
                            ArrStickerPacks.clear();
                            swipeRefreshLayout.setRefreshing(false);
                            try {
                                JSONObject jsonResponse = new JSONObject(response.toString());
                                if (jsonResponse != null) {
                                    boolean result = jsonResponse.getBoolean("result");
                                    if (result) {
                                        Gson g = new Gson();
                                        ArrayList<ApiStickersListResponse.Data> allSticker = g.fromJson(jsonResponse.getString("data"),
                                                new TypeToken<ArrayList<ApiStickersListResponse.Data>>() {
                                                }.getType());
                                        SharedPreferences preferences = getContext().getSharedPreferences("offline", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.clear();
                                        Gson gson = new Gson();
                                        String data = gson.toJson(allSticker);
                                        editor.putString("data", data);
                                        editor.apply();
                                        ArrStickerPacks.addAll(allSticker);

                                        exploreAdapter.notifyDataSetChanged();


                                    } else {
                                        String errorMessage = jsonResponse.getString("message");
                                        showErrorTextBox(errorMessage);
                                    }
                                }
                            } catch (Exception e) {
                                showErrorTextBox("Fail to parse data");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            swipeRefreshLayout.setRefreshing(false);

                            //  showErrorTextBox("Fail to get response");
                        }
                    });
        }else {
            swipeRefreshLayout.setRefreshing(false);
            APICALL();
            Offlinedata();
        }

    }
    @SuppressLint("CheckResult")
    private void getAllStickersList() {
        swipeRefreshLayout.setRefreshing(true);
        if (GlobalFun.isInternetConnected(getActivity())) {
            Offlinedata();
        } else {
            Offlinedata();

        }
    }

    private void Offlinedata() {

        swipeRefreshLayout.setRefreshing(false);
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("offline", Context.MODE_PRIVATE);
        String data = preferences.getString("data", "");
        if (data.equals("")){
            swipeRefreshLayout.setRefreshing(false);
            showErrorTextBox("Fail to get response");
        }else {
            Type collectionType = new TypeToken<ArrayList<ApiStickersListResponse.Data>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<ApiStickersListResponse.Data> allSticker = gson.fromJson(data, collectionType);
            ArrStickerPacks.clear();
            ArrStickerPacks.addAll(allSticker);
            exploreAdapter.notifyDataSetChanged();

        }

    }

    private void showErrorTextBox(String error) {
        rvExplore.setVisibility(View.GONE);
        txtErrorMessage.setVisibility(View.VISIBLE);
        txtErrorMessage.setText(error);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getAllStickersList();
    }

    @Override
    public void SerchingQueryText(String searchText) {
        exploreAdapter.getFilter().filter(searchText);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser && exploreAdapter != null) {
                exploreAdapter.getFilter().filter("");
            }
        } catch (Exception e) {

        }
    }
}