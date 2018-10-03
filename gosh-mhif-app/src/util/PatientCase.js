import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input,Media,MediaLeft,MediaContent,Content,Image,Level,
        Icon, MediaRight,Delete} from 'bloomer';


class PatientCase extends Component {

  constructor(props){
    super(props);

  }


  render() {
    return (
      <div>
      <Box>
        <Media>
          <MediaLeft>
              <Image isSize='64x64' src='https://via.placeholder.com/128x128' />
          </MediaLeft>
          <MediaContent>
              <Content>
                  <p>
                      <strong>{this.props.name.case}</strong>
                      <br />

                  </p>
              </Content>
              <Level isMobile>
                  <LevelLeft>
                      <LevelItem href={"#/cases/"+this.props.name.case}>
                          <Icon className="fa fa-pencil" isSize='small'><span  aria-hidden="true" /></Icon>
                      </LevelItem>
                        <Content>
                        <p>Tags:</p>
                      {this.props.name.tags.map((component,i)=>{
                        return(
                         <p>
                            <strong>{component}</strong>
                        </p>)})}
                          </Content>
                  </LevelLeft>
              </Level>
          </MediaContent>
          <MediaRight><Delete /></MediaRight>
          </Media>
              </Box>
                  <br />
      </div>
    );
  }
}

export default PatientCase;
