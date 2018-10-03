package org.gosh.hololens.repository.azure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.gosh.hololens.repository.mhifcreator.CasePackager;
import org.gosh.hololens.repository.mhifcreator.CaseQueryer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class CaseStoreDelegate {


    FileManagerDelegate saver = new AzureBlobStorageSaver();

    CasePackager packager = new CasePackager();
    CaseQueryer query = new CaseQueryer();

    public void uploadCase(String id, JSONObject patientDetails, InputStream uploadedDicom, FormDataContentDisposition details, List<String> tags) {

        Document xml = packager.createMHIFFile(id, patientDetails);
        Node root = xml.getElementsByTagName("meshes").item(0);
        if (uploadedDicom != null) {

            root.appendChild(packager.embedModel(uploadedDicom, details.getFileName(), patientDetails.getString("doctorName"), FilenameUtils.getExtension(details.getFileName()), xml));

        }
        if (tags != null) {
            for (String tag : tags) {
                xml = packager.addMetaTag(xml, tag);
            }
        }
        saver.saveFile(id, xml);

    }


    public void updateCase(String id, JSONObject newDetails) {
        // TODO Auto-generated method stub

        File file = saver.getFile(id);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            try {
                String s = newDetails.getString("patientName");
                Node name = doc.getElementsByTagName("patientName").item(0);
                name.setTextContent(s);
            } catch (JSONException e) {

            }

            try {
                String s = newDetails.getString("lastEditedBy");
                Node name = doc.getElementsByTagName("createdBy").item(0);
                name.setTextContent(s);
            } catch (JSONException e) {

            }


            saver.saveFile(id, doc);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    public InputStream getCase(String id) {
        File file = saver.getFile(id);


        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }


        return stream;
    }


    public Node getModel(String id) {
        File file = saver.getFile(id);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            Node mesh = doc.getElementsByTagName("mesh").item(0);
            return mesh;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public JSONArray getCases(String tag, String id) {


        String[] files = saver.getFiles();
        JSONArray array = new JSONArray();
        List<String> filtered = new ArrayList<>();

        if (id == null) {
            id = "";
        }
        if (tag == null) {
            tag = "";
        }

        for (String file : files) {
            if (file.equals(id)) {
                filtered.add(file);
            } else if (file.contains(id)) {
                filtered.add(file);
            }
        }


        for (String file : filtered) {
            if (tag.equals("")) {
                try {
                    array.put(query.getTags(new FileInputStream(saver.getFile(FilenameUtils.getBaseName(file))), FilenameUtils.getBaseName(file)));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject patientCase = query.filterByTag(new FileInputStream(saver.getFile(FilenameUtils.getBaseName(file))), tag, FilenameUtils.getBaseName(file));
                    if (patientCase != null) {
                        array.put(patientCase);
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        return array;
    }

    public JSONArray getCases() {


        String[] files = saver.getFiles();
        JSONArray array = new JSONArray();
        for (String file : files) {
            Logger.getLogger("InfoLogging").info(file);
            ;
            try {
                array.put(query.getTags(new FileInputStream(saver.getFile(FilenameUtils.getBaseName(file))), FilenameUtils.getBaseName(file)));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return array;
    }

    public byte[] getMesh(String caseID, String meshID) {

        File file = saver.getFile(caseID);

        try {
            return query.getMesh(new FileInputStream(file), caseID, meshID);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public boolean deleteCase(String id) throws FileNotFoundException {
        return saver.deleteFile(id);
    }


}
