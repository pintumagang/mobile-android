package com.pintumagang.android_app.entity;

public class FcmPushInfo {
    private String body;
    private String title;
    private int no;
    private String regDate;

    public FcmPushInfo(int no, String title, String body, String regDate) {
        this.no = no;
        this.body = body;
        this.title = title;
        this.regDate = regDate;
    }

    public int getNo() {return no;}
    public String getBody() {return body;}
    public String getTitle() {return title;}
    public String getRegDate() {return regDate;}

    public String[] getFcmPushInfoArray(){
        String[] info = {
                Integer.toString(this.no),
                this.body,
                this.title,
                this.regDate
        };

        return info;
    }
}
