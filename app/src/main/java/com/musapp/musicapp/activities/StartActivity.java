package com.musapp.musicapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.fragments.registration_fragments.GenreGridFragment;
import com.musapp.musicapp.fragments.registration_fragments.ProfessionAndBioFragment;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment1;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment2;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment5;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegisterFragmentTransaction;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.fragments.sign_in_fragments.SignInFragment;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.preferences.RememberPreferences;


public class StartActivity extends AppCompatActivity {

    private RegistrationFragment1 register1;
    private RegistrationFragment2 register2;
    private RegistrationFragment5 register5;
    private GenreGridFragment register3;
    private ProfessionAndBioFragment register4;
    private SignInFragment signInFragment;


    private RegisterFragmentTransaction registerFragmentTransaction = new RegisterFragmentTransaction() {
        @Override
        public void getNextFragment(int id) {
            if (id == R.integer.registration_fragment_1) {
                beginTransaction(register2);
            }
            if (id == R.integer.registration_fragment_2) {
                beginTransaction(register3);
            }
            if (id == R.integer.registration_fragment_grid_view_3) {
                beginTransaction(register4);
            }
            if (id == R.integer.registration_fragment_professions_4) {
                beginTransaction(register5);
            }
            if (id == R.integer.registration_fragment_5) {
                beginTransaction(signInFragment);
            }
            if (id == R.integer.sign_in_fragment_main_page) {
                startMainPageActivity();
            }
            if (id == R.integer.sign_in_fragment_registration) {
                beginTransaction(register1);
            }
            if (id == R.integer.sign_in_fragment_forgot_pass) {
                //TODO beginTransaction(forgotPasswordFragment)
            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        init();
        setRememberSharedPreferenceState();
        RegistrationTransactionWrapper.setRegisterFragmentTransaction(registerFragmentTransaction);
        start();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // TODO handle logo visibility
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getFragments().size() == 0) {
                    return;
                }
                Fragment currentFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 1);

                if (currentFragment instanceof RegistrationFragment2) {
                    handleLogoVisibility(View.VISIBLE);
                    handleLayoutVisibility(View.VISIBLE);
                }
                else if(currentFragment instanceof GenreGridFragment || currentFragment instanceof ProfessionAndBioFragment){
                    handleLogoVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", StartActivity.class.getName());
        editor.apply();
    }

    private void init() {
        register1 = new RegistrationFragment1();
        register2 = new RegistrationFragment2();
        register3 = new GenreGridFragment();
        register4 = new ProfessionAndBioFragment();
        register5 = new RegistrationFragment5();
        signInFragment = new SignInFragment();
    }

    private void beginTransaction(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //  Fragment previousFragment = fragmentManager.findFragmentById(R.id.container_grid_and_profession_fragment);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if (fragment instanceof GenreGridFragment || fragment instanceof ProfessionAndBioFragment) {

            handleLayoutVisibility(View.GONE);

            //TODO clear framelayout from fragment and handle backpressing
            //findViewById(R.id.layout_activity_start_content_main).setVisibility(View.GONE);
            clearLayoutOne();
            transaction.replace(R.id.layout_registration_genre_info, fragment);

        }else{
            //handleLogoVisibility(View.VISIBLE);
            //handleLogoVisibility(View.GONE);
            //handleLayoutVisibility(View.VISIBLE);
//            setLogoVisibility(View.VISIBLE);
//            findViewById(R.id.layout_activity_start_content_main).findViewById(R.id.action_fragment_grid_and_profession_next).setVisibility(View.INVISIBLE);
//
//
//            if(previousFragment instanceof RegistrationFragment2){
//                transaction.remove(previousFragment);
//                transaction.commit();}
//
//             findViewById(R.id.container_grid_and_profession_fragment).setVisibility(View.GONE);
//            findViewById(R.id.layout_activity_start_content_main).setVisibility(View.VISIBLE);
            handleLayoutVisibility(View.VISIBLE);
            handleLogoVisibility(View.VISIBLE);
            transaction.replace(R.id.layout_activity_start_content_main, fragment);


        }
        transaction.addToBackStack(null);
        transaction.commit();

    }
    private void clearLayoutOne(){
        if(findViewById(R.id.layout_activity_start_content_main) != null){
            FrameLayout layout = findViewById(R.id.layout_activity_start_content_main);
            layout.removeAllViews();
        }
    }

    //for animation fragment visibility
    private void slideContainerToLeft(boolean hide) {
        FrameLayout view = findViewById(R.id.layout_activity_start_content_main);
        TranslateAnimation animate = new TranslateAnimation(0, -view.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void start() {
        DBAccess.getFirebaseAuth().signOut();
        CurrentUser.setCurrentUser(new User());

         //   beginTransaction(register1);}
        if (RememberPreferences.getState(this)) {
            RememberPreferences.saveUser(this, "none");
            beginTransaction(signInFragment);
        } else
            startMainPageActivity();
    }

    private void startMainPageActivity() {
        Intent intent = new Intent(this, AppMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.layout_activity_start_content_main);

        if (f instanceof RegistrationFragment1 || f instanceof SignInFragment) {
            finish();
        }else if(f instanceof RegistrationFragment2){
            handleLayoutVisibility(View.VISIBLE);
            beginTransaction(register2);
            super.onBackPressed();
        }
    }


    private void handleLogoVisibility(int visibility){
        findViewById(R.id.layout_start_activity_logo).setVisibility(visibility);
        //findViewById(R.id.action_fragment_grid_and_profession_next).setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
        //
        //findViewById(R.id.action_fragment_grid_and_profession_next).setVisibility(visibility == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);

    }
    private void handleLayoutVisibility(int visibility){
        if(visibility == View.GONE){

            findViewById(R.id.layout_1).setVisibility(View.GONE);
            findViewById(R.id.layout_2).setVisibility(View.VISIBLE);
            findViewById(R.id.action_fragment_grid_and_profession_next).setVisibility( View.VISIBLE);

        }else{
            findViewById(R.id.layout_1).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_2).setVisibility(View.GONE);
        }

    }

    private void setRememberSharedPreferenceState(){
        Intent intent = getIntent();
        if(intent != null &&  intent.hasExtra(getString(R.string.quit))){
            RememberPreferences.saveState(this, intent.getBooleanExtra(getString(R.string.quit), RememberPreferences.getState(this)));
        }
    }
}
