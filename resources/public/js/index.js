function creationMyth() {
  var mainColor = "#ff5252";
  var canvasHeight = window.innerHeight;
  var canvasWidth = window.innerWidth;
  var loader = new THREE.TextureLoader();

  var scene = new THREE.Scene();
  scene.fog = new THREE.FogExp2("#fff", 0.0011);

  var camera = new THREE.PerspectiveCamera(20, canvasWidth / canvasHeight, 0.1, 1000);
  camera.lookAt(new THREE.Vector3(0, 100, 0));
  camera.position.set(0, 50, 400);

  var spotLight = new THREE.SpotLight(0xffffff);
  spotLight.position.set(0, 300, 300);
  spotLight.intensity = 1.3;
  spotLight.castShadow = true;
  scene.add(spotLight);

  var controls = new THREE.OrbitControls(camera);
  controls.damping = 0.2;
  controls.enabled = false;
  controls.maxPolarAngle = Math.PI / 2;
  controls.minPolarAngle = 1;
  controls.minDistance = 300;
  controls.maxDistance = 500;

  var renderer = new THREE.WebGLRenderer({
    alpha: true
  });
  renderer.setSize(canvasWidth, canvasHeight * 1.29);
  renderer.shadowMap.enabled = true;
  renderer.shadowMap.type = THREE.PCFSoftShadowMap;
  renderer.setClearColor("#fff", 1);
  $('.background').append(renderer.domElement);

  window.onresize = function() {
    var canvasHeight = window.innerHeight;
    var canvasWidth = window.innerWidth;
    camera.aspect = canvasWidth / canvasHeight;
  }

  function genesisDevice() {

    this.geometry = new THREE.PlaneGeometry(canvasWidth * 2, canvasHeight * 2, 128, 128);

    this.material = new THREE.MeshLambertMaterial({
      color: mainColor
    });

    this.inception = function() {
      //plot terrain vertices
      for (var i = 0; i < this.geometry.vertices.length; i++) {
        if (i % 2 === 0 || i % 5 === 0 || i % 7 === 0) {
          var num = Math.floor(Math.random() * (30 - 20 + 1)) + 20;
          this.geometry.vertices[i].z = Math.random() * num;
        }
      }
      //define terrain model
      this.terrain = new THREE.Mesh(this.geometry, this.material);

      this.terrain.rotation.x = -Math.PI / 2;
      this.terrain.position.y = +10;

      this.terrain.recieveShadow = true;
      this.terrain.castShadow = true;

      scene.add(this.terrain);
      return this;
    }

    this.inception();
  }

  //sky cube
  var skyGeometry = new THREE.CubeGeometry(1024, 1024, 1024);
  var skyArray = [];
  for (var i = 0; i < 6; i++) {
    skyArray.push(new THREE.MeshBasicMaterial({
      side: THREE.BackSide
    }));
  }
  var skyMaterial = new THREE.MeshFaceMaterial(skyArray);
  var skyBox = new THREE.Mesh(skyGeometry, skyMaterial);
  scene.add(skyBox)

  var terrain = genesisDevice();

  var render = function() {
      requestAnimationFrame(render);
      animation();
      renderer.render(scene, camera);
    }
    //animations
  function animation() {
     scene.rotation.y -= .0005;
  }

  render();
}

creationMyth();
