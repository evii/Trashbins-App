package cz.optimization.odpadky.retrofit_data;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import cz.optimization.odpadky.ui.clusters.CustomClusterRenderer;
import cz.optimization.odpadky.ui.clusters.TrashbinClusterItem;

public class FetchContainersAtPlaceViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private String mPlaceId;
    private CustomClusterRenderer mRenderer;
    private TrashbinClusterItem mTrashbinClusterItem;


    public FetchContainersAtPlaceViewModelFactory(Application application, String placeId, CustomClusterRenderer renderer, TrashbinClusterItem trashbinClusterItem) {
        mApplication = application;
        mPlaceId = placeId;
        mRenderer = renderer;
        mTrashbinClusterItem = trashbinClusterItem;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FetchContainersAtPlaceViewModel(mApplication, mPlaceId, mRenderer, mTrashbinClusterItem);
    }

}