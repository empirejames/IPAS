package com.james.ipas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class MainActivity extends Activity {

    TextView next_grade, grade, tv_spendTime, tv_spend ;
    ImageView img_ipas;
    Button btn_enter;
    Spinner spinner, spiner_subject;
    String getResult, spendTime;
    MaterialBetterSpinner subjectSpinner, countSpiner;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.ipas);
        initView();
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
                Intent i = new Intent(MainActivity.this, QuestionActivity.class);
                Bundle b = new Bundle();
                b.putString("subject", txt_subject);
                b.putString("question", txt_question);
                i.putExtras(b);
                startActivity(i);
            }
        });


        ArrayList<String> passingData = new ArrayList<String>();

    }

    public void initView() {
        final String[] questions = {"10", "20", "30", "40", "50"};
        final String[] subject = {"行動裝置概論"};
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


}



