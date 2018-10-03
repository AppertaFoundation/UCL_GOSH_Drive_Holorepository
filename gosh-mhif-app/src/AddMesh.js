import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Scene from './Scene'
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input,Label} from 'bloomer';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import request from 'superagent';
import PatientCase from './util/PatientCase'


import store from './stores/activeCaseStore'

class AddMesh extends Component {

  constructor(props){
    super(props);
    this.state = {active:false}
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  componentDidMount(){

  }
  handleSubmit(event) {
    event.preventDefault();

    request
    .post(process.env.PUBLIC_URL+'/api/case/'+this.props.match.params.id+'/mesh')
    .attach('model',event.target[0].files[0])
    .end ((error, res)=>{
        if(!error){
            this.props.history.push('/cases/'+this.props.match.params.id);
        }else{
          console.log(error)
        }
      }
    )

  }

  render() {
    return (

        <div>
        <Breadcrumb>
            <ul>
        <BreadcrumbItem><a href={process.env.REACT_APP_ROUTER_BASE+"/#/"}>Cases</a></BreadcrumbItem>
        <BreadcrumbItem><a>new</a></BreadcrumbItem>
            </ul>
        </Breadcrumb>
        <Box>
          <form onSubmit={this.handleSubmit}>
          <Field>
            <Label>Organ mesh</Label>
            <Control>
                <Input type="file" placeholder='Test' />
            </Control>
            </Field>
             <Button type="submit" isColor='success' isOutlined>Create</Button>
            </form>
          </Box>

        </div>


    );
  }

}

export default AddMesh;
