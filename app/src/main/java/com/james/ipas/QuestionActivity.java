package com.james.ipas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class QuestionActivity extends Activity {
    String TAG = QuestionActivity.class.getSimpleName();
    int numberOfSelect = 3;
    int max_number = 250;
    int[] randItem = new int[numberOfSelect];
    ArrayList<TestItem> testLists = new ArrayList<TestItem>();
    TextView tv_selector, tv_content;
    RadioGroup radioGroup;
    RadioButton radioButton_A, radioButton_B, radioButton_C, radioButton_D;
    Button btn_next, btn_pre;
    int page = 0;
    int getAnswer;
    int answer = 0;
    int countCorrect = 0;
    boolean showFlag;


    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_question);
        Bundle bundle = getIntent().getExtras();
        String subject = bundle.getString("subject");
        String question = bundle.getString("question");
        numberOfSelect = Integer.parseInt(question);
        initView();
        //Log.e(TAG, "subject : " + subject);
        //Log.e(TAG, "question : " + question);

        randItem = genRandNum(max_number, numberOfSelect);
        new GetData().execute(randItem);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page < numberOfSelect - 1) {
                    if (getAnswer != answer) {
                        Log.e(TAG, "page : " + page);

                        Log.e(TAG, "showAnswer : " + showFlag);
                        showFlag = showAnswer(answer);
                        if (showFlag) {
                            page++;
                            updateView(page);
                        }
                    } else {
                        page++;
                        updateView(page);
                    }
                } else {
                    gotoHomePage();
                }
            }
        });

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page < numberOfSelect && page >= 0) {
                    page--;
                    updateView(page);
                } else {
                    page = 0;
                    gotoHomePage();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                getAnswer = radioGroup.indexOfChild(radioButton);
                Log.e(TAG, "Checked : " + getAnswer);
            }
        });
    }

    public boolean showAnswer(int answer) {
        countCorrect ++;
        if (answer == 0) {
            radioButton_A.setTextColor(getResources().getColor(R.color.red));
        } else if (answer == 1) {
            radioButton_B.setTextColor(getResources().getColor(R.color.red));
        } else if (answer == 2) {
            radioButton_C.setTextColor(getResources().getColor(R.color.red));
        } else {
            radioButton_D.setTextColor(getResources().getColor(R.color.red));
        }
        return false;
    }

    public int updateView(int page) {
        radioGroup.clearCheck();
        radioButton_A.setTextColor(getResources().getColor(R.color.black));
        radioButton_B.setTextColor(getResources().getColor(R.color.black));
        radioButton_C.setTextColor(getResources().getColor(R.color.black));
        radioButton_D.setTextColor(getResources().getColor(R.color.black));
        if (testLists.size() != 0 && page >= 0) {
            tv_selector.setText(page + 1 + " .");
            tv_content.setText(testLists.get(page).getTopic());
            tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
            radioButton_A.setText(testLists.get(page).getItem_A());
            radioButton_B.setText(testLists.get(page).getItem_B());
            radioButton_C.setText(testLists.get(page).getItem_C());
            radioButton_D.setText(testLists.get(page).getItem_D());

            if (testLists.get(page).getAnswer().contains("A")) {
                answer = 0;
            } else if (testLists.get(page).getAnswer().contains("B")) {
                answer = 1;
            } else if (testLists.get(page).getAnswer().contains("C")) {
                answer = 2;
            } else if (testLists.get(page).getAnswer().contains("D")) {
                answer = 3;
            }
        }
        return answer;
    }

    public void initView() {
        tv_selector = findViewById(R.id.tv_selector);
        tv_content = findViewById(R.id.tv_content);
        radioGroup = findViewById(R.id.rgroup_answer);
        radioButton_A = findViewById(R.id.answer_a);
        radioButton_B = findViewById(R.id.answer_b);
        radioButton_C = findViewById(R.id.answer_c);
        radioButton_D = findViewById(R.id.answer_d);
        btn_next = findViewById(R.id.btnRight);
        btn_pre = findViewById(R.id.btnLeft);
    }

    public void gotoHomePage() {

        Bundle b = new Bundle();
        int result = numberOfSelect - countCorrect;
        b.putString("result" , result + " / " + numberOfSelect);
        Log.e(TAG, " Go Home Page : " + result);
        Intent i = new Intent(QuestionActivity.this, MainActivity.class);
        i.putExtras(b);
        startActivity(i);
    }


    public int[] genRandNum(int max_number, int numberOfSelect) {
        int[] sixNum = new int[numberOfSelect];
        Random r = new Random();
        for (int i = 0; i < sixNum.length; i++) {
            sixNum[i] = r.nextInt(max_number) + 1;
            for (int j = 0; j < i; ) {            // 與前數列比較，若有相同則再取亂數
                if (sixNum[j] == sixNum[i]) {
                    sixNum[i] = r.nextInt(max_number) + 1;
                    j = 0;            // 避面重新亂數後又產生相同數字，若出現重覆，迴圈從頭開始重新比較所有數
                } else j++;            // 若都不重複則下一個數
            }
        }
        return sixNum;
    }


    class GetData extends AsyncTask<int[], Integer, String> {
        String TAG = MainActivity.class.getSimpleName();
        ArrayList<TestItem> result = new ArrayList<TestItem>();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(final int[]... strings) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int[] randResult = strings[0];

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        for (int i = 0; i < randResult.length; i++) {
                            TestItem items = ds.child(randResult[i] + "").getValue(TestItem.class);
                            testLists.add(items);
                            //Log.e(TAG, "testList Size : " + testLists.size());
                        }
                    }
                    updateView(page);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(TAG, "Failed to read value.", error.toException());
                }
            });

            return "G";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

}