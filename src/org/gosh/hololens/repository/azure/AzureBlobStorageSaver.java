package org.gosh.hololens.repository.azure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class AzureBlobStorageSaver implements FileManagerDelegate{

    public static final String storageConnectionString = System.getProperty("storage.connection").replace("'", "");
    ;

    public void saveFile(String id, Document xml) {

        CloudStorageAccount storageAccount = null;
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
        } catch (InvalidKeyException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (URISyntaxException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = null;
        try {
            container = blobClient.getContainerReference("blob");
        } catch (URISyntaxException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (StorageException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            throw new RuntimeException("Could not save case");
        }

        // Create the container if it does not exist with public access.
        System.out.println("Creating container: " + container.getName());
        try {
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        File file = null;
        try {
            file = File.createTempFile(id, ".mhif");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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


            CloudBlockBlob blob = container.getBlockBlobReference(id + ".mhif");

            //Creating blob and uploading file to it
            System.out.println("Uploading the sample file ");
            blob.uploadFromFile(file.getAbsolutePath());
            file.deleteOnExit();


        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (StorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public File getFile(String id) {
        CloudStorageAccount storageAccount = null;
        File temp = null;
        try {
            temp = File.createTempFile(id, ".mhif");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
        } catch (InvalidKeyException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (URISyntaxException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = null;
        try {
            container = blobClient.getContainerReference("blob");
        } catch (URISyntaxException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (StorageException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();

        }
        CloudBlockBlob blob = null;
        try {
            blob = container.getBlockBlobReference(id + ".mhif");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (StorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            blob.downloadToFile(temp.getAbsolutePath());
        } catch (StorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return temp;
    }


    public String[] getFiles() {
        List<String> files = new ArrayList<>();
        CloudStorageAccount storageAccount = null;
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
        } catch (InvalidKeyException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (URISyntaxException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = null;
        try {
            container = blobClient.getContainerReference("blob");
        } catch (URISyntaxException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (StorageException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        for (ListBlobItem blobItem : container.listBlobs()) {
            files.add(blobItem.getUri().getPath().replaceFirst("^/blob/", ""));
        }
        return files.toArray(new String[files.size()]);
    }

    public boolean deleteFile(String id) throws FileNotFoundException {
        CloudStorageAccount storageAccount = null;
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
        } catch (InvalidKeyException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (URISyntaxException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = null;
        try {
            container = blobClient.getContainerReference("blob");
        } catch (URISyntaxException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (StorageException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        // Create the container if it does not exist with public access.
        System.out.println("Creating container: " + container.getName());
        try {
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        CloudBlockBlob blob;
        try {
            blob = container.getBlockBlobReference(id + ".mhif");
            blob.delete();
            return true;
        } catch (URISyntaxException | StorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FileNotFoundException("Case cannot be found");
        }


    }


}
