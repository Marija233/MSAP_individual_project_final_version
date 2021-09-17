package com.example.msap_individual_project_final_version.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONObject;
import androidx.appcompat.app.AlertDialog;
import com.example.msap_individual_project_final_version.R;
import com.example.msap_individual_project_final_version.adapter.EarthquakeAdapter;
import com.example.msap_individual_project_final_version.model.EarthquakeBean;
import com.example.msap_individual_project_final_version.model.FeatureBean;
import com.example.msap_individual_project_final_version.network.ApiService;
import com.example.msap_individual_project_final_version.network.RetrofitClientInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.msap_individual_project_final_version.settings.SettingsActivity;
import com.example.msap_individual_project_final_version.sync.EarthquakeSyncUtils;
import com.example.msap_individual_project_final_version.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class EarthquakeActivity extends AppCompatActivity implements EarthquakeAdapter.OnEarthquakeClickListener {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String EARTHQUAKE_SHARE_HASHTAG = " #EarthquakeApplication";
    private Context context;
    private EarthquakeAdapter earthquakeAdapter;
    private List<FeatureBean> featureBeanList;
    private RecyclerView recyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private TextView mEmptyStateTextView;
    private AlertDialog loadingDialog;
    boolean doubleBackToExitPressedOnce = false;
    private SwipeRefreshLayout pullToRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);
        context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.xml.layout_loading_dialog);
        loadingDialog = builder.create();
        loadingDialog.show();


        featureBeanList = new ArrayList<>();
        earthquakeAdapter = new EarthquakeAdapter(this, featureBeanList, this);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EarthquakeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(earthquakeAdapter);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Timber.e("pullToRefresh.setOnRefreshListener");

                init();
                pullToRefresh.setColorSchemeResources(R.color.orange, R.color.red,  R.color.intensity3, R.color.redAlert);
                pullToRefresh.setRefreshing(false);
            }
        });

        init();

        EarthquakeSyncUtils.initialize(context);
    }


    public void init() {
        Map<String, String> parameters = Utils.getDefaultParamsQuery(context);

        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<EarthquakeBean> call = service.getEarthQuakes(parameters);
        call.enqueue(new Callback<EarthquakeBean>() {
            @Override
            public void onResponse(Call<EarthquakeBean> call, Response<EarthquakeBean> response) {
                String result;
                if (response != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        loadingDialog.dismiss();
                        generateDataList(response.body());
                    } catch (Exception e) {
                        Timber.e("Exception: %s", e.getLocalizedMessage());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EarthquakeBean> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(EarthquakeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generateDataList(EarthquakeBean earthquakeBean) {
        Timber.e("earthquakeBean: %s", earthquakeBean.toString());
        featureBeanList = earthquakeBean.getFeatures();

        earthquakeAdapter.setFeatureList(featureBeanList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_tune) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onEarthquakeClick(int position) {
        Timber.e("position: %s", featureBeanList.get(position));

        FeatureBean featureBean = featureBeanList.get(position);

        Intent intent = new Intent(this, DetailEarthquakeActivity.class);
        intent.putExtra("position", featureBean);
        startActivity(intent);

    }


}