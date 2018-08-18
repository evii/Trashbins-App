package cz.optimization.odpadky.retrofit_data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
    private CustomClusterRenderer mRenderer;
    private TrashbinClusterItem mTrashbinClusterItem;

    public FetchContainersAtPlaceViewModel (@NonNull Application application, String placeId, CustomClusterRenderer renderer, TrashbinClusterItem trashbinClusterItem) {
        super(application);
        mPlaceId = placeId;
        mRenderer = renderer;
        mTrashbinClusterItem = trashbinClusterItem;
    }

    public LiveData<List<Container>> FetchContainersAtPlace(String placeId, CustomClusterRenderer renderer, TrashbinClusterItem trashbinClusterItem) {
        if (mContainers == null) {
            mContainers = new MutableLiveData<List<Container>>();
            fetchContainersAtPlace(placeId, renderer, trashbinClusterItem);
        }
        return mContainers;
    }

    private void fetchContainersAtPlace(String placeId, final CustomClusterRenderer renderer, final TrashbinClusterItem trashbinClusterItem) {

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<Container.ContainersResult> call = service.getContainersList(placeId);
        call.enqueue(new Callback<Container.ContainersResult>() {
            @Override
            public void onResponse(Call<Container.ContainersResult> call, Response<Container.ContainersResult> response) {

                List<Container> containers = response.body().getResults();

                Gson gson = new Gson();
                String containersString = gson.toJson(containers);

                // Puting data about Containers at given place to SharedPreferences
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(MapsActivity.PREFS_NAME, MapsActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MapsActivity.PREFS_KEY, containersString);
                editor.commit();

                Marker marker = renderer.getMarker(trashbinClusterItem);
                marker.showInfoWindow();

            }

            @Override
            public void onFailure(Call<Container.ContainersResult> call, Throwable t) {

                Toast.makeText(getApplication(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });


    }


}
