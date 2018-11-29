package com.james.ipas;

public class TestItem {
    String TAG  = TestItem.class.getSimpleName();

    String topic, answer, A, B, C, D;
    public TestItem() {}
    public TestItem(String topic, String answer, String item_A, String item_B, String item_C, String item_D){
        this.topic = topic;
        this.answer = answer;
        this.A = item_A;
        this.B = item_B;
        this.C = item_C;
        this.D = item_D;

    }


    public String getTopic(){
        return  topic;
    }
    public String getAnswer(){
        return answer;
    }
    public String getItem_A(){
        return A;
    }
    public String getItem_B(){
        return B;
    }
    public String getItem_C(){
        return C;
    }
    public String getItem_D(){
        return D;
    }
}
