package com.musapp.musicapp.uploads;

import com.musapp.musicapp.enums.PostUploadType;

public class BaseUpload {

    protected String url;


    public BaseUpload(String url){
        this.url = url;
    }

    public BaseUpload(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
