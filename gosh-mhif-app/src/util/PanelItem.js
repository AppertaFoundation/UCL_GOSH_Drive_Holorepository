import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { Container,PanelBlock, Box ,Column,Columns,Breadcrumb, BreadcrumbItem,LevelItem,
        Field,LevelLeft,Subtitle,Control,LevelRight,Button,
        Input,Media,MediaLeft,MediaContent,Content,Image,Level,
        Icon, MediaRight,Delete} from 'bloomer';


class PanelItem extends Component {

  constructor(props){
    super(props);

  }

  render() {
    return (
        <PanelBlock>
        <Media>
          <MediaLeft>
              <Image isSize='64x64' src='https://via.placeholder.com/128x128' />
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
                  </LevelLeft>
              </Level>
          </MediaContent>
          <MediaRight><Delete /></MediaRight>
          </Media>
            </PanelBlock>

    );
  }
}

export default PanelItem;
