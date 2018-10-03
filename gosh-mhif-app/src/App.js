import React, { Component } from 'react';
import logo from './logo.svg';
import Scene from './Scene'
import About from './About'
import { Container, Box ,Column,Columns,Breadcrumb, BreadcrumbItem} from 'bloomer';
import AppNavBar from './app/AppNavBar'
import AppSidePanel from './app/AppSidePanel'
import CaseList from './caseList'
import HomeScreen from './HomeScreen'
import CaseView from './CaseView'
import AddAnnotation from './AddAnnotation'
import AddMesh from './AddMesh'
import ApiDocumentation from './ApiDocumentation'
import Add from './Add'
import { HashRouter as Router, Route, Link,Switch
 } from "react-router-dom";

class App extends Component {
//Legacy Layout components
  // <AppNavBar/>
  // <br/>
  // <Columns isCentered>
  // <Column isSize='1/3'>
  //   <AppSidePanel/>
  //   </Column>

  constructor(props){
    super(props);

  }

  render() {
    return (
      <div className="App">
        <div>
        <AppNavBar/>
        <br/>
        <Columns isCentered>

          <Column>
          <Router basename={process.env.REACT_APP_ROUTER_BASE+"" || ''}>
              <Switch>
                <Route path="/add/mesh/:id" component={AddMesh} />
                <Route path="/add/annotation/:id" component={AddAnnotation} />
                <Route path="/about" component={About} />
                <Route path="/add" component={Add} />
                <Route path="/cases/:id/mesh/:model" component={Scene} />
                <Route path="/cases/:id/allmeshes/" component={Scene} />
                <Route path="/cases/:id" component={CaseView} />
                <Route path="/documentation/" component={ApiDocumentation} />
                <Route path="/all" component={CaseList} />
                <Route path="/" component={HomeScreen} />
              </Switch>
            </Router>
            </Column>
          </Columns>
        </div>



      </div>
    );
  }
}

export default App;
