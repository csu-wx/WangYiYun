package csu.soc.xwz.musicplayer.utils;


import java.util.Comparator;

import csu.soc.xwz.musicplayer.pojo.Singer;

public class SingerLetterComparater implements Comparator<Singer> {

    public int compare(Singer o1, Singer o2) {
        if (o1.getFirstCharacter().equals("@")
                || o2.getFirstCharacter().equals("#")) {
            return 1;
        } else if (o1.getFirstCharacter().equals("#")
                || o2.getFirstCharacter().equals("@")) {
            return -1;
        } else {
            return o1.getFirstCharacter().compareTo(o2.getFirstCharacter());
        }
    }
}
