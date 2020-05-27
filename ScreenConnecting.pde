class ScreenConnecting implements Screen {
  
  boolean connected = false;

  ComponentText text;
  
  ComponentProgressIndicator pgIndicator;
  
  ScreenConnecting() {
    text = new ComponentText(
                          "Verbinde...",
                          28,
                          new Colors().textNormalLight(),
                          new Position(AlignmentX.CENTER, AlignmentY.CENTER, 0, 0, 0, 0),
                          CENTER,
        ComponentText.ALL_CAPS
                        );
    pgIndicator = new ComponentProgressIndicator(
                    new Colors().backgroundLight(),
                    new Position(AlignmentX.CENTER, AlignmentY.CENTER, 0, 0, 0, 0),
                    300
    );
  }
  
  void draw() {
    text.draw();
    pgIndicator.draw();
  }
  
  Screen navigateTo() {
    if (connected)
      return new ScreenMain();
      //return null;
    else
      return null;
  }
  
  void updateBoat(Boat boat) {
    connected = boat.connected;
  }
  
}
