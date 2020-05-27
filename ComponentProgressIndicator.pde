class ComponentProgressIndicator implements Component {

    Color bgColor;
    Position pos;
    int radius;

    private int currentTime = 0;

    ComponentProgressIndicator(Color bgColor, Position pos, int radius) {
        this.bgColor = bgColor;
        this.pos = pos;
        this.radius = radius;
    }

    void draw() {
        float currentRadius = ((float)Math.sqrt(currentTime * currentTime)*3) + (radius/2);
        float currentOpacityFactor = 1 - (currentRadius / radius);

        noStroke();
        fill(bgColor.r, bgColor.g, bgColor.b, bgColor.a * currentOpacityFactor);

        ellipse(this.pos.getLeft(), this.pos.getTop(), currentRadius, currentRadius);

        if (currentRadius < radius)
            currentTime += 1;
        else
            currentTime = 0;
    }
}