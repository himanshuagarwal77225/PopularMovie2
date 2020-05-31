package com.udacity.popularmovies.data;

import androidx.lifecycle.LiveData;

import com.udacity.popularmovies.data.local.model.Movie;
import com.udacity.popularmovies.data.local.model.MovieDetails;
import com.udacity.popularmovies.data.local.model.RepoMoviesResult;
import com.udacity.popularmovies.data.local.model.Resource;
import com.udacity.popularmovies.ui.movieslist.MoviesFilterType;

import java.util.List;

public interface DataSource {

    LiveData<Resource<MovieDetails>> loadMovie(long movieId);

    RepoMoviesResult loadMoviesFilteredBy(MoviesFilterType sortBy);

    LiveData<List<Movie>> getAllFavoriteMovies();

    void favoriteMovie(Movie movie);

    void unfavoriteMovie(Movie movie);
}
