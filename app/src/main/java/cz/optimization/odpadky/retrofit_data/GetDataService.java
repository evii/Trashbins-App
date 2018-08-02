package cz.optimization.odpadky.retrofit_data;

import java.util.List;

import cz.optimization.odpadky.objects.Container;
import cz.optimization.odpadky.objects.Place;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("places")
    Call<List<Place>> getAllPlaces();

    @GET("places/{place_id}")
    Call<Container.ContainersResult> getContainersList(@Path("place_id") String placeId);

}