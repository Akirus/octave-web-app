package me.alextur.matlab.rest.endpoints.documents;

import org.springframework.stereotype.Component;

/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentLinkManager {

    public final static String ROOT_PATH = "documents";
    public final static String ALL_PATH = "all";

    public String getLink(String id){
        return String.format("%s/%s", ROOT_PATH, id);
    }

}
