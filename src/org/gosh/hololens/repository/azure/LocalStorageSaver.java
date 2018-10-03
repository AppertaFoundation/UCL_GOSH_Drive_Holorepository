package org.gosh.hololens.repository.azure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * @author Immanuel
 *
 * This class is used to test file manipluation locally.
 *
 */
public class LocalStorageSaver implements FileManagerDelegate {


    @Override
    public void saveFile(String id, Document xml) {
        File file = new File(FileSystems.getDefault().getPath("/Volumes/Drive2/" + id + ".mhif").toString());


        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // send DOM to file
            tr.transform(new DOMSource(xml),
                    new StreamResult(new FileOutputStream(file)));

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public File getFile(String id) {
        return new File(FileSystems.getDefault().getPath("/Volumes/Drive2/" + id + ".mhif").toString());
    }

    @Override
    public String[] getFiles() {


        File f = new File(FileSystems.getDefault().getPath("/Volumes/Drive2/").toString());
        return f.list();
    }

    @Override
    public boolean deleteFile(String id) throws FileNotFoundException {
        File file = new File(FileSystems.getDefault().getPath("/Volumes/Drive2/" + id + ".mhif").toString());
        if (file.exists()) {
            return file.delete();
        } else {
            throw new FileNotFoundException("");
        }

    }


}
