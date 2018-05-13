package me.alextur.matlab.model.media;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author Alex Turchynovich
 */
@Entity
public class Media {

    private Long id;

    private String name;
    private String contentType;
    private byte[] content;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String pContentType) {
        contentType = pContentType;
    }

    @Lob
    @Column(length=100000)
    @JsonIgnore
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] pContent) {
        content = pContent;
    }
}
