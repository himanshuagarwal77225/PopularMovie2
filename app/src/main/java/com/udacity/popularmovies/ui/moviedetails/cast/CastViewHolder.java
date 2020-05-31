package com.udacity.popularmovies.ui.moviedetails.cast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.local.model.Cast;
import com.udacity.popularmovies.databinding.ItemCastBinding;
import com.udacity.popularmovies.utils.Constants;


public class CastViewHolder extends RecyclerView.ViewHolder {

    private ItemCastBinding binding;

    private Context context;

    public CastViewHolder(@NonNull ItemCastBinding binding, Context context) {
        super(binding.getRoot());

        this.binding = binding;
        this.context = context;
    }

    public static CastViewHolder create(ViewGroup parent) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemCastBinding binding =
                ItemCastBinding.inflate(layoutInflater, parent, false);
        return new CastViewHolder(binding, parent.getContext());
    }

    public void bindTo(final Cast cast) {
        String profileImage =
                Constants.IMAGE_BASE_URL + Constants.PROFILE_SIZE_W185 + cast.getProfileImagePath();
        Glide.with(context)
                .load(profileImage)
                .placeholder(R.color.md_grey_200)
                .dontAnimate()
                .into(binding.imageCastProfile);

        binding.textCastName.setText(cast.getActorName());

        binding.executePendingBindings();
    }
}
