package com.enrico.gallery.galleryapp.albums;


import java.io.File;

public class Albums {

    private String albumPath;

    void setAlbumsUrl(String albumUrl) {

        File f = new File(albumUrl);
        String absolutePath = f.getPath();

        String filename = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);

        setAlbumsPath(absolutePath.replace(filename, ""));

    }

    String getAlbumsPath() {
        return albumPath;
    }

    private void setAlbumsPath(String albumPath) {
        this.albumPath = albumPath;
    }

}

