package com.james.ipas;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    }

    public void initView() {
        tv_selector = findViewById(R.id.tv_selector);
        tv_content = findViewById(R.id.tv_content);
        radioGroup = findViewById(R.id.rgroup_answer);
        radioButton_A = findViewById(R.id.answer_a);
        radioButton_B = findViewById(R.id.answer_b);
        radioButton_C = findViewById(R.id.answer_c);
        radioButton_D = findViewById(R.id.answer_d);

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
                    tv_selector.setText("1");
                    tv_content.setText(testLists.get(0).getTopic());
                    Log.e(TAG, "testLists.get(0) : " + testLists.get(0).getAnswer());
                    Log.e(TAG, "testLists.get(0) : " + testLists.get(0).getItem_A());

                    radioButton_A.setText(testLists.get(0).getItem_A());
                    radioButton_B.setText(testLists.get(0).getItem_B());
                    radioButton_C.setText(testLists.get(0).getItem_C());
                    radioButton_D.setText(testLists.get(0).getItem_D());
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