package org.gosh.hololens.repository.mhifcreator;

import org.json.JSONObject;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class CaseQueryer {


    public byte[] getMesh(InputStream file, String fileName, String id) {
        boolean mesh = false;
        String Mesh = null;
        String meshid = null;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(file);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();

                        if (qName.equalsIgnoreCase("mesh")) {

                            Iterator<Attribute> attributes = startElement.getAttributes();
                            meshid = attributes.next().getValue();

                        }

                        if (qName.equals("rawData") && meshid.equals(id)) {
                            mesh = true;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();

                        if (mesh) {
                            Mesh += characters.getData();
                        }

                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String eName = endElement.getName().getLocalPart();
                        if (eName.equals("rawData")) {
                            mesh = false;
                        }
                        break;
                }
            }
        } catch (XMLStreamException e2) {
            e2.printStackTrace();
        }
        byte[] decoded = Base64.getMimeDecoder().decode(Mesh);

        //Removes 3 extra characters created from the base64 conversion
        return Arrays.copyOfRange(decoded, 3, decoded.length);
    }


    public JSONObject getTags(InputStream file, String fileName) {
        boolean keyword = false;
        List<String> tags = new ArrayList<String>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(file);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equals("keyword")) {
                            keyword = true;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();

                        if (keyword) {
                            Logger.getLogger("InfoLogging").info("First Name: " + characters.getData());
                            tags.add(characters.getData());
                            keyword = false;
                        }

                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        break;
                }
            }
        } catch (XMLStreamException e2) {
            e2.printStackTrace();
        }
        JSONObject object = new JSONObject();

        object.put("case", fileName);
        object.put("tags", tags.toArray());
        return object;
    }


    public JSONObject filterByTag(InputStream file, String tag, String fileName) {
        boolean keyword = false;
        boolean found = false;
        List<String> tags = new ArrayList<String>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader =
                    factory.createXMLEventReader(file);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equals("keyword")) {
                            keyword = true;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();

                        if (keyword) {
                            Logger.getLogger("InfoLogging").info("First Name: " + characters.getData());
                            if (characters.getData().contains(tag)) {
                                found = true;
                            }
                            tags.add(characters.getData());
                            keyword = false;
                        }

                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        break;
                }
            }
        } catch (XMLStreamException e2) {
            e2.printStackTrace();
        }
        JSONObject object = new JSONObject();

        object.put("case", fileName);
        object.put("tags", tags.toArray());
        if (found) {
            return object;
        } else {
            return null;
        }
    }


}
