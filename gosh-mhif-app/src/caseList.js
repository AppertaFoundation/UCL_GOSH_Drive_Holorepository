import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Scene from './Scene'
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input} from 'bloomer';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import request from 'superagent';
import PatientCase from './util/PatientCase'
import store from './stores/activeCaseStore'

class CaseList extends Component {

  constructor(props){
    super(props);
    this.state = {cases:[],tag:"All",name:""}
    this.handleClick = this.handleClick.bind(this)
    this.handleForm = this.handleForm.bind(this)
  }
  handleForm(event){
    this.setState({name: event.target.value});
    console.log(this.state.tag)
  }

  handleClick(event){
    event.preventDefault()
    console.log(event.target.childNodes[0].nodeValue)
    this.setState({tag:event.target.childNodes[0].nodeValue})
  }

  componentDidMount(){
    request.get(process.env.PUBLIC_URL+"/api/case/")
        .set('Accept', 'application/json')
        .end ((error, res)=>{
            let jsonObject = JSON.parse(res.text);
            this.setState({cases:jsonObject.cases})
            console.log(this.state)
        })
  }

  render() {
    return (

        <div>
        <div>
      
        <br/>
        </div>
           {this.state.cases.filter((component)=>{
             let isThere = false;
             if(this.state.tag==="All"){
               isThere = true;
             }
             for(let i = 0;i<component.tags.length;i++){
               if(this.state.tag==="All"){
                 isThere = true;
               }
              else if(component.tags[i].includes(this.state.tag)){
                    isThere = true;
               }
             }
             return isThere;
           })
             .map((component, i) => <PatientCase name={component}/>)}
        </div>



    );
  }
}

export default CaseList;
