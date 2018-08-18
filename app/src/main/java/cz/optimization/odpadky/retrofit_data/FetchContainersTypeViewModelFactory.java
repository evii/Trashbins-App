package cz.optimization.odpadky.retrofit_data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class FetchContainersTypeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final String mTrashTypeSelected;

    public FetchContainersTypeViewModelFactory(String trashTypeSelected) {
        mTrashTypeSelected = trashTypeSelected;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FetchContainersTypeViewModel (mTrashTypeSelected);
    }

}
