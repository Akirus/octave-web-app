package me.alextur.matlab.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.alextur.matlab.model.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Test")
public class Test extends TreeEntity {

    private List<TestQuestion> questions;
    private List<TestSubmission> submissions;

    private String introductionText;

    public String getIntroductionText() {
        return introductionText;
    }

    public void setIntroductionText(String pIntroductionText) {
        introductionText = pIntroductionText;
    }

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<TestQuestion> getQuestions() {
        return questions;
    }

    @Transient
    @JsonIgnore
    public List<Long> getQuestionIds(){
        List<TestQuestion> questions = getQuestions();
        if(questions != null){
            return questions.stream()
                    .map(TestQuestion::getId)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public void setQuestions(List<TestQuestion> questions) {
        this.questions = questions;
    }

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<TestSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<TestSubmission> submissions) {
        this.submissions = submissions;
    }
}
