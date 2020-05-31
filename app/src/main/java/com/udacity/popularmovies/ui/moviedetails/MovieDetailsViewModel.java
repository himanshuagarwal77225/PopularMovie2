package com.udacity.popularmovies.ui.moviedetails;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.MovieRepository;
import com.udacity.popularmovies.data.local.model.MovieDetails;
import com.udacity.popularmovies.data.local.model.Resource;
import com.udacity.popularmovies.utils.SnackbarMessage;

import timber.log.Timber;


public class MovieDetailsViewModel extends ViewModel {

    private final MovieRepository repository;
    private final SnackbarMessage mSnackbarText = new SnackbarMessage();
    private LiveData<Resource<MovieDetails>> result;
    private MutableLiveData<Long> movieIdLiveData = new MutableLiveData<>();
    private boolean isFavorite;

    public MovieDetailsViewModel(final MovieRepository repository) {
        this.repository = repository;
    }

    public void init(long movieId) {
        if (result != null) {
            return; // load movie details only once the activity created first time
        }
        Timber.d("Initializing viewModel");

        result = Transformations.switchMap(movieIdLiveData,
                new Function<Long, LiveData<Resource<MovieDetails>>>() {
                    @Override
                    public LiveData<Resource<MovieDetails>> apply(Long movieId) {
                        return repository.loadMovie(movieId);
                    }
                });

        setMovieIdLiveData(movieId); // trigger loading movie
    }

    public LiveData<Resource<MovieDetails>> getResult() {
        return result;
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private void setMovieIdLiveData(long movieId) {
        movieIdLiveData.setValue(movieId);
    }

    public void retry(long movieId) {
        setMovieIdLiveData(movieId);
    }

    public void onFavoriteClicked() {
        MovieDetails movieDetails = result.getValue().data;
        if (!isFavorite) {
            repository.favoriteMovie(movieDetails.getMovie());
            isFavorite = true;
            showSnackbarMessage(R.string.movie_added_successfully);
        } else {
            repository.unfavoriteMovie(movieDetails.getMovie());
            isFavorite = false;
            showSnackbarMessage(R.string.movie_removed_successfully);
        }
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(message);
    }
}
