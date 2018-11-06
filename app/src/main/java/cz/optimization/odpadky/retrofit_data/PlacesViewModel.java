package cz.optimization.odpadky.retrofit_data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import cz.optimization.odpadky.objects.Place;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesViewModel extends ViewModel {
    private static final String TAG = "PlacesViewModel";

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Place>> placesList;
    // to show a progressbar while loading places
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    // display error if loading failed
    MutableLiveData<Boolean> error = new MutableLiveData<>();

    //we will call this method to get the data
    public LiveData<List<Place>> getPlaces() {
        //if the list is null
        if (placesList == null) {
            placesList = new MutableLiveData<List<Place>>();
            //we will load it asynchronously from server in this method
            loadPlaces();
        }
        //finally we will return the list
        return placesList;
    }

    //This method is using Retrofit to get the JSON data from URL
    private void loadPlaces() {
        isLoading.setValue(true);
        error.setValue(false);

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<List<Place>> call = service.getAllPlaces();
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {

                // if there is no data set error and end
                if(response.body() == null){
                    error.setValue(true);
                    isLoading.setValue(false);
                    return;
                }
                // store places data to a list
                placesList.setValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t);
                isLoading.setValue(false);
                error.setValue(true);

            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getError() {
        return error;
    }
}



