package org.gosh.hololens.repository.mhifcreator;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.gosh.hololens.repository.azure.LocalStorageSaver;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * @author Immanuel Baskaran
 */
public class CasePackager {

    /**
     * Creates the Basic Medical Health-care Interchange format file as a {@link Document} object
     * <p>
     * The {@link Document} still needs to be saved to a file using a {@link Transformer}, Please see {@link LocalStorageSaver} for an example
     *
     * @param id         The patient id of the case, can be used to store multiple cases
     * @param caseDetail A {@link JSONObject} string describing the patient details,
     * @return {@link Document}
     * @TODO Change so that a FHIR link can be inserted to integrate with EHR record
     */
    public Document createMHIFFile(String id, JSONObject caseDetail) {
        Document dom;
        Element e = null;


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();
            // create the root element
            Element rootEle = dom.createElement("patientCase");

            //A patient may have multiple cases
            Attr idp = dom.createAttribute("id");
            idp.appendChild(dom.createTextNode(id));
            rootEle.setAttributeNode(idp);

            //Use a fhir compliant Identifier to link
            e = dom.createElement("patientId");
            e.appendChild(dom.createTextNode(caseDetail.getString("patientId")));
            rootEle.appendChild(e);


            e = dom.createElement("revisionNumber");
            e.appendChild(dom.createTextNode(Integer.toString(0)));
            rootEle.appendChild(e);


            e = dom.createElement("lastEditedBy");
            e.appendChild(dom.createTextNode(caseDetail.getString("createdBy")));
            rootEle.appendChild(e);

            e = dom.createElement("DateAndTimeOfLastEdit");
            e.appendChild(dom.createTextNode(LocalDateTime.now().toString()));
            rootEle.appendChild(e);

            e = dom.createElement("meshes");
            rootEle.appendChild(e);

            e = dom.createElement("annotations");
            rootEle.appendChild(e);
            dom.appendChild(rootEle);

            return dom;

        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
            return null;
        }
    }


    /**
     * Creates an XML element that stores the given mesh file and metadata regarding the file
     *
     * @param file      The contents of the file itself
     * @param name      The name of the mesh file
     * @param extention the file extension for the model so that client app's can use the appropriate loader
     * @param dom       The Document object that is required to create the new model Element
     * @return An {@link Element} object that can be attached to the document
     */


    public Element embedModel(InputStream file, String name, String author, String extention, Document dom) {


        Node revision = dom.getElementsByTagName("revisionNumber").item(0);
        int currentRevision = Integer.parseInt(revision.getTextContent());

        Element modelTag = dom.createElement("mesh");
        modelTag.setAttribute("id", name);

        Element f = dom.createElement("name");
        f.appendChild(dom.createTextNode(name));
        modelTag.appendChild(f);

        Node authorNode = dom.createElement("author");

        //TODO: Add ability to specify who added the mesh
        authorNode.appendChild(dom.createTextNode(author));
        modelTag.appendChild(authorNode);

        Element type = dom.createElement("fileType");
        type.appendChild(dom.createTextNode(extention));
        modelTag.appendChild(type);
        Element dateOfEdit = dom.createElement("timeOfAddition");
        dateOfEdit.appendChild(dom.createTextNode(LocalDateTime.now().toString()));
        modelTag.appendChild(dateOfEdit);
        Node annotationRevison = dom.createElement("revisionNumber");
        annotationRevison.appendChild(dom.createTextNode(Integer.toString(currentRevision)));
        modelTag.appendChild(annotationRevison);


        try {
            String content = encodeFileToBase64Binary(file);
            Element rawData = dom.createElement("rawData");
            rawData.appendChild(dom.createTextNode(content));
            modelTag.appendChild(rawData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentRevision += 1;
        revision.setTextContent(Integer.toString(currentRevision));

        return modelTag;
    }

    /**
     * Helper method to add keyword tag to patient case
     *
     * @param doc
     * @param tag
     * @return
     */
    public Document addMetaTag(Document doc, String tag) {
        doc = attachElement(doc, createMetaTag(doc, tag));

        return doc;
    }

    /**
     * Helper method to remove keyword tag from patient case
     *
     * @param doc
     * @param id
     * @return
     */
    public Document deleteMetaTag(Document doc, String id) {
        doc = removeTag(id, doc);

        return doc;
    }

    /**
     * Helper method to update keyword tag in patient case with new tag
     *
     * @param doc
     * @param tag
     * @return
     */
    public Document updateMetaTag(Document doc, String id, String tag) {
        doc = removeTag(id, doc);

        doc = attachElement(doc, createMetaTag(doc, tag));
        return doc;
    }

    /**
     * Helper method to create a new meta keyword tag
     *
     * @param doc
     * @param tag
     * @return
     */
    private Element createMetaTag(Document doc, String tag) {
        Element keywordTag = doc.createElement("keyword");
        keywordTag.setAttribute("id", UUID.randomUUID().toString());
        keywordTag.appendChild(doc.createTextNode(tag));
        return keywordTag;
    }


    /**
     * Creates a element that contains base information of a comment
     * made by a doctor, This method will increment the revisionNumber tag
     *
     * @param doc     The document object needed to create the element
     * @param comment
     * @param author
     * @return The {@link Element} that contains the comment and metadata regarding it
     */
    private Element createBaseAnnotation(Document doc, String comment, String author) {

        Node revision = doc.getElementsByTagName("revisionNumber").item(0);


        int currentRevision = Integer.parseInt(revision.getTextContent());

        Element test = doc.createElement("annotation");
        test.setAttribute("id", UUID.randomUUID().toString());
        Node authorNode = doc.createElement("author");
        Node timeOfEdit = doc.createElement("timeofEdit");

        Node annotationRevison = doc.createElement("revisionNumber");

        authorNode.appendChild(doc.createTextNode(author));
        annotationRevison.appendChild((doc.createTextNode(Integer.toString(currentRevision))));

        timeOfEdit.appendChild(doc.createTextNode(LocalDateTime.now().toString()));


        test.appendChild(authorNode);
        test.appendChild(annotationRevison);
        test.appendChild(timeOfEdit);
        currentRevision += 1;
        revision.setTextContent(Integer.toString(currentRevision));

        return test;
    }

    /**
     * Used to append a text annotationData tag as per the XML Schema
     *
     * @param doc
     * @param baseAnotation
     * @param comment
     * @return
     */
    private Element createTextAnnotation(Document doc, Element baseAnotation, String comment) {
        Element textComment = doc.createElement("annotationData");
        textComment.setAttribute("type", "Text");
        textComment.setAttribute("tag", "comment");
        textComment.setAttribute("vector", "");
        textComment.appendChild(doc.createTextNode(comment));
        baseAnotation.appendChild(textComment);
        return textComment;
    }

    /**
     * Helper method to Attach a given element to a MHIF document
     *
     * @param doc
     * @param annotation
     * @return
     */
    private Document attachElement(Document doc, Element annotation) {
        Node root = doc.getElementsByTagName("annotations").item(0);
        root.appendChild(annotation);

        return doc;
    }


    private Element attachFile(Document dom, Element annotation, String name, String extention, InputStream file) {
        Element fileTag = dom.createElement("annotationData");
        //TODO: Extract file-type
        fileTag.setAttribute("type", "image");
        fileTag.setAttribute("fileExtension", extention);
        fileTag.setAttribute("tag", name);
        fileTag.setAttribute("vector", "");

        try {
            String content = encodeFileToBase64Binary(file);
            fileTag.appendChild(dom.createTextNode(content));
            annotation.appendChild(fileTag);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return annotation;
    }


    /**
     * Helper method to find and delete an annotation node from the given document
     *
     * @param id
     * @param doc
     * @return
     */

    private Document removeAnnotation(String id, Document doc) {
        for (int i = 0; i < doc.getElementsByTagName("annotation").getLength(); i++) {
            if (((Element) doc.getElementsByTagName("annotation").item(i)).getAttribute("id").equals(id)) {

                Node removed = doc.getElementsByTagName("annotation").item(i);
                removed.getParentNode().removeChild(removed);
            }
        }
        return doc;
    }


    /**
     * Helper method to find and delete a keyword node from the given document
     *
     * @param id
     * @param doc
     * @return
     */
    private Document removeTag(String id, Document doc) {
        for (int i = 0; i < doc.getElementsByTagName("keyword").getLength(); i++) {
            if (((Element) doc.getElementsByTagName("keyword").item(i)).getAttribute("id").equals(id)) {

                Node removed = doc.getElementsByTagName("keyword").item(i);
                removed.getParentNode().removeChild(removed);
            }
        }
        return doc;
    }

    /**
     * Helper method to remove a mesh from a case given an id
     *
     * @param meshName
     * @param doc
     * @return
     */
    private Document removeMesh(String meshName, Document doc) {

        Element meshes = (Element) doc.getElementsByTagName("meshes").item(0);

        for (int i = 0; i < meshes.getElementsByTagName("mesh").getLength(); i++) {
            if (((Element) meshes.getElementsByTagName("mesh").item(i)).getAttribute("id").equals(meshName)) {

                Node removed = meshes.getElementsByTagName("mesh").item(i);
                removed.getParentNode().removeChild(removed);
            }
        }


        return doc;
    }


    /**
     * @param id
     * @param comment
     * @param author
     * @param uploadedDicom
     * @param details
     * @param doc
     * @return
     */

    public Document addAnnotationFile(String id, String comment, String author, InputStream uploadedDicom, FormDataContentDisposition details, Document doc) {

        doc = attachElement(doc, attachFile(doc, createBaseAnnotation(doc, comment, author), details.getFileName(), FilenameUtils.getExtension(details.getFileName()), uploadedDicom));

        return doc;

    }

    /**
     * Public method to add an text comment to a given MHIF Document
     *
     * @param id
     * @param comment
     * @param author
     * @param doc
     * @return
     */
    public Document addAnnotation(String id, String comment, String author, Document doc) {

        doc = attachElement(doc, createTextAnnotation(doc, createBaseAnnotation(doc, comment, author), comment));

        return doc;


    }

    /**
     * Public method to remove an annotation from a given MHIF Document
     *
     * @param id
     * @param dom
     * @return The document with the annotation removed
     */
    public Document deleteAnnotation(String id, Document dom) {

        dom = removeAnnotation(id, dom);

        return dom;


    }

    /**
     * Public method to remove a mesh from a given MHIF Document
     *
     * @param name
     * @param dom
     * @return The document with the annotation removed
     */
    public Document deleteMesh(String name, Document dom) {

        dom = removeMesh(name, dom);

        return dom;


    }


    /**
     * Helper method to convert a file into Base64 for use in the MHIF file
     * <p>
     * Please see embedModel for an example
     *
     * @param file
     * @return
     * @throws IOException
     */
    private String encodeFileToBase64Binary(InputStream file)
            throws IOException {


        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(file);
        byte[] encoded = java.util.Base64.getMimeEncoder().encode(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }


}
