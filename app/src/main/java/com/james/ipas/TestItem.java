package com.james.ipas;

public class TestItem {
    String TAG  = TestItem.class.getSimpleName();

    String topic, answer, A, B, C, D;
    public TestItem() {}
    public TestItem(String topic, String answer, String A, String B, String C, String D){
        this.topic = topic;
        this.answer = answer;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;

    }


    public String getTopic(){
        return  topic;
    }
    public String getAnswer(){
        return answer;
    }
    public String getA(){
        return A;
    }
    public String getB(){
        return B;
    }
    public String getC(){
        return C;
    }
    public String getD(){
        return D;
    }
}
