/**
* Klasse, die das Animieren von einer Zahl von einem Wert zum anderen ermöglicht
**/

abstract class Animator {

	public Animator(int duration) {
		this.duration = duration;
	}

	int startTime;
	int duration;
  	boolean ranBefore = false;
	boolean isRunning() {
		return startTime+duration >= millis() && startTime <= millis();
	}

	abstract float getValue();

	void start() {
		if (!ranBefore) {
			startTime = millis();
			ranBefore = true;
		}
	}

	void reset() {
		ranBefore = false;
	}
}
