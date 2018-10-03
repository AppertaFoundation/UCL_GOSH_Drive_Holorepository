import React, { Component } from 'react';
import {Panel,PanelHeading,PanelBlock,
        Control,Icon,Input,PanelTabs,
         PanelTab,PanelIcon,Checkbox,Button} from 'bloomer';
import store from '../stores/activeCaseStore'
import AnnotationItem from '../util/AnnotationItem'
import PanelItem from '../util/PanelItem'
import request from 'superagent';


class AppSidePanel extends Component {



constructor(props) {
  super(props);

  this.state = {isActive:false,active:store.getState(),selected:"All",value:''};
  this.onClickNav = this.onClickNav.bind(this);
  this.handleChange = this.handleChange.bind(this);
  this.handleClick = this.handleClick.bind(this);
  this.handleForm = this.handleForm.bind(this);
  this.deleteSelf = this.deleteSelf.bind(this)
  store.subscribe(this.handleChange)
}
  handleClick(event){
    event.preventDefault()
    this.setState({selected:event.target.childNodes[0].nodeValue})
  }

  renderAnnotations(){
          let annotations = store.getState().getElementsByTagName('annotation')
          let len = annotations.length;
          let arrayofnodes = [];
          let i;
          for(i = 0; i < len; i++){
              arrayofnodes[i] = annotations[i];
          }
          console.log(arrayofnodes)
      return(
        arrayofnodes.filter((component)=>{
          return(component.getElementsByTagName("author")[0].textContent.includes(this.state.value) || this.state.value==='')
        }).map((component,i) => <AnnotationItem content={component.getElementsByTagName("annotationData")[0].getAttribute("tag")}
                                                          name={component.getElementsByTagName("author")[0].textContent}
                                                          id={component.getAttribute("id")}
                                                          deleteSelf={this.deleteSelf}/>)
      )
  }



  renderMeshes(){
    let annotations = store.getState().getElementsByTagName('mesh')
    let len = annotations.length;
    let arrayofnodes = [];
    let i;
    for(i = 0; i < len; i++){
        arrayofnodes[i] = annotations[i];
        console.log(store.getState().childNodes[0].childNodes[1].getAttribute("id"))
    }
    return(
      arrayofnodes.filter((component)=>{
        return(component.getElementsByTagName("name")[0].textContent.includes(this.state.value) || this.state.value==='')
      }).map((component,i) => <PanelItem root={store.getState().childNodes[0].childNodes[1].getAttribute("id")} name={component.getElementsByTagName("name")[0].textContent}/>)
    )
  }

renderCase(){
  if(this.state.isActive === true){
    if(this.state.selected==="Meshes"){
      return this.renderMeshes();
    }else if(this.state.selected==="Annotations"){
      return this.renderAnnotations();
    }

    else{
      return(<div>
        {this.renderMeshes()}
        {this.renderAnnotations()}
        </div>
      )
    }
  }
  return null;

}

  onClickNav(){
    this.setState({isActive:!this.state.isActive})
  }
  handleForm(event){
    this.setState({value: event.target.value});
    console.log(this.state.value)
  }
  handleChange() {
        console.log(store.getState().childNodes)
        let xmlDoc = store.getState();
        if(Object.keys(xmlDoc).length === 0 && xmlDoc.constructor === Object){
            this.setState({isActive:false})
        }else{
        this.setState({isActive:true})
        this.setState({active:(store.getState().childNodes)})
        }
  }

  render() {

    let xml;
    xml = <div>{JSON.stringify((this.state.active), null, 2) }</div>

    return (
      <Panel>
      <PanelHeading>Patient Case View</PanelHeading>
      <PanelBlock>
          <Control hasIcons='left'>
              <Input isSize='small' placeholder='Search' onChange={this.handleForm}/>
              <Icon isSize='small' isAlign='left'>
                  <span className='fa fa-search' aria-hidden='true' />
              </Icon>
          </Control>
      </PanelBlock>
      <PanelTabs>
          <PanelTab onClick={this.handleClick} isActive={this.state.selected==="All"} tag="a">All</PanelTab>
          <PanelTab onClick={this.handleClick} isActive={this.state.selected==="Meshes"} tag="a">Meshes</PanelTab>
          <PanelTab onClick={this.handleClick} isActive={this.state.selected==="DICOMs"} tag="a">DICOMs</PanelTab>
          <PanelTab onClick={this.handleClick} isActive={this.state.selected==="Annotations"} tag="a">Annotations</PanelTab>
      </PanelTabs>

            {this.renderCase()}

      <PanelBlock>
          <Button isOutlined isFullWidth isColor='primary'>Reset all filters</Button>
      </PanelBlock>
      </Panel>
    );
  }

  deleteSelf(annotation){
    request
    .delete(process.env.PUBLIC_URL+'/api/annotation/delete')
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
}

export default AppSidePanel;
