package me.alextur.matlab.utils;

import java.util.*;

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

    public static <T> List<T> copyList(List<T> list){
        if(list == null){
            return Collections.emptyList();
        }

        return new ArrayList<>(list);
    }

}
