package com.james.ipas;

public class TestItem {
    String TAG  = TestItem.class.getSimpleName();

    String topic, answer, item_A, item_B, item_C, item_D;
    public TestItem() {}
    public TestItem(String topic, String answer, String item_A, String item_B, String item_C, String item_D){
        this.topic = topic;
        this.answer = answer;
        this.item_A = item_A;
        this.item_B = item_B;
        this.item_C = item_C;
        this.item_D = item_D;

    }


    public String getTopic(){
        return  topic;
    }
    public String getAnswer(){
        return answer;
    }
    public String getItem_A(){
        return item_A;
    }
    public String getItem_B(){
        return item_B;
    }
    public String getItem_C(){
        return item_C;
    }
    public String getItem_D(){
        return item_D;
    }
}
