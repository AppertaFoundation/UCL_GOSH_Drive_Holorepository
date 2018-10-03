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

class HomeScreen extends Component {

  constructor(props){
    super(props);
    this.state = {cases:[],tag:"",name:""}
    this.handleClick = this.handleClick.bind(this)
    this.handleNameForm = this.handleNameForm.bind(this)
    this.handleTagForm = this.handleTagForm.bind(this)
  }

  
  handleNameForm(event){
    this.setState({name: event.target.value});
  }
    handleTagForm(event){
            this.setState({tag: event.target.value});
    }

  handleClick(event){
            event.preventDefault()
            request.get(process.env.PUBLIC_URL+"/api/case/?tags="+this.state.tag+"&name="+this.state.name)
                .set('Accept', 'application/json')
                .end ((error, res)=>{
                    let jsonObject = JSON.parse(res.text);
                    this.setState({cases:jsonObject.cases})
                    console.log(this.state)
                })
  }

  componentDidMount(){
  }

  render() {
    return (

        <div>
        <Content>
          <h1>GOSH DRIVE HOLO REPOSITORY</h1>


      <Columns isCentered>
          <Column hasTextAlign='centered'>
            <Field hasAddons>
                <Control>
                    <Input placeholder='Find a Case by Name ' onChange={this.handleNameForm} />
                </Control>
            </Field>
            <Field hasAddons>
                <Control>
                    <Input placeholder='Find a Case by Tag' onChange={this.handleTagForm} />
                </Control>
            </Field>
            <Control>
                <Button onClick={this.handleClick}>Search</Button>
            </Control>
            <Control>
            <Button href={'/#/all'} isColor='success' isOutlined>View All</Button>
            </Control>
            </Column>
        </Columns>
             </Content>
           {this.state.cases.map((component, i) => <PatientCase name={component}/>)}
        </div>



    );
  }
}

export default HomeScreen;
