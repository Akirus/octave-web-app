package me.alextur.matlab.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class TestVariant {

    private Long id;
    private String content;
    private boolean isCorrect;
    private TestQuestion testQuestion;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @ManyToOne
    @JoinColumn(name = "test_question_id")
    @JsonIgnore
    public TestQuestion getTestQuestion() {
        return testQuestion;
    }

    public void setTestQuestion(TestQuestion testQuestion) {
        this.testQuestion = testQuestion;
    }
}
