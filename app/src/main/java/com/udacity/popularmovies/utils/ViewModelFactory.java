package com.udacity.popularmovies.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.popularmovies.data.MovieRepository;
import com.udacity.popularmovies.ui.moviedetails.MovieDetailsViewModel;
import com.udacity.popularmovies.ui.movieslist.discover.DiscoverMoviesViewModel;
import com.udacity.popularmovies.ui.movieslist.favorites.FavoritesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MovieRepository repository;

    private ViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    public static ViewModelFactory getInstance(MovieRepository repository) {
        return new ViewModelFactory(repository);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DiscoverMoviesViewModel.class)) {
            //noinspection unchecked
            return (T) new DiscoverMoviesViewModel(repository);
        } else if (modelClass.isAssignableFrom(FavoritesViewModel.class)) {
            //noinspection unchecked
            return (T) new FavoritesViewModel(repository);
        } else if (modelClass.isAssignableFrom(MovieDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new MovieDetailsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
