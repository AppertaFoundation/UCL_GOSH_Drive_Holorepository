import React, { Component } from 'react';
import { Navbar, NavbarItem ,NavbarBrand,
         NavbarBurger,NavbarDropdown,NavbarDivider,
         Icon, Field, Button,
         Control, NavbarEnd, NavbarLink,
       NavbarStart, NavbarMenu} from 'bloomer';


class AppNavBar extends Component {



constructor(props) {
  super(props);

  this.state = {isActive:false };
  this.onClickNav = this.onClickNav.bind(this);
}

  onClickNav(){
    this.setState({isActive:!this.state.isActive})
  }


  render() {
    return (
      <Navbar style={{ border: 'solid 1px #00D1B2', margin: '0' }}>
  <NavbarBrand>
      <NavbarItem>
          <img src={'https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg'} style={{ marginRight: 5 }} /> GOSH DRIVE HOLOREPOSITORY
      </NavbarItem>

      <NavbarBurger isActive={this.state.isActive} onClick={this.onClickNav} />
  </NavbarBrand>
  <NavbarMenu isActive={this.state.isActive} onClick={this.onClickNav}>
      <NavbarStart>
          <NavbarItem href='#/'>Home</NavbarItem>
          <NavbarItem hasDropdown isHoverable>
              <NavbarLink href='#/documentation'>Documentation</NavbarLink>
          </NavbarItem>
      </NavbarStart>
      <NavbarEnd>
        <NavbarItem href='#/about'>About</NavbarItem>
      </NavbarEnd>
  </NavbarMenu>
  </Navbar>
    );
  }
}

export default AppNavBar;
