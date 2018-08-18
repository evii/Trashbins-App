package cz.optimization.odpadky.retrofit_data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import cz.optimization.odpadky.objects.Container;

public class FetchContainersTypeViewModel extends ViewModel {

    private MutableLiveData<List<Container>> containerCoordinatesList;
    private String mTrashType;

    public FetchContainersTypeViewModel (String trashType) {
        mTrashType = trashType;
    }

    public LiveData<List<Container>> FetchContainersType(String trashType) {
        if (containerCoordinatesList == null) {
            containerCoordinatesList = new MutableLiveData<List<Container>>();
            fetchContainersType(trashType);
        }
        return containerCoordinatesList;
    }

    private void fetchContainersType(String trashType) {
        // Do an asynchronous operation to fetch users.
    }


}
