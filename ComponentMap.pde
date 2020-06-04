class ComponentMap implements Component {
  
  float speed = 0;
  float direction = 0;

    Position pos;

    PImage bgImg;

    PImage boatImg;
    Position boatPos;

    Color arrowColor;

    //BG
    RoundedRectBg bg;
    

    float y;
    float rotation;

    ComponentMap(Position pos) {
        this.pos = pos;
        setupBg();
        setupArrow();
        setupBoat();
    }

    void draw() {
        drawBg();
        drawArrow();
        drawBoat();
    }

    private void setupBg() {
        bg = new RoundedRectBg(pos.getLeft(), pos.getTop(), pos.w, pos.h, 0);
    }
    private void drawBg() {
        bg.draw();
    }

    private void setupArrow() {
        arrowColor = new Colors().overlayLight();
    }
    private void drawArrow() {
        noFill();
        strokeWeight(32);
        arrowColor.s();
        
        int p1x = (pos.w/2)+pos.getLeft()+(boatImg.width/2);
        int p1y = (pos.h/2)+pos.getTop();
        
        int speedFactor = 1;
        if (speed > 0) speedFactor = -1;
        else if (speed == 0) {
          speedFactor = 0;
          direction = 0;
        }
        
        
        int p2x = pos.getLeft()+pos.w/2+round(direction*(pos.w/2))+16;
        int p2y = pos.getTop()+pos.h/2+speedFactor*(pos.h/2+16);
        int cp1x = p1x;
        int cp1y = ((p2y-p1y)/2)+p1y;
        int cp2x = ((p1x-p2x)/2)+p2x;
        int cp2y = p2y;
        
        bezier(p1x, p1y, cp1x, cp1y, cp2x, cp2y, p2x, p2y);
    }

    private void setupBoat() {
        boatImg = loadImage("boot.png");
        boatPos = new Position(AlignmentX.LEFT, AlignmentY.CENTER, (pos.w/2)+pos.getLeft(), 0, boatImg.width, boatImg.height);
    }
    private void drawBoat() {
        image(boatImg, boatPos.getLeft(), boatPos.getTop(), boatPos.w, boatPos.h);
    }
}
