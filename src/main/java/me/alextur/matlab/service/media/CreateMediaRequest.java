package me.alextur.matlab.service.media;

/**
 * @author Alex Turchynovich
 */
public class CreateMediaRequest {

    private String name;
    private String content;
    private String contentType;

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String pContentType) {
        contentType = pContentType;
    }
}
