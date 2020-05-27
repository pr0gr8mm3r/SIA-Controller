class RoundedRectBg {

    int x;
    int y;
    int w;
    int h;
    float r;

    Colors colors;

    public RoundedRectBg(int x, int y, int w, int h, float r) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        
        this.colors = new Colors();
    }

    void draw() {

        //Runde Ecken, haha
        beginShape();
        //Trick, damit die Bögen richtig gezeichnet werden
        stroke(50);
        strokeWeight(0);

        //LINKER TEIL
        colors.backgroundColored().f();
        arc(x+r, y+h-r, r*2, r*2, HALF_PI, PI, CHORD);
        arc(x+r, y+r, r*2, r*2, PI, PI+HALF_PI, CHORD);
        
        //RECHTER TEIL
        colors.backgroundColoredDarker().f();
        arc(x+w-r, y+r, r*2, r*2, -HALF_PI, 0, CHORD);
        arc(x+w-r, y+h-r, r*2, r*2, 0, HALF_PI, CHORD);        

        endShape(CLOSE);



        //Fill in der Mitte
        beginShape();

        //LINKER TEIL
        colors.backgroundColored().f();
        //Unten, linker Punkt
        vertex(x+r, y+h);
        //Links, unterer Punkt
        vertex(x, y+h-r);
        //Links, oberer Punkt
        vertex(x, y+r);
        //Oben, linker Punkt
        vertex(x+r, y);

        //RECHTER TEIL
        colors.backgroundColoredDarker().f();
        //Oben, rechter Punkt
        vertex(x+w-r, y);
        //Rechts, oberer Punkt
        vertex(x+w, y+r);
        //Rechts, unterer Punkt
        vertex(x+w, y+h-r);
        //Unten, rechter Punkt
        vertex(x+w-r, y+h);

        endShape(CLOSE);
    }
}
