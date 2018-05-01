package com.pintumagang.android_app.entity;

public class FcmPushInfo {
    private String message;
    private String title;
    private int no,id_user;
    private String regDate;

    public FcmPushInfo(int no,int id_user, String title, String message, String regDate) {
        this.no = no;
        this.id_user = id_user;
        this.message = message;
        this.title = title;
        this.regDate = regDate;

    }

    public int getNo() {return no;}
    public int getId_user(){return id_user;}
    public String getMessage() {return message;}
    public String getTitle() {return title;}
    public String getRegDate() {return regDate;}

    public String[] getFcmPushInfoArray(){
        String[] info = {
                Integer.toString(this.no),
                Integer.toString(this.id_user),
                this.title,
                this.message,
                this.regDate
        };

        return info;
    }

    public String[] insertPushInfoArray(){
    String[] info= {
            Integer.toString(this.id_user),
            this.title,
            this.message
    };
    return info;
    }
}
