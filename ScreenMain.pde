class ScreenMain implements Screen {
  
  boolean gyroActive = false;
  
  Boat boat;
  
  ComponentButton gyroToggle;
  ComponentButton gyroReset;

  ComponentMap map;
  ComponentControlPad controlPad;

  ScreenMain() {    
    int mapWidth = height;

    map = new ComponentMap(
      new Position(AlignmentX.LEFT, AlignmentY.BOTTOM, 0, 0, mapWidth, height)
    );
    controlPad = new ComponentControlPad(
      new Position(AlignmentX.RIGHT, AlignmentY.BOTTOM, 20, 20, width-mapWidth-40, height-100-60)
    );
    controlPad.speed = 0;
    controlPad.direction = 0;
                        
    gyroToggle = new ComponentButton(
      "Gyroskop: An",
      new Position(AlignmentX.LEFT, AlignmentY.TOP, mapWidth+20, 28+20, (controlPad.pos.w/2)-10, 0)
    );

    gyroReset = new ComponentButton(
      "Null setzten",
      new Position(AlignmentX.LEFT, AlignmentY.TOP, gyroToggle.pos.getLeft() + gyroToggle.pos.w + 20, 28+20, (controlPad.pos.w/2)-10, 0)
    );
  }
  
  void draw() {
    //Hintergrund aufhellen
    background(255, 255, 255, 190);
    
    if(!controlPad.isCapturingCursor() && gyroToggle.isPressed()) {
      if (gyroActive)
        gyroToggle.updateText("Gyroskop: Aus");
      else
        gyroToggle.updateText("Gyroskop: An");
      gyroActive = !gyroActive;
    }

    if (gyroActive) gyroReset.draw();
    
    controlPad.draw();

    gyroToggle.draw();
    
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
