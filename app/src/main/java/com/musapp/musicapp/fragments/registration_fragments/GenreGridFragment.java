package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.GenreRecyclerViewAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.Genre;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.AppPreferences;
import com.musapp.musicapp.utils.UIUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenreGridFragment extends Fragment {

    private GenreRecyclerViewAdapter genreRecyclerAdapter;
    private Button nextButton;
    private User user = CurrentUser.getCurrentUser();
    private List<Genre> listOfGenres = new ArrayList<Genre>(
            Arrays.asList(new Genre("Rock", R.drawable.rock_genre)
                    ,new Genre("Blues", R.drawable.blues_genre)
                    ,new Genre("Classic", R.drawable.classic1_genre)
                    ,new Genre("Classic Metal", R.drawable.classic_metal_genre)
                    ,new Genre("Power Metal", R.drawable.power_metal_genre)
                    ,new Genre("Symphonic Metal", R.drawable.simphonic_metal_genre)
                    ,new Genre("Electronic dance", R.drawable.elctronic_dance_genre)
                    ,new Genre("Jazz", R.drawable.jazz_genre)
                    ,new Genre("Pop", R.drawable.pop_genre)
                    ,new Genre("Folk", R.drawable.folk_genre)
                    ,new Genre("Hip Hop", R.drawable.hip_hop_genre)
                    ,new Genre("Country", R.drawable.country_genre)
                    ,new Genre("Rhythm & Blues", R.drawable.rhythm_blues_genre)
                    ,new Genre("Heavy metal", R.drawable.heavy_metal_genre)
                    ,new Genre("Reggae", R.drawable.reggae_genre)
                    ,new Genre("Punk Rock", R.drawable.punk_rock_genre)
                    ,new Genre("Funk", R.drawable.funk_genre)
                    ,new Genre("Soul", R.drawable.soul_genre)
                    ,new Genre("Alternative Rock", R.drawable.alternative_rock_genre)
                    ,new Genre("Dance music", R.drawable.dance_genre)
                    ,new Genre("Techno", R.drawable.techno_genre)
                    ,new Genre("Rap", R.drawable.rap_genre)
                    ,new Genre("House", R.drawable.house_genre)
                    ,new Genre("Singing/Vocal", R.drawable.vocal_genre)
                    ,new Genre("Ambient", R.drawable.ambient_genre)
                    ,new Genre("Instrumental", R.drawable.classic1_genre)
                    ,new Genre("World music", R.drawable.world_genre)
                    ,new Genre("Trance music", R.drawable.trance_genre)
                    ,new Genre("Progressive Rock", R.drawable.power_metal_genre)
                    ,new Genre("Latin Music", R.drawable.latin_genre)
                    ,new Genre("Indie Rock", R.drawable.indie_genre)
                    ,new Genre("Pop Rock", R.drawable.pop_genre)
                    ,new Genre("Hard Rock", R.drawable.hard_rock_genre)
                    ,new Genre("Psychedelic music", R.drawable.psychedelic_genre)
                    ,new Genre("Grunge", R.drawable.grunge_genre)
                    ,new Genre("Electro", R.drawable.elctronic_dance_genre)
                    ,new Genre("New Wave", R.drawable.new_wave_genre)
                    ,new Genre("Death metal", R.drawable.death_metal_genre)
                    ,new Genre("Noise", R.drawable.noise_genre)
                    ,new Genre("Industrial Rock", R.drawable.industrial_genre)
                    ,new Genre("Acoustic", R.drawable.country_genre)
                    ,new Genre("Jazz Fusion", R.drawable.jazz_genre)
                    ,new Genre("African music", R.drawable.african_genre))
    );

    private GenreRecyclerViewAdapter.OnItemSelectedListener mOnItemSelectedListener = new GenreRecyclerViewAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(Genre genre, View view) {
            //TODO Change background of textView and remember checked genre

            View temp = view.findViewById(R.id.text_genre_grid_fragment_genre_name);
            boolean flag = genre.isChecked();
            if (!flag){
                temp.setBackgroundColor(getResources().getColor(R.color.colorGenreChecked));
                Toast.makeText(getContext(),genre.getName() + " :checked " , Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(getContext(),genre.getName() + " :Unchecked ", Toast.LENGTH_SHORT).show();
                temp.setBackgroundColor(getResources().getColor(R.color.colorWhiteTransparent));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_grid_view_genre_fragment, container, false);

        RecyclerView view = rootView.findViewById(R.id.genre_recycler_view_genre_fragment);

        initRecyclerAdapter(view);
    //    nextButton = rootView.findViewById(R.id.action_fragment_grid_and_profession_next);
        //TODO very bad solution
        nextButton = UIUtils.getButtonFromView(getActivity().findViewById(R.id.layout_registration_genre_info), R.id.action_fragment_grid_and_profession_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationTransactionWrapper.registerForNextFragment((int)nextButton.getTag());
                submitInformation();
                CurrentUser.setCurrentUser(user);
            }
        });
        return rootView;
    }

    private void initRecyclerAdapter(RecyclerView view){
        genreRecyclerAdapter = new GenreRecyclerViewAdapter();
        genreRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        genreRecyclerAdapter.setData(listOfGenres);
        view.setHasFixedSize(false);
        //view.setLayoutManager(new GridLayoutManager(getContext(), 2));
        StaggeredGridLayoutManager man = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        man.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        view.setLayoutManager(man);
        view.setAdapter(genreRecyclerAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_grid_view_3);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String json = AppPreferences.getGenreState(getActivity().getBaseContext());
        if(json != null){
            Gson gson = new Gson();
            //String json = AppPreferences.getGenreState(getActivity().getBaseContext());
            Type type = new TypeToken<List<Genre>>() {}.getType();
            List<Genre> list = new ArrayList<>();
            list = gson.fromJson(json, type);

            listOfGenres = list;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        String json = gson.toJson(genreRecyclerAdapter.getData());

        AppPreferences.saveGenreState(getActivity().getBaseContext(), json);
    }

    public GenreGridFragment() {
    }

    private void submitInformation(){
        List<String> genreId = new ArrayList<>();
        for(Genre genre: listOfGenres){
            if(genre.isChecked()){
                genreId.add(genre.getName());
            }
        }
        user.setGenresId(genreId);

    }

}

