package com.example.shesprototype.SaloonShopDetailsScreen.Gallary;

public class GallaryModel {

    String name;
    int img;
    String post;

    public GallaryModel(String name, int img, String post) {
        this.name = name;
        this.img = img;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
