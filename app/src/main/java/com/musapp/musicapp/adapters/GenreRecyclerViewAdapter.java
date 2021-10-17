package com.musapp.musicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.GenreViewHolder;
import com.musapp.musicapp.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreRecyclerViewAdapter extends RecyclerView.Adapter<GenreViewHolder> {

    private List<Genre> mData;
    private OnItemSelectedListener mOnItemSelectedListener;
    private Context context;

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genre_item_grid_view, viewGroup, false);
        final GenreViewHolder viewHolder = new GenreViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemSelectedListener != null){
                    mOnItemSelectedListener.onItemSelected(mData.get(viewHolder.getAdapterPosition()), v);
                    boolean checked = mData.get(viewHolder.getAdapterPosition()).isChecked();
                    if(checked){
                        mData.get(viewHolder.getAdapterPosition()).setChecked(false);
                    }else{
                        mData.get(viewHolder.getAdapterPosition()).setChecked(true);
                    }
                }
            }
        });
        context = viewGroup.getContext();
        if (!mData.get(i).isChecked()){
            viewHolder.setNameBackground(context.getResources().getColor(R.color.colorWhiteTransparent));
        }else{
            viewHolder.setNameBackground(context.getResources().getColor(R.color.colorGenreChecked));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder genreViewHolder, int i) {
        Genre genre = mData.get(i);
        genreViewHolder.setImage(genre.getImageResource());
        genreViewHolder.setName(genre.getName());
        if (!mData.get(i).isChecked()){
            genreViewHolder.setNameBackground(context.getResources().getColor(R.color.colorWhiteTransparent));
        }else{
            genreViewHolder.setNameBackground(context.getResources().getColor(R.color.colorGenreChecked));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Genre> list){
        if(mData == null){
            mData = new ArrayList<>(list);
        }else{
            mData.clear();
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<Genre> getData(){
        return mData;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(Genre genre, View view);
    }

}

