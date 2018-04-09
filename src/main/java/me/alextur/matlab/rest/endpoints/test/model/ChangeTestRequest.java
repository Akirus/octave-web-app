package me.alextur.matlab.rest.endpoints.test.model;

import me.alextur.matlab.rest.endpoints.documents.model.ChangeTreeEntityRequest;

/**
 * @author Alex Turchynovich
 */
public class ChangeTestRequest extends ChangeTreeEntityRequest {

    private String introductionText;

    public String getIntroductionText() {
        return introductionText;
    }

    public void setIntroductionText(String pIntroductionText) {
        introductionText = pIntroductionText;
    }
}
