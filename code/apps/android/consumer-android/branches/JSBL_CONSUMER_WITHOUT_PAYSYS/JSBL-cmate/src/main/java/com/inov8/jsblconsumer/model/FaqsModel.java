package com.inov8.jsblconsumer.model;

import java.io.Serializable;

public class FaqsModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String question;
    private String answer;

    public FaqsModel(String id, String question, String answer) {
        super();
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
