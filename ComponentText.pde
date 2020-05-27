class ComponentText implements Component {
  
  public static final int ALL_CAPS = 1;
  public static final int NONE = 0;

  private String text;
  int textSize;
  Color textColor;
  Position pos;
  int aligment;
  int textTransform;

  ComponentText(String text, int textSize, Color textColor, Position pos, int aligment, int textTransform) {
    this.text = text;
    this.textSize = textSize;
    this.textColor = textColor;
    this.pos = pos;
    this.aligment = aligment;
    this.textTransform = textTransform;
    textTransform();
  }

  void draw() {
    textSize(this.textSize);
    textAlign(this.aligment, CENTER);
    this.textColor.f();

    text(text, pos.getLeft(), pos.getTop());
  }
  
  void updateText(String newText) {
    text = newText;
    textTransform();
  }
  
  void textTransform() {
    if (textTransform == ALL_CAPS) {
      text = text.toUpperCase();
    }   
  }
  
  public boolean isPressed() {
      return mousePressed && mouseWithinBounds();
    }
    
    private boolean mouseWithinBounds() {
      return mouseX > pos.getLeft() && mouseX < pos.getLeft()+pos.w && mouseY > pos.getTop() && mouseY < pos.getTop()+pos.h;
    }
}
