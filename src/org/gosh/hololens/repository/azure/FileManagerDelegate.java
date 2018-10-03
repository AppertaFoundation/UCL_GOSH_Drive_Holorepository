package org.gosh.hololens.repository.azure;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;

public interface FileManagerDelegate {
    void saveFile(String id, Document xml);

    File getFile(String id);

    String[] getFiles();

    boolean deleteFile(String id) throws FileNotFoundException;
}
