class ScreenMain implements Screen {
  
  Boat boat;

  ComponentMap map;
  ComponentControlPad controlPad;

  ScreenMain() {
    //Karte quadratisch darstellen
    int mapWidth = height;

    map = new ComponentMap(
      new Position(AlignmentX.LEFT, AlignmentY.BOTTOM, 0, 0, mapWidth, height)
    );
    controlPad = new ComponentControlPad(
      new Position(AlignmentX.RIGHT, AlignmentY.BOTTOM, 30, 30, width-mapWidth-60, height-37-60)
    );
    controlPad.speed = 0;
    controlPad.direction = 0;
  }
  
  void draw() {
    //Hintergrund aufhellen
    background(255, 255, 255, 190);
    
    controlPad.draw();
    
    map.speed = controlPad.speed;
    map.direction = controlPad.direction;

    map.draw();
  }

  Screen navigateTo() {
    return null;
  }
  
  void updateBoat(Boat boat) {
    this.boat = boat;
  }

}
