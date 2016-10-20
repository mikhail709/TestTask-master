package com.test.testtask.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.test.testtask.R;
import com.test.testtask.data.DataManager;
import com.test.testtask.data.entity.User;
import com.test.testtask.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {

    private static final int KEY_EMAIL = 512;
    private static final int KEY_BROWSER = 513;
    private static final int KEY_PHONE = 514;
    private static final int KEY_CITY = 515;

    private static final String ARG_PARAM_POST_ID = "id_post";
    private static final String ARG_PARAM_USER_ID = "id_user";

    private Context context;
    private int postId, userId;
    private TextView tvPostId, tvName, tvNikname, tvEmail, tvSite, tvPhone, tvCity, tvError;
    private Button btnSaveInDB;
    private ProgressBar pb;
    private LinearLayout layoutDetails;
    private User curUser;

    private Handler handler;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(int postId, int userID) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_POST_ID, postId);
        args.putInt(ARG_PARAM_USER_ID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.postId = getArguments().getInt(ARG_PARAM_POST_ID);
            this.userId = getArguments().getInt(ARG_PARAM_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        context = getActivity();

        findViews(view);
        tvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorTextViewPressed(v);
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(KEY_EMAIL, v);
            }
        });
        tvSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(KEY_BROWSER, v);
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(KEY_PHONE, v);
            }
        });
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(KEY_CITY, v);
            }
        });
        btnSaveInDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnSavePressed(v);
            }
        });

        handler = new Handler() {
            public void handleMessage(Message msg) {
                btnSaveInDB.setEnabled(true);
                Toast.makeText(context, context.getResources().getString(msg.what), Toast.LENGTH_SHORT).show();
            }
        };

        loadUserInfo(userId);

        return view;
    }

    private void onBtnSavePressed(View v){
        v.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dm = DataManager.getInstance();
                int msg = R.string.handler_error_msg;
                if (dm.findUserByID(curUser.getId())){
                    int cnt = dm.updateUser(curUser);
                    if (cnt > 0) msg = R.string.handler_upd_success_msg;
                } else {
                    long id = dm.saveUser(curUser);
                    if (id != -1) msg = R.string.handler_save_success_msg;
                }
                handler.sendEmptyMessage(msg);
            }
        }).start();
    }

    private void findViews(View view){
        this.tvPostId = (TextView) view.findViewById(R.id.tvPostIdDetailsFrag);
        this.tvName = (TextView) view.findViewById(R.id.tvName);
        this.tvNikname = (TextView) view.findViewById(R.id.tvNikname);
        this.tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        this.tvSite = (TextView) view.findViewById(R.id.tvSite);
        this.tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        this.tvCity = (TextView) view.findViewById(R.id.tvCity);
        this.tvError = (TextView) view.findViewById(R.id.tvErrorDetailsFrag);
        this.btnSaveInDB = (Button) view.findViewById(R.id.btnSaveInDB);
        this.pb = (ProgressBar) view.findViewById(R.id.pbDetailsFrag);
        this.layoutDetails = (LinearLayout) view.findViewById(R.id.layoutDetails);
    }

    private void loadUserInfo(int userId){
        Call<User> call = DataManager.getInstance().getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == Constants.RESPONSE_CODE_OK){
                    callSuccess(response);
                } else {
                    callFailed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callFailed();
            }
        });
    }

    private void callSuccess(Response<User> response){
        this.curUser = response.body();

        this.tvPostId.setText(String.valueOf(postId));
        this.tvNikname.setText(curUser.getUsername());
        this.tvName.setText(curUser.getName());

        Spannable spannable;
        spannable= new SpannableString(curUser.getEmail());
        spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.tvEmail.setText(spannable);

        spannable= new SpannableString(curUser.getWebsite());
        spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.tvSite.setText(spannable);

        spannable= new SpannableString(curUser.getPhone());
        spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.tvPhone.setText(spannable);

        spannable= new SpannableString(curUser.getAddress().getCity());
        spannable.setSpan(new UnderlineSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.tvCity.setText(spannable);
        tvCity.setTag(curUser.getAddress().getGeo());

        this.tvError.setVisibility(View.INVISIBLE);
        this.pb.setVisibility(View.INVISIBLE);
        this.layoutDetails.setVisibility(View.VISIBLE);
        this.btnSaveInDB.setVisibility(View.VISIBLE);
    }

    private void callFailed(){
        pb.setVisibility(View.INVISIBLE);
        tvError.setText(Constants.ERROR_MSG_WRONG);
        tvError.setVisibility(View.VISIBLE);
    };

    private void onErrorTextViewPressed(View v){
        YoYo.with(Techniques.Pulse)
                .duration(Constants.ANIMATION_DURATION_SHORT)
                .playOn(v);
        tvError.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        loadUserInfo(userId);
    }

    private void sendIntent(int key, View v){
        Intent intent;
        switch (key){
            case KEY_EMAIL:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, ((TextView)v).getText().toString());
                startActivity(Intent.createChooser(intent, Constants.SEND_EMAIL));
                break;
            case KEY_BROWSER:
                Uri adress = Uri.parse("http://" + ((TextView)v).getText().toString());
                intent = new Intent(Intent.ACTION_VIEW, adress);
                startActivity(Intent.createChooser(intent, Constants.OPEN_WEBSITE));
                break;
            case KEY_PHONE:
                String number = ((TextView)v).getText().toString();
                Uri call = Uri.parse("tel:" + number);
                intent = new Intent(Intent.ACTION_CALL, call);
                startActivity(Intent.createChooser(intent, Constants.CALL + number));
                break;
            case KEY_CITY:
                User.Geo geo = (User.Geo) v.getTag();
                Uri gmmIntentUri = Uri.parse("geo:" + geo.getLat() + "," + geo.getLng());
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(Intent.createChooser(intent, Constants.OPEN_MAP));
                break;

        }
    }

}
