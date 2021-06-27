package org.techtown.myproject;

public class Item {
    String text_main;
    String text_edit;
    Boolean check = false;
    int cardBackGroundColor = -1;
    String time;

    public Item(String text_main, String text_edit, Boolean check, int cardBackGroundColor, String time) {
        this.text_main = text_main;
        this.text_edit = text_edit;
        this.check = check;
        this.cardBackGroundColor = cardBackGroundColor;
        this.time = time;
    }

    public Item(String text_main, String time) {
        this.text_main = text_main;
        this.time = time;
    }

    public Item(String text_main) {
        this.text_main = text_main;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Item(Boolean check) {
        this.check = check;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getText_main() {
        return text_main;
    }

    public void setText_main(String text_main) {
        this.text_main = text_main;
    }

    public String getText_edit() {
        return text_edit;
    }

    public void setText_edit(String text_edit) {
        this.text_edit = text_edit;
    }

    public int getCardBackGroundColor() {
        return cardBackGroundColor;
    }

    public void setCardBackGroundColor(int cardBackGroundColor) {
        this.cardBackGroundColor = cardBackGroundColor;
    }

    public String getInfo(){
        return "텍스트 : " + this.text_main + " 에딧텍스트 : " + this.text_edit + " 체크값 : " + this.getCheck() + " 배경값 : " + String.valueOf(this.getCardBackGroundColor());
    }
}
