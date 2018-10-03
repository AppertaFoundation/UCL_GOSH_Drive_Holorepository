package org.gosh.hololens.repository.azure;

import java.io.File;
import java.io.InputStream;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.gosh.hololens.repository.mhifcreator.CasePackager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.microsoft.azure.storage.file.FileInputStream;


public class MeshDelegate {

    CasePackager packager = new CasePackager();
    FileManagerDelegate saver = new AzureBlobStorageSaver();


    private Document loadFile(File file) throws TransformerFactoryConfigurationError {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;


        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            return doc;


        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    ;

    private Document attachMesh(Document doc, Element annotation) {
        Node root = doc.getElementsByTagName("meshes").item(0);


        root.appendChild(annotation);


        return doc;
    }

    public void addMesh(String id, InputStream uploadedDicom, String author, FormDataContentDisposition details) {
        File file = saver.getFile(id);


        Document doc = loadFile(file);
        doc = attachMesh(doc, packager.embedModel(uploadedDicom, details.getFileName(), author, FilenameUtils.getExtension(details.getFileName()), doc));

        saver.saveFile(id, doc);

    }

    ;

    public void deleteMesh(String id, String meshName) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.deleteMesh(meshName, doc);

        saver.saveFile(id, doc);


    }

    public void updateMesh(String id, String meshName, String author, InputStream newMesh, FormDataContentDisposition details) {
        File file = saver.getFile(id);

        Document doc = loadFile(file);
        doc = packager.deleteMesh(meshName, doc);

        doc = attachMesh(doc, packager.embedModel(newMesh, details.getFileName(), author, FilenameUtils.getExtension(details.getFileName()), doc));


        saver.saveFile(id, doc);

    }


}
