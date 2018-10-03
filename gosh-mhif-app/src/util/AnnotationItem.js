import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input,Media,MediaLeft,MediaContent,Content,Image,Level,
        Icon, MediaRight,Delete} from 'bloomer';
import request from 'superagent';

class AnnotationItem extends Component {

  constructor(props){
    super(props);
  }




  render() {
    return (
      <div>
      <Box>
        <Media>
          <MediaLeft>
              
          </MediaLeft>
          <MediaContent>
              <Content>
                  <p>
                      <strong>{this.props.name}</strong>
                      <br />
                  </p>
                  <p>
                      {this.props.content}
                  </p>
              </Content>
              <Level isMobile>
                  <LevelLeft>
                  </LevelLeft>
              </Level>
          </MediaContent>
          <MediaRight>
          <button onClick={()=> this.props.deleteSelf(this.props.id)}  type="button" class="delete"></button>
          </MediaRight>

          </Media>
              </Box>
      </div>
    );
  }
}

export default AnnotationItem;
