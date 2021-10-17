package com.musapp.musicapp.firebase;

public interface DBAsyncTaskResponse {
    void doOnResponse(String str, String type);

    void doForResponse(String str, Object obj);
}
