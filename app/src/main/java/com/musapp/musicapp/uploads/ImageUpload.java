package com.musapp.musicapp.uploads;

import com.musapp.musicapp.enums.PostUploadType;

public class ImageUpload extends BaseUpload {

    public ImageUpload(){}
    public ImageUpload(String url){
        super(url);
    }
    public void setUrl(String u){
        super.url = u;
    }
    public String getUrl(){
        return super.url;
    }
}

