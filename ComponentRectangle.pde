class ComponentRectangle implements Component {

    Color bgColor;
    Position pos;
    int[] radii;

    ComponentRectangle(Color bgColor, Position pos, int[] radii) {
        this.bgColor = bgColor;
        this.pos = pos;
        this.radii = radii;
    }

    void draw() {
        noStroke();
        this.bgColor.f();
        rect(this.pos.getLeft(), this.pos.getTop(), this.pos.w, this.pos.h, radii[0], radii[1], radii[2], radii[3]);
    }
}