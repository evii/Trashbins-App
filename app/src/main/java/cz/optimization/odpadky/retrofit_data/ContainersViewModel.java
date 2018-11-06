package cz.optimization.odpadky.retrofit_data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import cz.optimization.odpadky.objects.Container;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContainersViewModel extends ViewModel {
    private static final String TAG = "ContainersViewModel";

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Container>> containersList;
    // to show a progressbar while loading containers
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    // display error if loading failed
    MutableLiveData<Boolean> error = new MutableLiveData<>();

    //we will call this method to get the data
    public LiveData<List<Container>> getContainers() {
        //if the list is null
        if (containersList == null) {
            containersList = new MutableLiveData<List<Container>>();
            //we will load it asynchronously from server in this method
            loadContainers();
        }

        //finally we will return the list
        return containersList;
    }


    //This method is using Retrofit to get the JSON data from URL
    private void loadContainers() {
        isLoading.setValue(true);
        error.setValue(false);

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        Call<List<Container>> call2 = service.getContainersTypes();
        call2.enqueue(new Callback<List<Container>>() {
            @Override
            public void onResponse(Call<List<Container>> call, Response<List<Container>> response) {
                // if there is no data set error and end
                if(response.body() == null){
                    error.setValue(true);
                    isLoading.setValue(false);
                    return;
                }
                // store containers data to a list
                containersList.setValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Container>> call, Throwable t) {
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





