import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input,Media,MediaLeft,MediaContent,Content,Image,Level,
        Icon, MediaRight,Delete} from 'bloomer';


class MeshItem extends Component {

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
              </Content>
              <Level isMobile>
                  <LevelLeft>
                      <LevelItem href={"#/cases/"+this.props.root+"/mesh/"+this.props.name}>
                          <Icon className="fa fa-pencil" isSize='small'><span  aria-hidden="true" /></Icon>
                      </LevelItem>
                        <Button onClick={()=>this.props.sendToHololens(this.props.root,this.props.name)} isColor='success' isOutlined>Beam To Hololens</Button>
                  </LevelLeft>
              </Level>
          </MediaContent>
          <MediaRight><Delete /></MediaRight>
          </Media>
              </Box>
          <br/>
      </div>
    );
  }
}

export default MeshItem;
