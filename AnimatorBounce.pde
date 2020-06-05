/**
* siehe auch Animator.pde, implementiert einen Animator, der mit ausfadender Interpolation
**/

class AnimatorBounce extends Animator {

	public AnimatorBounce(int duration) {
		super(duration);
	}

	float getValue() {
		if (isRunning()) {
			float timeFraction = (millis() - float(startTime)) / duration;
			return - timeFraction * (timeFraction - 2);
		} else return 0.0f;
	}
}
