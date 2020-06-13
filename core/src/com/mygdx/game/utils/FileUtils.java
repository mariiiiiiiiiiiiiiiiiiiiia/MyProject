package com.mygdx.game.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Const;


public class FileUtils {

    public static String getBody(String filepath, boolean isLocal) {
        FileHandle fileHandle;
        if (!Const.TYPE.equals(Application.ApplicationType.Android)) {
            if (isLocal)
                fileHandle = Gdx.files.local(filepath);
            else
                fileHandle = Gdx.files.external(filepath);
        } else {
            if (isLocal)
                fileHandle = Gdx.files.local("assets/" + filepath);
            else
                fileHandle = Gdx.files.external(filepath);
        }
        return fileHandle.readString();
    }

    public static FileHandle getFileHandle(String filepath) {
        FileHandle local;
        if (!Const.TYPE.equals(Application.ApplicationType.Android))
            local = Gdx.files.local(filepath);
        else
            local = Gdx.files.local("assets/" + filepath);
        return local;
    }
}
