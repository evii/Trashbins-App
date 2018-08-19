package cz.optimization.odpadky.retrofit_data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import java.util.List;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.ui.clusters.CustomClusterRenderer;
import cz.optimization.odpadky.ui.clusters.TrashbinClusterItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchContainersAtPlaceViewModel extends AndroidViewModel {

    private MutableLiveData<List<Container>> mContainers;
    private String mPlaceId;

    public FetchContainersAtPlaceViewModel(@NonNull Application application, String placeId) {
        super(application);
        mPlaceId = placeId;
    }

    public MutableLiveData<List<Container>> FetchContainersAtPlace(String placeId) {
        mContainers = new MutableLiveData<List<Container>>();
        fetchContainersAtPlace(placeId);

        return mContainers;
    }

    private void fetchContainersAtPlace(final String placeId) {

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<Container.ContainersResult> call = service.getContainersList(placeId);
        call.enqueue(new Callback<Container.ContainersResult>() {
            @Override
            public void onResponse(Call<Container.ContainersResult> call, Response<Container.ContainersResult> response) {

                mContainers.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Container.ContainersResult> call, Throwable t) {
                Toast.makeText(getApplication(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }
}
