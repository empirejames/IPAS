package com.james.ipas;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    String TAG = MainActivity.class.getSimpleName();
    int numberOfSelect = 3;
    int max_number = 250;
    int [] randItem = new int[numberOfSelect];


    @Override
    protected  void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_main);
        ArrayList<String> passingData = new ArrayList<String>();
        randItem = genRandNum(passingData,max_number, numberOfSelect);
        new GetData().execute(randItem);
    }
    public int [] genRandNum(ArrayList<String> al, int max_number, int numberOfSelect) {
        int[] sixNum = new int[numberOfSelect];
        Random r = new Random();
        for (int i=0; i<sixNum.length; i++){
            sixNum[i]= r.nextInt(max_number)+1;
            al.add(r.nextInt(max_number)+1 +"");
            for (int j=0; j<i;){			// 與前數列比較，若有相同則再取亂數
                if (sixNum[j]==sixNum[i]){
                    sixNum[i] = r.nextInt(max_number)+1;
                    al.add(r.nextInt(max_number)+1 +"");
                    j=0;			// 避面重新亂數後又產生相同數字，若出現重覆，迴圈從頭開始重新比較所有數
                }
                else j++;			// 若都不重複則下一個數
            }
        }
        return  sixNum;
    }

}



class GetData extends AsyncTask<int [] , Integer, String>{
    String TAG = MainActivity.class.getSimpleName();


     @Override
     protected String doInBackground(final int []... strings) {
         DatabaseReference  ref = FirebaseDatabase.getInstance().getReference();
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 int [] randResult = strings[0];

                 for (DataSnapshot ds : dataSnapshot.getChildren() ){

                     for(int i =0; i<randResult.length;i++){
                         TestItem items  = ds.child(randResult[i]+"").getValue(TestItem.class);
                         Log.e(TAG,"Topic :  " +  items.getTopic());
                         Log.e(TAG,"Answer :  " +  items.getAnswer());
                     }
                 }
             }

             @Override
             public void onCancelled(DatabaseError error) {
                 Log.e(TAG, "Failed to read value.", error.toException());
             }
         });

         return null;
     }

     @Override
    protected  void onPreExecute(){

    }

}


