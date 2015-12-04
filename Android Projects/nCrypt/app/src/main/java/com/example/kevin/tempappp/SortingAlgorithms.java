package com.example.kevin.tempappp;

import java.util.Comparator;

/**
 * Created by Steve on 2015-12-03.
 */
public class SortingAlgorithms implements Comparator<Conversation> {

    @Override
    public int compare(Conversation lhs, Conversation rhs) {
        return rhs.getPriority() - lhs.getPriority();
    }

}
