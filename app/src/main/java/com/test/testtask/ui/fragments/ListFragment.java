package com.test.testtask.ui.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.test.testtask.R;
import com.test.testtask.ui.adapters.ViewPagerAdapter;
import com.test.testtask.data.DataManager;
import com.test.testtask.data.entity.Post;
import com.test.testtask.utils.Constants;
import com.test.testtask.utils.NetworkStatusChecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.markushi.ui.CircleButton;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {

    private final static Random random = new Random();
    private final int GRIDVIEW_ROW_COUNT = 2;
    private final int GRIDVIEW_COLUMN_COUNT = 3;
    private final int GRIDVIEW_CELL_COUNT = GRIDVIEW_COLUMN_COUNT * GRIDVIEW_ROW_COUNT;

    private ImageView ivLogo;
    private CircleButton btnSaveLogcat;
    private CircleIndicator indicator;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private TextView tvError;

    public ListFragment() {}

    public static ListFragment newInstance(){
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_for_logo);
        this.ivLogo = (ImageView) view.findViewById(R.id.ivLogo);
        ivLogo.startAnimation(animation);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.values()[random.nextInt(10) + 3])
                        .duration(Constants.ANIMATION_DURATION_LONG)
                        .playOn(v);
            }
        });

        this.btnSaveLogcat = (CircleButton) view.findViewById(R.id.btnSaveLogcat);
        btnSaveLogcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnSaveLogcatPressed();
            }
        });

        this.indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        this.viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        this.progressBar = (ProgressBar) view.findViewById(R.id.pbListFrag);
        this.tvError = (TextView) view.findViewById(R.id.tvErrorListFrag);
        this.tvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorTextViewPressed(v);
            }
        });

        loadPostsList();

        return view;
    }

    private void loadPostsList(){
        if (NetworkStatusChecker.isNetworkAvailable(getActivity())){
            Call<List<Post>> call = DataManager.getInstance().getAllPosts();
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if (response.code() == Constants.RESPONSE_CODE_OK){
                        callSuccess(response);
                    } else {
                        callFailed();
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    callFailed();
                }
            });
        } else {
            tvError.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void callSuccess(Response<List<Post>> response){
        List<Post> posts = response.body();

        List<GridFragment> fragments = new ArrayList<GridFragment>();

        int i = 0;
        while (i < posts.size()) {
            ArrayList<Post> postBatch = new ArrayList<>();
            do {
                postBatch.add(posts.get(i));
                i++;
            } while (i % GRIDVIEW_CELL_COUNT != 0 && i < posts.size());
            fragments.add(GridFragment.newInstance(postBatch));
        }

        ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getChildFragmentManager(),fragments);
        this.viewPager.setAdapter(viewPagerAdapter);
        this.indicator.setViewPager(this.viewPager);
        viewPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
		progressBar.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        indicator.setVisibility(View.VISIBLE);
    }

    private void callFailed(){
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(Constants.ERROR_MSG_WRONG);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void onBtnSaveLogcatPressed(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            (new LogcatSaver()).execute();
        } else {
            Toast.makeText(getActivity(), Constants.ERROR_MSG_EXT_STORAGE_UNAVAILABLE, Toast.LENGTH_LONG).show();
        }
    }

    private void onErrorTextViewPressed(View v){
        YoYo.with(Techniques.Pulse)
                .duration(Constants.ANIMATION_DURATION_SHORT)
                .playOn(v);
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadPostsList();
    }

    class LogcatSaver extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            String resultMsg;
            File directory = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                            .getAbsolutePath() + Constants.DELIMITER + getResources().getString(R.string.app_name));
            directory.mkdirs();
            File file = new File(directory, Constants.LOGCAT_FILENAME);
            try {
                if (file.exists()) file.delete();
                file.createNewFile();
                if (writeToFile(file, getStringLogCat()))
                    resultMsg = Constants.MSG_FILE_HAS_BEEN_SAVED_IN + file.getPath();
                    else resultMsg = Constants.ERROR_MSG_UNEXPECTED;
            } catch (IOException e) {
                e.printStackTrace();
                resultMsg = Constants.ERROR_MSG_CANT_CREATE_FILE;
            }
            return resultMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
        }

        private String getStringLogCat() {
            String result;
            try {
                Process process = Runtime.getRuntime().exec("logcat -d");
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                try {
                    StringBuilder logCatBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        logCatBuilder.append(line);
                        logCatBuilder.append("\n");
                    }
                    result = logCatBuilder.toString();
                } finally {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                result = Constants.ERROR_MSG_CANT_READ_LOGS;
            }
            return result;
        }

        private boolean writeToFile(File file, String what){
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                try {
                    bufferedWriter.write(what);
                } finally {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }
}
