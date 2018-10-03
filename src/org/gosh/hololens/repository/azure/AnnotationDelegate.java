package org.gosh.hololens.repository.azure;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.gosh.hololens.repository.mhifcreator.CasePackager;
import org.w3c.dom.Document;


public class AnnotationDelegate {


    FileManagerDelegate saver = new AzureBlobStorageSaver();
    CasePackager packager = new CasePackager();

    public void addAnnotation(String id, String comment, String author) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.addAnnotation(id, comment, author, doc);

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


    public void addAnnotationFile(String id, String comment, String author, InputStream uploadedDicom, FormDataContentDisposition details) {
        File file = saver.getFile(id);


        Document doc = loadFile(file);
        doc = packager.addAnnotationFile(id, comment, author, uploadedDicom, details, doc);

        saver.saveFile(id, doc);

    }

    ;


    public void deleteAnnotation(String id, String author) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.deleteAnnotation(author, doc);

        saver.saveFile(id, doc);


    }


}
