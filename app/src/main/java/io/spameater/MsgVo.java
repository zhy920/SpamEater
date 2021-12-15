package io.spameater;

public class MsgVo {
    private boolean isChecked=false;
    private String Title=null;
    private String Content=null;
    public boolean isChecked(){
        return isChecked;
    }
    public CharSequence getTitle(){
        return Title;
    }
    public CharSequence getContent(){
        return Content;
    }
    public void setChecked(Boolean check){
        isChecked=check;
    }

    public void setTitle(String title) {
        Title=title;
    }

    public void setContent(String content) {
        Content=content;
    }
}
