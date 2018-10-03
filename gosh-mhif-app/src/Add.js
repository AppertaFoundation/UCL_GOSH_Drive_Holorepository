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
import { WithContext as ReactTags } from 'react-tag-input';
import store from './stores/activeCaseStore'

const KeyCodes = {
  comma: 188,
  enter: 13,
};

const delimiters = [KeyCodes.comma, KeyCodes.enter];



class Add extends Component {

  constructor(props){
    super(props);
    this.state = {active:false,
      tags: [
            ],
           suggestions: [
               { id: 'Segmentation', text: 'Segmentation' },
               { id: 'GP', text: 'GP' },
               { id: 'Anatomy', text: 'Anatomy' },
               { id: 'Radiology', text: 'Radiology' },
            ]

    }
    this.handleDelete = this.handleDelete.bind(this);
        this.handleAddition = this.handleAddition.bind(this);
        this.handleDrag = this.handleDrag.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleDelete(i) {
          const { tags } = this.state;
          this.setState({
           tags: tags.filter((tag, index) => index !== i),
          });
      }

      handleAddition(tag) {
          this.setState(state => ({ tags: [...state.tags, tag] }));
      }

      handleDrag(tag, currPos, newPos) {
          const tags = [...this.state.tags];
          const newTags = tags.slice();

          newTags.splice(currPos, 1);
          newTags.splice(newPos, 0, tag);

          // re-render
          this.setState({ tags: newTags });
      }


  componentDidMount(){

  }
  handleSubmit(event) {
    console.log(event.target[3].files[0])
    event.preventDefault();

    var arr = []

    for(let i = 0;i<this.state.tags.length;i++){
      arr.push(this.state.tags[i].id);
    }

    request
    .post(process.env.PUBLIC_URL+'/api/case/create')
    .attach('model', event.target[3].files[0])
    .field('patientInfo', JSON.stringify({patientId:event.target[1].value,createdBy:event.target[2].value}))
    .field('patientId', event.target[0].value)
    .field('tags',arr)
    .end ((error, res)=>{
      if(!error){
          this.props.history.push('/');
      }else{
        console.log(error)
      }
      }
    )

  }

  render() {
     const { tags, suggestions } = this.state;
    return (

        <div>
        <Breadcrumb>
            <ul>
        <BreadcrumbItem><a href={process.env.REACT_APP_ROUTER_BASE+"/#"}>Cases</a></BreadcrumbItem>
        <BreadcrumbItem><a>new</a></BreadcrumbItem>
            </ul>
        </Breadcrumb>
        <Box>
          {//TODO add validation for patient ID
          }
          <form onSubmit={this.handleSubmit}>
          <Field>
            <Label>Patient ID</Label>
            <Control>
                <Input type="text" placeholder='ID' />
            </Control>
          </Field>
          <Field>
            <Label>Patient Name</Label>
            <Control>
                <Input type="text" placeholder='Patient Name' />
            </Control>
          </Field>
          <Field>
            <Label>Doctor Name</Label>
            <Control>
                <Input type="text" placeholder='Doctor Name' />
            </Control>
          </Field>
          <Field>
            <Label>Optional Mesh</Label>
            <Control>
                <Input type="file" placeholder='Test' />
            </Control>
            </Field>
            <Label>Tags</Label>
           <ReactTags tags={tags}
                    suggestions={suggestions}
                    handleDelete={this.handleDelete}
                    handleAddition={this.handleAddition}
                    handleDrag={this.handleDrag}
                    delimiters={delimiters} />

           <Button type="submit" isColor='success' isOutlined>Create</Button>

          </form>
          </Box>

        </div>


    );
  }

}

export default Add;
