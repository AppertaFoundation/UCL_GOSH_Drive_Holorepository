import React, { Component } from 'react'
import * as THREE from 'three'
import request from 'superagent';
import {Breadcrumb, BreadcrumbItem} from 'bloomer';
import GLTFLoader from 'three-gltf-loader';

import store from './stores/activeCaseStore'


class Scene extends Component {
  constructor(props) {
    super(props)
    console.log()
    this.start = this.start.bind(this)
    this.stop = this.stop.bind(this)
    this.animate = this.animate.bind(this)
    this.onWindowResize = this.onWindowResize.bind(this)
  }

  componentDidMount() {
    const width = this.mount.clientWidth
    const height = this.mount.clientHeight
    var light;
    console.log(width / height)
    const scene = new THREE.Scene()
    const camera = new THREE.PerspectiveCamera(
      45,
      width / height,
      0.1,
      1000
    )
    scene.background = new THREE.Color( 0xa0a0a0 );
    scene.fog = new THREE.Fog( 0xa0a0a0, 200, 1000 );
    camera.position.z = 20
    camera.position.y = 10





    light = new THREE.HemisphereLight( 0xffffff, 0x444444 );
    				light.position.set( 0, 200, 0 );
    				scene.add( light );

    				light = new THREE.DirectionalLight( 0x999999 );
    				light.position.set( 0, 200, 100 );
    				light.castShadow = true;
    				light.shadow.camera.top = 180;
    				light.shadow.camera.bottom = -100;
    				light.shadow.camera.left = -120;
    				light.shadow.camera.right = 120;
    				scene.add( light );


    const renderer = new THREE.WebGLRenderer({ antialias: true })

    var OrbitControls = require('three-orbit-controls')(THREE)

    let controls = new OrbitControls(camera,renderer.domElement)

    const geometry = new THREE.BoxGeometry(1, 1, 1)
    const material = new THREE.MeshBasicMaterial({ color: '#433F81' })
    const cube = new THREE.Mesh(geometry, material)

    var base64_arraybuffer = require('base64-arraybuffer');
    var FBXLoader = require('three-fbx-loader');


    console.log(store.getState())
    console.log(this.props.match.params)


    let xmlDoc = store.getState();
    if(Object.keys(xmlDoc).length === 0 && xmlDoc.constructor === Object){
        console.warn("No Patient case is loaded")
    }else{
      var x = xmlDoc.getElementsByTagName("mesh");
              for (let i = 0; i < x.length; i++) {
                let y = x[i].getElementsByTagName("rawData");
                let type = x[i].getElementsByTagName("fileType")[0];

                for (let j = 0; j < y.length; j++) {
                  if(type.childNodes[0].nodeValue==="fbx"){
                  if(this.props.match.params.model===undefined||
                    x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue===this.props.match.params.model){
                          var loader = new FBXLoader();
                          console.log(y[j].childNodes[0].nodeValue);
                  let objtest = loader.parse(base64_arraybuffer.decode(y[j].childNodes[0].nodeValue.replace(/\n/g, '').replace(/\r/g, '')),x.baseURL,null,function(error){
                    console.log("Error"+error);
                  });
                   console.log(objtest);
                  objtest.position.y = 10
                  objtest.position.z = 50*i
                  objtest.scale.set(0.001,0.001,0.001);
                  scene.add(objtest);
                }
              }else if(type.childNodes[0].nodeValue==="glb"||type.childNodes[0].nodeValue==="gltf"){
                if(this.props.match.params.model===undefined||
                  x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue===this.props.match.params.model){
                    const loader = new GLTFLoader();

                let objtest = loader.parse(base64_arraybuffer.decode(y[j].childNodes[0].nodeValue.replace(/\n/g, '').replace(/\r/g, '')),x.baseURL,(objtest)=>{
                            console.log(objtest)
                            console.log(objtest.scene)
                            scene.add(objtest.scene);
                })
              }
              }
              }
              }
    }



    renderer.setClearColor('#000000')
    renderer.setSize(this.mount.clientWidth, this.mount.clientHeight)

    this.scene = scene
    this.camera = camera
    this.renderer = renderer
    this.material = material
    this.cube = cube
    this.control = controls
    this.mount.appendChild(this.renderer.domElement)
    window.addEventListener( 'resize', this.onWindowResize, false );
    this.start()
  }

  componentWillUnmount() {
    this.stop()
    this.control.dispose()
    window.removeEventListener( 'resize', this.onWindowResize, false );
    this.mount.removeChild(this.renderer.domElement)
  }

  start() {
    if (!this.frameId) {
      this.frameId = requestAnimationFrame(this.animate)
    }
  }
  onWindowResize() {

				this.camera.aspect = this.mount.clientWidth / this.mount.clientHeight;
				this.camera.updateProjectionMatrix();

				this.renderer.setSize( this.mount.clientWidth, this.mount.clientHeight );

	}

  stop() {
    cancelAnimationFrame(this.frameId)
  }

  animate() {

	  this.control.update();
    this.renderScene()
    this.frameId = window.requestAnimationFrame(this.animate)
  }

  renderScene() {
    this.renderer.render(this.scene, this.camera)
  }

  render() {
    return (
      <div>

      <Breadcrumb>
          <ul>
              <BreadcrumbItem><a href={"/#"}>Cases</a></BreadcrumbItem>
              <BreadcrumbItem><a  href={"/#/cases/"+this.props.match.params.id}>{this.props.match.params.id}</a></BreadcrumbItem>
              <BreadcrumbItem isActive><a>Model</a></BreadcrumbItem>
          </ul>
      </Breadcrumb>


      <div
        style={{ width: '100%', height: '600px' }}
        ref={(mount) => { this.mount = mount }}
      />
      </div>
    )
  }
}

export default Scene
