package co.com.stohio.openshift.main;

/**
 * Created by emerson on 9/17/16.
 */
public class DataObjectEvents {
    private String mText4;
    private String mText5;
    private String mText6;
    private int mImgId;

    DataObjectEvents(String text4, String text5, String text6, int imgid ){
        mText4 = text4;
        mText5 = text5;
        mText6 = text6;
        mImgId = imgid;
    }

    public String getmText4() {
        return mText4;
    }

    public String getmText5() {
        return mText5;
    }
    public String getmText6() { return mText6; }

    public int getmImgId() { return mImgId; }

}
