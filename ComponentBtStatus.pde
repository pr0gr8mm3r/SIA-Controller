/**
* siehe auch Component.pde
* Component, der in der rechten oberen Ecke den aktuellen Status der Verbindung zeigt
**/

class ComponentBtStatus implements Component {

  ComponentRectangle bgRect;

  ComponentText statusText;

  //Initialisierung (entspricht ca. void setup())
  ComponentBtStatus() {
    bgRect = new ComponentRectangle(
                new Colors().backgroundLight(),
                new Position(AlignmentX.RIGHT, AlignmentY.TOP, 0, 0, 370, 37),
                new int[]{0,0,0,35}
            );
    statusText = new ComponentText(
                   "",
                   26,
                   new Colors().textNormalDark(),
                   new Position(AlignmentX.RIGHT, AlignmentY.TOP, 30, 15, 0, 40),
                   RIGHT,
                   ComponentText.NONE
                  );
  }

  void draw() {
    bgRect.draw();
    statusText.draw();
  }

  //Funktion die von main.pde aufgerufen wird, um den Status des Bootes zu aktualisieren
  public void updateBoat(Boat boat) {
    if (boat != null && boat.connected)
      this.statusText.updateText("Verbunden mit \"" + boat.name + "\"");
    else
      this.statusText.updateText("Nicht verbunden");
  }
}
