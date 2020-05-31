package com.udacity.popularmovies.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.udacity.popularmovies.data.local.MoviesLocalDataSource;
import com.udacity.popularmovies.data.local.model.Movie;
import com.udacity.popularmovies.data.local.model.MovieDetails;
import com.udacity.popularmovies.data.local.model.RepoMoviesResult;
import com.udacity.popularmovies.data.local.model.Resource;
import com.udacity.popularmovies.data.remote.MoviesRemoteDataSource;
import com.udacity.popularmovies.data.remote.api.ApiResponse;
import com.udacity.popularmovies.ui.movieslist.MoviesFilterType;
import com.udacity.popularmovies.utils.AppExecutors;

import java.util.List;

import timber.log.Timber;

/**
 * Repository implementation that returns a paginated data and loads data directly from network.
 */
public class MovieRepository implements DataSource {

    private static volatile MovieRepository sInstance;

    private final MoviesLocalDataSource mLocalDataSource;

    private final MoviesRemoteDataSource mRemoteDataSource;

    private final AppExecutors mExecutors;

    private MovieRepository(MoviesLocalDataSource localDataSource,
                            MoviesRemoteDataSource remoteDataSource,
                            AppExecutors executors) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mExecutors = executors;
    }

    public static MovieRepository getInstance(MoviesLocalDataSource localDataSource,
                                              MoviesRemoteDataSource remoteDataSource,
                                              AppExecutors executors) {
        if (sInstance == null) {
            synchronized (MovieRepository.class) {
                if (sInstance == null) {
                    sInstance = new MovieRepository(localDataSource, remoteDataSource, executors);
                }
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<Resource<MovieDetails>> loadMovie(final long movieId) {
        return new NetworkBoundResource<MovieDetails, Movie>(mExecutors) {

            @Override
            protected void saveCallResult(@NonNull Movie item) {
                mLocalDataSource.saveMovie(item);
                Timber.d("Movie added to database");
            }

            @Override
            protected boolean shouldFetch(@Nullable MovieDetails data) {
                return data == null; // only fetch fresh data if it doesn't exist in database
            }

            @NonNull
            @Override
            protected LiveData<MovieDetails> loadFromDb() {
                Timber.d("Loading movie from database");
                return mLocalDataSource.getMovie(movieId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Movie>> createCall() {
                Timber.d("Downloading movie from network");
                return mRemoteDataSource.loadMovie(movieId);
            }

            @NonNull
            @Override
            protected void onFetchFailed() {
                // ignored
                Timber.d("Fetch failed!!");
            }
        }.getAsLiveData();
    }

    @Override
    public RepoMoviesResult loadMoviesFilteredBy(MoviesFilterType sortBy) {
        return mRemoteDataSource.loadMoviesFilteredBy(sortBy);
    }

    @Override
    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return mLocalDataSource.getAllFavoriteMovies();
    }

    @Override
    public void favoriteMovie(final Movie movie) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Adding movie to favorites");
                mLocalDataSource.favoriteMovie(movie);
            }
        });
    }

    @Override
    public void unfavoriteMovie(final Movie movie) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Removing movie from favorites");
                mLocalDataSource.unfavoriteMovie(movie);
            }
        });
    }
}
