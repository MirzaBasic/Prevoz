package com.basic.prevoz.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 21.05.2017..
 */

public class StaniceListIconLetter {

    List<String> characterList;


    public static String getLetter(int position){
        List<String> characterList;

        characterList=new ArrayList<String>();

        characterList.add("A");
        characterList.add("B");
        characterList.add("C");
        characterList.add("D");
        characterList.add("E");
        characterList.add("F");
        characterList.add("G");
        characterList.add("H");
        characterList.add("I");
        characterList.add("J");
        characterList.add("K");
        characterList.add("L");
        characterList.add("M");
        characterList.add("N");
        characterList.add("O");
        characterList.add("P");
        characterList.add("Q");
        characterList.add("R");
        characterList.add("S");
        characterList.add("T");
        characterList.add("U");
        characterList.add("V");
        characterList.add("W");
        characterList.add("X");
        characterList.add("Y");
        characterList.add("Z");


        return characterList.get(position);

}}
