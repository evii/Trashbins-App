package cz.optimization.odpadky.retrofit_data;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class FetchContainersTypeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private String mTrashTypeSelected;
    private String mListPlacesString;

    public FetchContainersTypeViewModelFactory(Application application, String trashTypeSelected, String listPlacesString) {
        mApplication = application;
        mTrashTypeSelected = trashTypeSelected;
        mListPlacesString = listPlacesString;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FetchContainersTypeViewModel (mApplication, mTrashTypeSelected, mListPlacesString);
    }

}
