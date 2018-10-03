import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Scene from './Scene'
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input,Content} from 'bloomer';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import request from 'superagent';
import PatientCase from './util/PatientCase'
import store from './stores/activeCaseStore'
import { Markdown } from 'react-showdown';



class About extends Component {

  constructor(props){
    super(props);
    this.state = {markdown:""};
  }



  componentDidMount(){
   request.get(process.env.PUBLIC_URL+"/Readme.md")
        .set('Accept', 'text/plain')
        .end ((error, res)=>{
            this.setState({markdown:res.text})
            console.log(this.state.markdown)
        })
  }

  render() {
  const ReactMarkdown = require('react-markdown')
    return (
        <div>
            <Columns isCentered>
                <Column>
                    <Content>
                     <Markdown markup={ this.state.markdown } />
                     </Content>
                </Column>
            </Columns>
        </div>



    );
  }
}

export default About;
