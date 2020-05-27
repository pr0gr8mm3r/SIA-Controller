class Position {
    public AlignmentX alignmentX;
    public AlignmentY alignmentY;
    public int distX;
    public int distY;
    public int w;
    public int h;

    Position(AlignmentX alignmentX, AlignmentY alignmentY, int distX, int distY, int w, int h) {
        this.alignmentX = alignmentX;
        this.alignmentY = alignmentY;
        this.distX = distX;
        this.distY = distY;
        this.w = w;
        this.h = h;
    }

    int getTop() {
        switch(alignmentY) {
            case TOP:
                return distY;
            case CENTER:
                return (height/2) - (h/2) + distY;
            case BOTTOM:
                return height - h - distY;
            default:
                return 0;
        }
    }

    int getLeft() {
        switch(alignmentX) {
            case LEFT:
                return distX;
            case CENTER:
                return (width/2) - (w/2) + distX;
            case RIGHT:
                return width - w - distX;
            default:
                return 0;
        }
    }
}

enum AlignmentX {
    LEFT, CENTER, RIGHT
}
enum AlignmentY {
    TOP, CENTER, BOTTOM
}
