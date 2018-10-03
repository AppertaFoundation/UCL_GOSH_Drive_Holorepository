import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Scene from './Scene'
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input, Title, Modal,ModalBackground,ModalCard,ModalCardHeader,ModalCardBody,
        ModalCardFooter, ModalCardTitle} from 'bloomer';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import request from 'superagent';
import MeshItem from './util/MeshItem'
import ScrollArea from 'react-scrollbar'
import AnnotationItem from './util/AnnotationItem'

import store from './stores/activeCaseStore'





class CaseView extends Component {

  constructor(props){
    super(props);
    this.state = {active:false,willSend:false}
    this.deleteSelf = this.deleteSelf.bind(this)
    //TODO Programmatically assign the websocket connection
    this.exampleSocket = new WebSocket("wss://goshmhif.azurewebsites.net/hololens/one/");

    this.exampleSocket.onmessage = function (event) {
        console.log(event);
     }
    this.sendToHololens = this.sendToHololens.bind(this);

  }



    componentWillUnmount(){

         this.exampleSocket.close();
    }



  sendToHololens(caseFile,mesh){
       var rx = Math.floor((Math.random() * 20) - 10);
       var rz = Math.floor((Math.random() * 20) - 10);

        var message = {
                            content:"https://goshmhif.azurewebsites.net/api/case/model/"+caseFile+"/"+mesh,
                            sender:"webapp",
                            direct:"false",
                            position:{x:rx,y:0,z:rx}
                            };
      this.exampleSocket.send(JSON.stringify(message));
  }

  componentDidMount(){
    request.get(process.env.PUBLIC_URL+"/api/case/"+this.props.match.params.id)
        .set('Accept', 'application/xml')
        .end ((error, res)=>{
            let xmlDoc = new DOMParser().parseFromString(res.text,"text/xml");
            store.dispatch({type: 'SWITCH-CASE', values:xmlDoc})
            this.setState({active:true})
          });

  }







  renderMeshes(){
    if(this.state.active === true){
          let annotations = store.getState().getElementsByTagName('mesh')
          let len = annotations.length;
          let arrayofnodes = [];
          let i;
          for(i = 0; i < len; i++){
              arrayofnodes[i] = annotations[i];
          }

      return(
        arrayofnodes.map((component,i) => (<MeshItem root={this.props.match.params.id}
         name={component.getElementsByTagName("name")[0].textContent}
         sendToHololens={this.sendToHololens}/>))
      )
    }
    return <div/>;
  }

  renderAnnotations(){
    if(this.state.active === true){
          let annotations = store.getState().getElementsByTagName('annotation')
          let len = annotations.length;
          let arrayofnodes = [];
          let i;
          for(i = 0; i < len; i++){
              arrayofnodes[i] = annotations[i];
          }
          console.log(arrayofnodes)
      return(
        arrayofnodes.map((component,i) => (<AnnotationItem content={component.getElementsByTagName("annotationData")[0].getAttribute("tag")}
                                                          name={component.getElementsByTagName("author")[0].textContent}
                                                          case={this.props.match.params.id}
                                                          id={component.getAttribute("id")}
                                                          deleteSelf={this.deleteSelf}/>))
      )
    }
    return <div/>;
  }

  render() {

    return (

        <div>

        <Breadcrumb>
            <ul>
                <BreadcrumbItem><a href={"/#/"}>Cases</a></BreadcrumbItem>
                <BreadcrumbItem><a>{this.props.match.params.id}</a></BreadcrumbItem>
            </ul>
        </Breadcrumb>


                <Title isSize={4}>Files</Title>
        <br/>
        <Columns>
        <Column hasTextAlign='centered'>
          <Button href={'/#/add/mesh/'+this.props.match.params.id} isColor='success' isOutlined>Upload Mesh</Button>
        </Column>
        <Column hasTextAlign='centered'>
          <Button href={'/#/add/annotation/'+this.props.match.params.id} isColor='success' isOutlined>Add Annotation</Button>
        </Column>
            </Columns>
        <Column hasTextAlign='centered'>
          <Button href={'/#/cases/'+this.props.match.params.id+"/allmeshes"} isColor='success' isOutlined>View All Meshes</Button>
        </Column>


        <ScrollArea
                    speed={0.8}
                    className="area"
                    contentClassName="content"
                    horizontal={false}
                    >


            {
              this.renderMeshes()
            }
            {
                this.renderAnnotations()
            }
          </ScrollArea>

        </div>


    );
  }

  deleteSelf(annotation){
    request
    .delete('/api/annotation/delete')
    .send('caseId='+this.props.match.params.id)
    .send('annotationID='+annotation)
    .end ((error, res)=>{
        if(!error){
              let xmlDoc = store.getState()
              for(let i=0;i<xmlDoc.getElementsByTagName("annotation").length;i++) {
              			if(xmlDoc.getElementsByTagName("annotation")[i].getAttribute("id") === annotation) {

              				let removed = xmlDoc.getElementsByTagName("annotation")[i];
                      console.log(removed)
              				removed.parentNode.removeChild(removed);
              			}
              		}
               store.dispatch({type: 'SWITCH-CASE', values:xmlDoc})
               this.forceUpdate()
        }else{
          console.log(error)
        }
      }
    )


  }

  deleteSelfMesh(annotation){
    request
    .delete(process.env.PUBLIC_URL+'/api/annotation/delete')
    .field({'caseId':this.props.match.params.id})
    .field({'annotationID':annotation})
    .end ((error, res)=>{
        if(!error){
              let xmlDoc = store.getState()
              for(let i=0;i<xmlDoc.getElementsByTagName("annotation").length;i++) {
                    if(xmlDoc.getElementsByTagName("annotation")[i].getAttribute("id") === annotation) {

                      let removed = xmlDoc.getElementsByTagName("annotation")[i];
                      console.log(removed)
                      removed.parentNode.removeChild(removed);
                    }
                  }
               store.dispatch({type: 'SWITCH-CASE', values:xmlDoc})
               this.forceUpdate()
        }else{
          console.log(error)
        }
      }
    )


  }



}

export default CaseView;
