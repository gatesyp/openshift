package co.com.stohio.openshift.main;

/**
 * Created by emerson on 9/17/16.
 */
public class DataObjectSocial {
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private String mText5;
    private String mText6;
    private int mImgId;

    DataObjectSocial(String text4, String text5, String text6, int imgid){
        mImgId = imgid;
        mText4 = text4;
        mText5 = text5;
        mText6 = text6;
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
    public String getmText5() { return mText5; }
    public String getmText6() { return mText6; }

    public int getmImgId() { return mImgId; }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}
