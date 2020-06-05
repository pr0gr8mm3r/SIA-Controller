/**
* siehe auch Component.pde
* Component, mit dem der Nutzer die Richtung und Geschwindigkeit des Bootes bestimmen kann
**/

class ComponentControlPad implements Component {
  
  float speed = 0;
  float direction = 0;

  private float previousDirection = 0;
  private float previousSpeed = 0;
  private float x = 0;
  private float y = 0;

  private AnimatorBounce animator;
  
  private boolean isCapturingCursor = false;
  private boolean mousePressedInFrameBefore = false;

    Position pos;
    
    RoundedRectBg bg;

    //Pfeile um den Steuerknüppel, wenn er noch nicht bewegt wurde
    PImage chevronRing;
    float chevronRingTolerance = 0.05f;

  //Initialisierung (entspricht ca. void setup())
    ComponentControlPad(Position pos) {
        this.pos = pos;
            
      bg = new RoundedRectBg(pos.getLeft(), pos.getTop(), pos.w, pos.h, 30);
        chevronRing = loadImage("chevron-ring.png");

        animator = new AnimatorBounce(200);
    }

    void draw() {
        bg.draw();
        
        if (isCapturingCursor) {

          animator.reset();
          
          //X
          if (mouseX > pos.getLeft()) {
            
            if (mouseX < pos.getLeft()+pos.w) {
              direction = (float)(mouseX-pos.getLeft()-pos.w/2)/(pos.w/2);
            } else {
              direction = 1;
            }
            
          } else {
              direction = -1;       
          }

          x = direction;
          previousDirection = direction;
          
          //Y
          if (mouseY > pos.getTop()) {
            
            if (mouseY < pos.getTop()+pos.h) {
              speed = (float)-(mouseY-pos.getTop()-pos.h/2)/(pos.h/2);
            } else {
              speed = -1;
            }
            
          } else {
            speed = 1;
          }

          y = speed;
          previousSpeed = speed;
          
        } else {
          speed = 0;
          direction = 0;

          //Steuerknüppel zurück zum Ausgang animieren
          if (!animator.isRunning()) {
            if (animator.ranBefore) {
              x = 0;
              y = 0;
            }
            animator.start();
          } else {
            float animatorValue = animator.getValue();
            x = (1 - animatorValue) * previousDirection; 
            y = (1 - animatorValue) * previousSpeed;
          }
        }
        
        checkCursorCapture();
        
        new Colors().overlayLight().f();
        new Colors().backgroundLight().s();
        strokeWeight(7);
        ellipse(pos.getLeft()+(x+1)*(pos.w/2), pos.getTop()+(-y+1)*(pos.h/2), 70, 70);
        
        //4-Weg Pfeil zeichnen, wenn sich der Knüppel im richtigen Bereich (ca. der Mitte) befindet
        if (chevronRingTolerance > y && y > -chevronRingTolerance && chevronRingTolerance > x && x > -chevronRingTolerance)
          image(chevronRing, pos.getLeft()+pos.w/2 - chevronRing.width/2, pos.getTop()+pos.h/2 - chevronRing.height/2);
          
        mousePressedInFrameBefore = mousePressed;
    }
    
    public boolean isCapturingCursor() {
      return isCapturingCursor;
    }
    
    private void checkCursorCapture() {
      if (mousePressed) {
        if (!mousePressedInFrameBefore && mouseX > pos.getLeft() && mouseX < pos.getLeft()+pos.w && mouseY > pos.getTop() && mouseY < pos.getTop()+pos.h) {
          isCapturingCursor = true;
        }
      } else {
        isCapturingCursor = false;
      }
    }
}