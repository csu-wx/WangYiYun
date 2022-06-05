package csu.soc.xwz.musicplayer.MineFragmentFolder;

public class SelfList_Content {
    private String selfName;
    private int imgId;

    public SelfList_Content(String selfName, int imgId) {
        this.selfName = selfName;
        this.imgId = imgId;
    }


    public String getSelfName() {
        return selfName;
    }

    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }


    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
