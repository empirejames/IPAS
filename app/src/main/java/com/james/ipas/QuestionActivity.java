package com.james.ipas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

public class QuestionActivity extends Activity implements View.OnClickListener{
    String TAG = QuestionActivity.class.getSimpleName();
    int numberOfSelect = 3;
    int max_number = 250;
    int[] randItem = new int[numberOfSelect];
    ArrayList<TestItem> testLists = new ArrayList<TestItem>();
    TextView tv_selector, tv_content , txt_getCount;
    RadioGroup radioGroup;
    RadioButton radioButton_A, radioButton_B, radioButton_C, radioButton_D;
    Button btn_next, btn_pre , btn_A, btn_B, btn_C, btn_D;
    int page = 0;
    int getAnswer;
    int answer = 0;
    int countCorrect = 0;
    boolean showFlag;
    private Long startTime;
    String spendTimes;
    private Handler handler = new Handler();


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
        btn_A.setOnClickListener(this);
        btn_B.setOnClickListener(this);
        btn_C.setOnClickListener(this);
        btn_D.setOnClickListener(this);


        //Start timmer :
        startTime = System.currentTimeMillis();
        //設定定時要執行的方法
        handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        handler.postDelayed(updateTimer, 1000);


        randItem = genRandNum(max_number, numberOfSelect);
        new GetData().execute(randItem);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page < numberOfSelect - 1) {
                    if (getAnswer != answer) {
                        Log.e(TAG, "正確解答 : " + answer +"  , 您的解答:  " + getAnswer);

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

    }

    public boolean showAnswer(int answer) {
        countCorrect ++;
        if (answer == 1) {
            btn_A.setTextColor(getResources().getColor(R.color.red));
        } else if (answer == 2) {
            btn_B.setTextColor(getResources().getColor(R.color.red));
        } else if (answer == 3) {
            btn_C.setTextColor(getResources().getColor(R.color.red));
        } else {
            btn_D.setTextColor(getResources().getColor(R.color.red));
        }
        return false;
    }


    public void resetBtnBackground(){
        btn_A.setBackgroundResource(R.drawable.rule_button_shape);
        btn_B.setBackgroundResource(R.drawable.rule_button_shape);
        btn_C.setBackgroundResource(R.drawable.rule_button_shape);
        btn_D.setBackgroundResource(R.drawable.rule_button_shape);
    }

    public void resetBtnTextColor(){
        btn_A.setTextColor(getResources().getColor(R.color.colorWhite));
        btn_B.setTextColor(getResources().getColor(R.color.colorWhite));
        btn_C.setTextColor(getResources().getColor(R.color.colorWhite));
        btn_D.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    public int updateView(int page) {

        resetBtnTextColor();
        resetBtnBackground();
        txt_getCount.setText(page+1 + " / " + numberOfSelect);

        if (testLists.size() != 0 && page >= 0) {
            Log.e(TAG,"testLists size" + "  :  " +testLists.size() + " Page  :" + page);
            tv_content.setText(testLists.get(page).getTopic());
            tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
            btn_A.setText(testLists.get(page).getItem_A());
            btn_B.setText(testLists.get(page).getItem_B());
            btn_C.setText(testLists.get(page).getItem_C());
            btn_D.setText(testLists.get(page).getItem_D());
            Log.e(TAG,"Update Page" + "    " + testLists.get(page).getTopic());
            Log.e(TAG,"Update Page" + "    " + testLists.get(page).getAnswer());
            Log.e(TAG,"Update Page" + "    " + testLists.get(page).getItem_A());
            Log.e(TAG,"Update Page" + "    " + testLists.get(page).getItem_B());


            if (testLists.get(page).getAnswer().contains("A")) {
                answer = 1;
            } else if (testLists.get(page).getAnswer().contains("B")) {
                answer = 2;
            } else if (testLists.get(page).getAnswer().contains("C")) {
                answer = 3;
            } else if (testLists.get(page).getAnswer().contains("D")) {
                answer = 4;
            }
        }
        return answer;
    }

    public void initView() {
        txt_getCount = findViewById(R.id.txt_getCount);
        tv_content = findViewById(R.id.tv_content);
        btn_next = findViewById(R.id.btnRight);
        btn_pre = findViewById(R.id.btnLeft);
        btn_A = findViewById(R.id.btn_A);
        btn_B = findViewById(R.id.btn_B);
        btn_C = findViewById(R.id.btn_C);
        btn_D = findViewById(R.id.btn_D);
    }

    public void gotoHomePage() {

        Bundle b = new Bundle();
        int result = numberOfSelect - countCorrect;
        b.putString("result" , result + " / " + numberOfSelect);
        b.putString("spendTimes" , spendTimes);
        Log.e(TAG, " Go Home Page : " + result);
        Intent i = new Intent(QuestionActivity.this, MainActivity.class);
        i.putExtras(b);
        startActivity(i);
    }
    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (TextView) findViewById(R.id.timer);
            String txt_minius, txt_seconds;

            Long spentTime = System.currentTimeMillis() - startTime;
            //計算目前已過分鐘數
            Long minius = (spentTime/1000)/60;
            //計算目前已過秒數
            Long seconds = (spentTime/1000) % 60;

            txt_minius = String.valueOf(minius);
            txt_seconds = String.valueOf(seconds);

            if(txt_minius.length()<2){
                txt_minius = "0"+txt_minius;
            }
            if(txt_seconds.length()<2){
                txt_seconds = "0"+txt_seconds;
            }
            time.setText(txt_minius+" : "+txt_seconds);
            spendTimes = txt_minius+" : "+txt_seconds;
            handler.postDelayed(this, 1000);
        }
    };



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

    @Override
    public void onClick(View v) {
        resetBtnBackground();
        switch (v.getId()){
            case  R.id.btn_A :
                getAnswer = 1;
                btn_A.setBackgroundResource(R.drawable.rule_error_button_shape);
                resetBtnTextColor();
                break;
            case R.id.btn_B :
                getAnswer = 2;
                btn_B.setBackgroundResource(R.drawable.rule_error_button_shape);
                resetBtnTextColor();
                break;
            case   R.id.btn_C:
                getAnswer = 3;
                btn_C.setBackgroundResource(R.drawable.rule_error_button_shape);
                resetBtnTextColor();
                break;
            case R.id.btn_D:
                getAnswer = 4;
                btn_D.setBackgroundResource(R.drawable.rule_error_button_shape);
                resetBtnTextColor();
                break;
        }

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
                            Log.e(TAG, "testList Size : " + testLists.size());
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