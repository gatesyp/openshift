package co.com.stohio.openshift.main;

/**
 * Created by emerson on 9/17/16.
 */
public class DataObject {
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private int mImgId;

    DataObject (String text1, String text2, int imgid, String text3, String text4 ){
        mText1 = text1;
        mText2 = text2;
        mImgId = imgid;
        mText3 = text3;
        mText4 = text4;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }
    public String getmText3() { return mText3; }
    public String getmText4() { return mText4; }

    public int getmImgId() { return mImgId; }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}
