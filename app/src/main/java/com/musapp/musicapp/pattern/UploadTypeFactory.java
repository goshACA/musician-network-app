package com.musapp.musicapp.pattern;

import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.uploads.ImageUpload;
import com.musapp.musicapp.uploads.MusicUpload;
import com.musapp.musicapp.uploads.VideoUpload;

public final class UploadTypeFactory {
    private UploadTypeFactory(){}

    public static BaseUpload setUploadByType(PostUploadType type, String url){
        switch (type){
            case IMAGE: return new ImageUpload(url);
            case VIDEO: return new VideoUpload(url);
            case MUSIC: return new MusicUpload(url);
            default: return new BaseUpload();
        }
    }
}
