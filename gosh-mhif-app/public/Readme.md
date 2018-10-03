# **GOSH DRIVE HoloRepository**
This is the first implementation of the Azure based HoloRepository to be hosted and run by GOSH DRIVE for the NHS. It is a starting point proof of concept with a few demonstrator examples. 


It is my Supervisor’s hope to see a continuation of student projects around the containment, indexing and ultimately machine learning of patient related and surgical teaching related holographics.

This project was created For Great Ormond Street Hospital, NHS ‘Digital Research, Informatics and Virtual Environments’ unit (DRIVE):

This project was supervised by Dr. Dean Mohamedally of UCL and Prof. Neil Sebire of GOSH.

## Medical Holographic Interchange Format
This repository uses a new Proof of Concept XML based file format which has the following features
- It can store multiple scans per patient case.
- It can store tags for faster indexing and searching, ultimately leading to machine learning on categories of patient cases.
- It is a scalable file format (More medical files can be attached eg DICOM files).


## Built With
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