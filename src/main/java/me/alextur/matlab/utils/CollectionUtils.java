package me.alextur.matlab.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex Turchynovich
 */
public class CollectionUtils {

    public static <T> Set<T> copySet(Set<T> set){
        if(set == null){
            return Collections.emptySet();
        }
        return new HashSet<T>(set);
    }

}
