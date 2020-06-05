/**
* Klasse, die eine Farbe darstellt (per rot, grün, blau und alpha) und nützliche Zeichen-Funktionen enthält
**/

class Color {
    public float r;
    public float g;
    public float b;
    public float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    void f() {
        fill(this.r, this.g, this.b, this.a);
    }

    void s() {
        stroke(this.r, this.g, this.b, this.a);
    }
}
