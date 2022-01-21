package com.example.miniproj2admin.models;

public class questionsModel {

    private String id,question,A,B,C,D,answer,set;


    public questionsModel(String id ,String question,String A,String B,String C,String D,String answer,String set)
    {
        this.id=id;
        this.question=question;
       this.A=A;
        this.B=B;
        this.C=C;
        this.D=D;
        this.answer=answer;
        this.set=set;

    }

    public questionsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }
}

