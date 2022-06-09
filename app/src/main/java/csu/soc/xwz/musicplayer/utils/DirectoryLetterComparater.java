package csu.soc.xwz.musicplayer.utils;

import java.util.Comparator;

import csu.soc.xwz.musicplayer.pojo.Directory;

public class DirectoryLetterComparater implements Comparator<Directory> {

    public int compare(Directory o1, Directory o2) {
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
