package de.sia.wazynski.simon.siacontroller;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.os.Bundle; 
import android.os.Handler; 
import android.content.Intent; 
import android.widget.Toast; 
import android.view.Gravity; 
import android.app.Activity; 
import ketai.net.bluetooth.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class main extends PApplet {








Screen currentScreen;
ComponentBtStatus btStatusComponent;

PImage bgImage;
PFont robotoMedium;

Boat currentBoat;

BtManager btManager;

final static Boolean demoMode = false;

public void setup() {
    
    

    bgImage = loadImage("wasser.jpg");

    robotoMedium = createFont("Roboto-Medium.ttf", 32);
    textFont(robotoMedium);

    if (!demoMode) currentScreen = new ScreenConnecting();
    else currentScreen = new ScreenMain();
    btStatusComponent = new ComponentBtStatus();
}


public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!demoMode) {
        btManager = new BtManager(new KetaiBluetooth(this));
        launchDeviceSelectActivity();
    }
}

public void launchDeviceSelectActivity() {
    Intent intent = new Intent(this.getActivity(), BluetoothActivity.class);
    getActivity().startActivityForResult(intent, 2);
}

public void draw() {

    if (btManager.isConnected()) {
        if (!currentBoat.connected) currentBoat.setConnected(true);
        if (currentScreen instanceof ScreenConnecting) changeScreen(new ScreenMain());
    } else {
        if (currentBoat.connected) currentBoat.setConnected(false);
        if (currentScreen instanceof ScreenMain) {
            changeScreen(new ScreenConnecting());
            launchDeviceSelectActivity();
        }
    }

    clear();
    //Hintergrundbild zeichnen
    image(bgImage, 0, 0, width, height);
    //Aktuellen Bildschirm zeichnen
    currentScreen.draw();
    //Bluetooth-Statusleiste zeichnen
    btStatusComponent.updateBoat(currentBoat);
    btStatusComponent.draw();
}

public void changeScreen(Screen nextScreen) {
    currentScreen = nextScreen;
    currentScreen.updateBoat(currentBoat);
}

public void onBluetoothDataEvent(String who, byte[] data) {
  btManager.onBluetoothDataEvent(who, data);
}

public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (btManager != null) btManager.onActivityResult(requestCode, resultCode, data);
    //Berechtigung nicht gewährt
    if (requestCode == 1 && resultCode == 0) {
        CharSequence message = "Bluetooth wird ben\u00F6tigt, um das Boot zu steuern. Bitte zulassen.";
        Toast toast = Toast.makeText(this.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
        toast.show();
    } else if (requestCode == 2) {
        if (resultCode == Activity.RESULT_OK) {
            String address = data.getStringExtra(BluetoothActivity.EXTRA_DEVICE_ADDRESS);
            if (address != null && btManager != null) {
                currentBoat = new Boat(data.getStringExtra(BluetoothActivity.EXTRA_DEVICE_NAME), address, false);
                btManager.connect(address);
            }
        } else {
            getActivity().finish();
        }
    }
}
abstract class Animator {

	public Animator(int duration) {
		this.duration = duration;
	}

	int startTime;
	int duration;
  	boolean ranBefore = false;
	public boolean isRunning() {
		return startTime+duration >= millis() && startTime <= millis();
	}

	public abstract float getValue();

	public void start() {
		if (!ranBefore) {
			startTime = millis();
			ranBefore = true;
		}
	}

	public void reset() {
		ranBefore = false;
	}
}
class AnimatorBounce extends Animator {

	public AnimatorBounce(int duration) {
		super(duration);
	}

	public float getValue() {
		if (isRunning()) {
			float timeFraction = (millis() - PApplet.parseFloat(startTime)) / duration;
			return - timeFraction * (timeFraction - 2);
		} else return 0.0f;
	}
}
class Boat {
  
  String name;
  String btAddress;
  public boolean connected;
  
  Boat(String name, String btAddress, boolean connected) { 
    this.name = name;
    this.btAddress = btAddress;
    this.connected = connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }
  
}


class BtManager {

    KetaiBluetooth kBt;
    Boolean connected = false;

    BtManager(KetaiBluetooth kBt) {
        this.kBt = kBt;
        kBt.start();
    }

    public void connect(String address) {
        connected = kBt.connectDevice(address);
    }

    public void send() {}

    public void send(String data) {
        byte[] byteArrayData = new byte[data.length()];
        for (int i = 0; i < data.length(); i++) {
            byteArrayData[i] = PApplet.parseByte(data.charAt(i));
        }
        send(byteArrayData);
    }

    public void send(byte[] data) {
        kBt.broadcast(data);
    }

    public Boolean isConnected() {
        return kBt.getConnectedDeviceAddresses().size() > 0;
    }

    public void onBluetoothDataEvent(String who, byte[] data) {
        println("Received message from " + who);

        String response = "";

        for (byte dataByte: data) {
            response += String.valueOf((char) dataByte);
        }

        println(response);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        kBt.onActivityResult(requestCode, requestCode, data);
    }
}
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

    public void f() {
        fill(this.r, this.g, this.b, this.a);
    }

    public void s() {
        stroke(this.r, this.g, this.b, this.a);
    }
}
public class Colors {

  public Color textNormalDark() {
    return new Color(0,0,0,170);
  }
  public Color textNormalLight() {
    return new Color(255,255,255,170);
  }
  public Color textHighlightLight() {
    return new Color(255,255,255,255);
  }

  public Color overlayLight() {
    return new Color(255,255,255,128);
  }


  public Color backgroundLight() {
    return new Color(255,255,255,255);
  }
  public Color backgroundColored() {
    return new Color(31,139,173,255);
  }
  public Color backgroundColoredDarker() {
    return new Color(23,91,130,255);
  }
  
}
interface Component {
  public void draw();
}
class ComponentBtStatus implements Component {

  ComponentRectangle bgRect;

  ComponentText statusText;

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

  public void draw() {
    bgRect.draw();
    statusText.draw();
  }

  public void updateBoat(Boat boat) {
    if (boat != null && boat.connected)
      this.statusText.updateText("Verbunden mit \"" + boat.name + "\"");
    else
      this.statusText.updateText("Nicht verbunden");
  }
}
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
    
    PImage chevronRing;
    float chevronRingTolerance = 0.05f;

    ComponentControlPad(Position pos) {
        this.pos = pos;
            
      bg = new RoundedRectBg(pos.getLeft(), pos.getTop(), pos.w, pos.h, 30);
        chevronRing = loadImage("chevron-ring.png");

        animator = new AnimatorBounce(200);
    }

    public void draw() {
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

    public void draw() {
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

    public void draw() {
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
class ComponentRectangle implements Component {

    Color bgColor;
    Position pos;
    int[] radii;

    ComponentRectangle(Color bgColor, Position pos, int[] radii) {
        this.bgColor = bgColor;
        this.pos = pos;
        this.radii = radii;
    }

    public void draw() {
        noStroke();
        this.bgColor.f();
        rect(this.pos.getLeft(), this.pos.getTop(), this.pos.w, this.pos.h, radii[0], radii[1], radii[2], radii[3]);
    }
}
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

  public void draw() {
    textSize(this.textSize);
    textAlign(this.aligment, CENTER);
    this.textColor.f();

    text(text, pos.getLeft(), pos.getTop());
  }
  
  public void updateText(String newText) {
    text = newText;
    textTransform();
  }
  
  public void textTransform() {
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

    public int getTop() {
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

    public int getLeft() {
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

    public void draw() {

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
interface Screen {
  public void draw();
  public Screen navigateTo();
  public void updateBoat(Boat boat);
}
class ScreenConnecting implements Screen {
  
  boolean connected = false;

  ComponentText text;
  
  ComponentProgressIndicator pgIndicator;
  
  ScreenConnecting() {
    text = new ComponentText(
                          "Verbinde...",
                          28,
                          new Colors().textNormalLight(),
                          new Position(AlignmentX.CENTER, AlignmentY.CENTER, 0, 0, 0, 0),
                          CENTER,
        ComponentText.ALL_CAPS
                        );
    pgIndicator = new ComponentProgressIndicator(
                    new Colors().backgroundLight(),
                    new Position(AlignmentX.CENTER, AlignmentY.CENTER, 0, 0, 0, 0),
                    300
    );
  }
  
  public void draw() {
    text.draw();
    pgIndicator.draw();
  }
  
  public Screen navigateTo() {
    if (connected)
      return new ScreenMain();
      //return null;
    else
      return null;
  }
  
  public void updateBoat(Boat boat) {
    connected = boat.connected;
  }
  
}
class ScreenMain implements Screen {
  
  Boat boat;

  ComponentMap map;
  ComponentControlPad controlPad;

  ScreenMain() {
    //Karte quadratisch darstellen
    int mapWidth = height;

    map = new ComponentMap(
      new Position(AlignmentX.LEFT, AlignmentY.BOTTOM, 0, 0, mapWidth, height)
    );
    controlPad = new ComponentControlPad(
      new Position(AlignmentX.RIGHT, AlignmentY.BOTTOM, 30, 30, width-mapWidth-60, height-37-60)
    );
    controlPad.speed = 0;
    controlPad.direction = 0;
  }
  
  public void draw() {
    //Hintergrund aufhellen
    background(255, 255, 255, 190);
    
    controlPad.draw();
    
    map.speed = controlPad.speed;
    map.direction = controlPad.direction;

    map.draw();
  }

  public Screen navigateTo() {
    return null;
  }
  
  public void updateBoat(Boat boat) {
    this.boat = boat;
  }

}
  public void settings() {  fullScreen(P2D);  smooth(2); }
}
