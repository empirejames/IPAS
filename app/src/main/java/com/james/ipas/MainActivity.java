package com.james.ipas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class MainActivity extends Activity {

    TextView next_grade, grade, tv_spendTime, tv_spend;
    ImageView img_ipas;
    Button btn_enter;
    Spinner spinner, spiner_subject;
    String getResult, spendTime;
    MaterialBetterSpinner subjectSpinner, countSpiner;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.ipas);
        initView();

        //new GetRemoteConfig().execute();
        tinyDB = new TinyDB(this);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            getResult = b.getString("result");
            spendTime = b.getString("spendTimes");
            next_grade.setVisibility(View.VISIBLE);
            grade.setVisibility(View.VISIBLE);
            grade.setText(getResult);
            tv_spendTime.setVisibility(View.VISIBLE);
            tv_spendTime.setText(spendTime);
            tv_spend.setVisibility(View.VISIBLE);
        }

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_question = countSpiner.getText().toString();
                String txt_subject = subjectSpinner.getText().toString();
                if (txt_subject.equals("行動裝置概論")) {
                    txt_subject = "mobile_intruduction";
                } else if (txt_subject.equals("Android程式設計")) {
                    txt_subject = "mobile_android";
                } else if (txt_subject.equals("iOS程式設計")) {
                    txt_subject = "mobile_ios";
                }

                if (txt_subject.equals("") || txt_question.equals("")) {
                    //Toast.makeText(MainActivity.this, "請選擇科目題數 ", Toast.LENGTH_LONG).show();
                    View view = findViewById(R.id.next_grade);
                    Snackbar.make(view,
                            "忘記選擇題庫 & 題數了喔～",
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Intent i = new Intent(MainActivity.this, QuestionActivity.class);
                    Bundle b = new Bundle();
                    b.putString("subject", txt_subject);
                    b.putString("question", txt_question);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });
    }

    public void initView() {
        final String[] questions = {"10", "20", "30", "40", "50"};
        final String[] subject = {"行動裝置概論", "Android程式設計", "iOS程式設計"};
        img_ipas = (ImageView) findViewById(R.id.img_ipas);

        next_grade = findViewById(R.id.next_grade);
        subjectSpinner = (MaterialBetterSpinner) findViewById(R.id.spinner_subject);
        countSpiner = (MaterialBetterSpinner) findViewById(R.id.spinner);
        tv_spendTime = findViewById(R.id.tv_spendTime);
        tv_spend = findViewById(R.id.tv_times);
        grade = findViewById(R.id.grade);
        btn_enter = findViewById(R.id.btn_enter);
        //spinner = (Spinner) findViewById(R.id.spinner);
        //spiner_subject = (Spinner) findViewById(R.id.spinner_subject);
        ArrayAdapter<String> subject_List = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, subject);
        ArrayAdapter<String> quesion_List = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, questions);
        countSpiner.setAdapter(quesion_List);
        subjectSpinner.setAdapter(subject_List);

    }

    public void getRemotePara() {
        final FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpiration = 3600;
        if (mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mRemoteConfig.activateFetched();

                            String introduction = mRemoteConfig.getString("mobile_introduction_num");
                            tinyDB.putString("mobile_introduction_num", introduction);

                            String android = mRemoteConfig.getString("mobile_android_num");
                            tinyDB.putString("mobile_android_num", android);

                            String ios = mRemoteConfig.getString("mobile_ios_num");
                            tinyDB.putString("mobile_android_num", ios);

                            String result = mRemoteConfig.getString("default");
                            tinyDB.putString("default", result);

                            Log.e("GGG", introduction);
                            Log.e("GGG", android);
                            Log.e("GGG", ios);
                            Log.e("GGG", result);

                        } else {
                            Log.e("GGG", "Un success");
                        }
                    }
                });
    }

    class GetRemoteConfig extends AsyncTask<int[], Integer, String> {
        @Override
        protected String doInBackground(int[]... ints) {
            getRemotePara();
            return "finish";
        }

        @Override
        protected void onPreExecute() {

        }
    }

}



