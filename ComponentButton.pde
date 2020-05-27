class ComponentButton implements Component {
  
  Position pos;
  
  private boolean isPressedInFrameBefore;
  
  ComponentText textComp;
  ComponentRectangle bgRect;

  RoundedRectBg bg;

  ComponentButton(String text, Position pos) {
    this.pos = pos;
    this.pos.h = 70;
    
    textComp = new ComponentText(
      text,
      26,
      new Colors().textHighlightLight(),
      new Position(AlignmentX.LEFT, AlignmentY.TOP, pos.getLeft()+pos.w/2, pos.getTop()+round(pos.h/2.3), pos.w, pos.h),
      CENTER,
      ComponentText.ALL_CAPS
    );

    bg = new RoundedRectBg(pos.getLeft(), pos.getTop(), pos.w, pos.h, pos.h/2);
  }

  void draw() {
    if (mousePressed) {
      isPressedInFrameBefore = mouseWithinBounds();
    } else isPressedInFrameBefore = false;

    bg.draw();
    textComp.draw();
  }
  
  void updateText(String text) {
    textComp.updateText(text);
  }
  
  public boolean isPressed() {
    return mousePressed && !isPressedInFrameBefore && mouseWithinBounds();
  }
  
  private boolean mouseWithinBounds() {
    return mouseX > pos.getLeft() && mouseX < pos.getLeft()+pos.w && mouseY > pos.getTop() && mouseY < pos.getTop()+pos.h;
  }
}
