package com.example.msap_individual_project_final_version.sync;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import com.example.msap_individual_project_final_version.model.EarthquakeBean;
import com.example.msap_individual_project_final_version.model.FeatureBean;
import com.example.msap_individual_project_final_version.network.ApiService;
import com.example.msap_individual_project_final_version.network.RetrofitClientInstance;
import com.example.msap_individual_project_final_version.settings.EarthquakePreferences;
import com.example.msap_individual_project_final_version.utils.NotificationUtils;
import com.example.msap_individual_project_final_version.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class EarthquakeSyncTask {

    private static final String LOG_TAG = EarthquakeSyncTask.class.getSimpleName();
    private static List<FeatureBean> featureBeanList;

    synchronized public static void checkEarthquake(final Context context) {

        try {
            boolean notificationsEnabled = EarthquakePreferences.isNotificationsEnabled(context);
            Timber.e("notificationsEnabled: %s", notificationsEnabled);

            if (notificationsEnabled) {

                Map<String, String> parameters = Utils.getNotificationParamsQuery(context);

                ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                Call<EarthquakeBean> call = service.getEarthQuakes(parameters);
                call.enqueue(new Callback<EarthquakeBean>() {
                    @Override
                    public void onResponse(Call<EarthquakeBean> call, Response<EarthquakeBean> response) {
                        if (response != null) {
                            Timber.e("Inner response");
                            try {
                                featureBeanList = response.body().getFeatures();

                                double currMagNotification = featureBeanList.get(0).getPropertiesBean().getMag();

                                String minMagnitude = EarthquakePreferences.getMinMagnitudePreferences(context);


                                if (currMagNotification >= Double.parseDouble(minMagnitude)) {
                                    Timber.e("currMagNotification: " + currMagNotification + " >= " + "minMagnitude: " + minMagnitude);

                                    NotificationUtils.notifyUserOfNewEarthquakeReport(context, featureBeanList);
                                }
                            } catch (Exception e) {
                                Timber.e("Exception: %s", e.getLocalizedMessage());
                            }
                        } else {
                            Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EarthquakeBean> call, Throwable t) {

                        Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}