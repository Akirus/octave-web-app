package me.alextur.matlab.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.alextur.matlab.model.user.StudentGroup;

import javax.persistence.*;
import java.util.List;

@Entity
public class TestQuestion {

    private Test test;
    private Long id;
    private List<TestVariant> variants;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "test_id")
    @JsonIgnore
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @OneToMany(mappedBy = "testQuestion", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<TestVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<TestVariant> variants) {
        this.variants = variants;
    }
}
