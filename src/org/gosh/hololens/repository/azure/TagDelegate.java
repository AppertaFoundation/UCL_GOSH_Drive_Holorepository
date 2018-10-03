package org.gosh.hololens.repository.azure;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.gosh.hololens.repository.mhifcreator.CasePackager;
import org.w3c.dom.Document;

public class TagDelegate {

    FileManagerDelegate saver = new AzureBlobStorageSaver();
    CasePackager packager = new CasePackager();


    public void addTag(String id, String tag) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.addMetaTag(doc, tag);

        saver.saveFile(id, doc);


    }

    private Document loadFile(File file) throws TransformerFactoryConfigurationError {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;


        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            return doc;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ;


    public void updateTag(String id, String tagID, String tag) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.updateMetaTag(doc, tagID, tag);
        saver.saveFile(id, doc);
    }


    public void deleteTag(String id, String tagID) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.deleteMetaTag(doc, tagID);
        saver.saveFile(id, doc);
    }

}
