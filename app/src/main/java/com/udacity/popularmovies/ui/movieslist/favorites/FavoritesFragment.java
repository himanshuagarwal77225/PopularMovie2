package com.udacity.popularmovies.ui.movieslist.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.local.model.Movie;
import com.udacity.popularmovies.databinding.FragmentFavoriteMoviesBinding;
import com.udacity.popularmovies.ui.movieslist.MoviesActivity;
import com.udacity.popularmovies.utils.Injection;
import com.udacity.popularmovies.utils.ItemOffsetDecoration;
import com.udacity.popularmovies.utils.ViewModelFactory;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel viewModel;
    private FragmentFavoriteMoviesBinding binding;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MoviesActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.favorites));
        viewModel = obtainViewModel(getActivity());
        setupListAdapter();
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = binding.moviesList.rvMovieList;
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter();
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        // setup recyclerView
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        // observe favorites list
        viewModel.getFavoriteListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movieList) {
                if (movieList.isEmpty()) {
                    // TODO: 11/7/2018 optimize this
                    // display empty state since there is no favorites in database
                    binding.moviesList.rvMovieList.setVisibility(View.GONE);
                    binding.emptyState.setVisibility(View.VISIBLE);
                } else {
                    binding.moviesList.rvMovieList.setVisibility(View.VISIBLE);
                    binding.emptyState.setVisibility(View.GONE);
                    favoritesAdapter.submitList(movieList);
                }
            }
        });
    }

    private FavoritesViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = Injection.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(FavoritesViewModel.class);
    }
}
