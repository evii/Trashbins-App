package cz.optimization.odpadky.retrofit_data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.objects.Place;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchContainersTypeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Container>> containerCoordinatesList;
    private String mTrashType;
    private String mListPlacesString;
    private MutableLiveData<List<Container>> mContainersbyType;
    private List<Place> mPlaces;


    public FetchContainersTypeViewModel(@NonNull Application application, String trashType, String listPlacesString) {
        super(application);
        mTrashType = trashType;
        mListPlacesString = listPlacesString;
    }

    public MutableLiveData<List<Container>> FetchContainersType(String trashType, String listPlacesString) {
        mContainersbyType = new MutableLiveData<List<Container>>();
        fetchContainersType(trashType, listPlacesString);

        return mContainersbyType;
    }

    private void fetchContainersType(final String trashTypeSelected, String listPlacesString) {

        GetDataService service = APIClient.getClient().create(GetDataService.class);
        final List<Place> mListPlaces;

        if (listPlacesString == null || listPlacesString.isEmpty()) {

            Call<List<Place>> call1 = service.getAllPlaces();
            call1.enqueue(new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                    mPlaces = response.body();


                    Gson gson = new Gson();
                    mListPlacesString = gson.toJson(mPlaces);
                }

                @Override
                public void onFailure(Call<List<Place>> call, Throwable t) {
                    Toast.makeText(getApplication(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Place>>() {
            }.getType();
            mPlaces = gson.fromJson(listPlacesString, type);

        }

        Call<List<Container>> call2 = service.getContainersTypes();
        call2.enqueue(new Callback<List<Container>>() {
            @Override
            public void onResponse(Call<List<Container>> call, Response<List<Container>> response) {

                List<Container> allContainersList = response.body();
                /*Gson gson = new Gson();
                mAllContainersListString = gson.toJson(allContainersList);
*/
                //covert list of places with coordinates into array map
                ArrayMap<String, Place> allPlacesMap = new ArrayMap<String, Place>();
                for (Place place : mPlaces) {
                    allPlacesMap.put(place.getPlaceId(), place);
                }

                // create new containers list with coordinates
                List<Container> containerCoordinatesList = new ArrayList<Container>();
                String trashType = "";
                for (Container container : allContainersList) {
                    trashType = container.getTrashType();

                    if (trashType.equals(trashTypeSelected)) {

                        String placeId = container.getPlaceId();
                        Place selectedPlace = allPlacesMap.get(placeId);
                        double lat = selectedPlace.getLatitude();
                        double lng = selectedPlace.getLongitude();
                        String title = selectedPlace.getTitle();


                        String underground = container.getUnderground();
                        String cleaning = container.getCleaning();
                        int progress = container.getProgress();

                        Container containerCoordinates = new Container(placeId, trashType, underground, cleaning, progress, lat, lng, title);
                        containerCoordinatesList.add(containerCoordinates);
                    }
                }
                mContainersbyType.setValue(containerCoordinatesList);
            }

            @Override
            public void onFailure(Call<List<Container>> call, Throwable t) {

                Toast.makeText(getApplication(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }


        });

    }
}
