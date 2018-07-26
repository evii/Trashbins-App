package cz.optimization.odpadky.data;

import java.util.List;

import cz.optimization.odpadky.TrashbinClusterItem;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("places")
    Call<List<TrashbinClusterItem>> getAllPlaces();
}