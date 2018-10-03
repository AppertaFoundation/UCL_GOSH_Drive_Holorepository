# **GOSH DRIVE HoloRepository**
This is the first implementation of the Azure based HoloRepository originally hosted and run by GOSH DRIVE for the NHS. It is a starting point proof of concept with a few demonstrator examples. 

This project was created For Great Ormond Street Hospital, NHS ‘Digital Research, Informatics and Virtual Environments’ unit (DRIVE):

## Authors 
This project was developed by UCL Computer Science students as part of the UCL Industry Exchange Network (http://ixn.org.uk) which pairs university students with industry as part of their curriculum.
Inventor and project lead - Dr Dean Mohamedally, d.mohamedally@ucl.ac.uk
MSc Student - Immanuel Baskaran
Supervised by - Prof. Neil Sebire of GOSH.

## Medical Holographic Interchange Format
This repository uses a new Proof of Concept XML based file format which has the following features
- It can store multiple scans per patient case.
- It can store tags for faster indexing and searching, ultimately leading to machine learning on categories of patient cases.
- It is a scalable file format (More medical files can be attached eg DICOM files).


##Built With
### Backened
- Java EE
    - Jersey 2
    - Swagger.io
    - Azure Blob Storage
### Frontend
- React
    - Bloomer
    - Superagent
    - Redux
    - Three.js
    - base64-arraybuffer
    - Bulma
##Installation instructions

### Building the source

To build the source you need [Apache Maven](https://maven.apache.org/) and JDK 8 installed. In the root of the project execute `mvn clean install` and the build process will produce a WAR file for local development.
To build for production, specify where the webapp will be hosted in `pom.xml` by changing the variable `PUBLIC_URL` then run `mvn clean install -P prod`

### Deploying to Server

The produced WAR file can then be deployed onto a Java EE server, I have used Tomcat 8 for development.
When you deploy the file, make sure to set the environment variable `storage.connection`
This can be done by specifying the Java argument `-Dstorage.connection=<azure storage connection string>`
## TODO
- [ ] Add basic authentication
- [x] Add basic web-sockets 
    - [ ] Add ability to programmatically specify endpoint connection
- [ ] Use Binary XML for the MHIF files
- [ ] Correct code to use more Java EE features



## Disclaimer
GOSH DRIVE HoloRepository is provided under a  GNU AFFERO GENERAL PUBLIC LICENS and all terms of that licence apply (See Licence.txt). Use of the GOSH DRIVE HoloRepository or code is entirely at your own risk. Neither Immanuel Baskaran nor DRIVE accept any responsibility for loss or damage to any person, property or reputation as a result of using the software or code. No warranty is provided by any party, implied or otherwise. This software and code is not guaranteed safe to use in a clinical or other environment and you should make your own assessment on the suitability for such use. Installation of any GOSH DRIVE HoloRepository software, indicates acceptance of this disclaimer. A supported and maintained version of GOSH DRIVE HoloRepository is available via ImmanuelBaskaran partner DRIVE.
