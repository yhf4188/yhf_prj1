package com.chat.model;


import android.os.Parcelable;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getA() {
        return A;
    }
    public void setA(String a) {
        A = a;
    }
    public String getB() {
        return B;
    }
    public void setB(String b) {
        B = b;
    }
    public String getC() {
        return C;
    }
    public void setC(String c) {
        C = c;
    }
    public String getD() {
        return D;
    }
    public void setD(String d) {
        D = d;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public Question() {}
    public Question(String question, String a, String b, String c, String d, String answer) {
        super();
        this.question = question;
        A = a;
        B = b;
        C = c;
        D = d;
        this.answer = answer;
    }

}

