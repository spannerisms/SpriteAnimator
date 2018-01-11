package animator;

public enum AnimationMode {
	PLAY ("Normal play"),
	STEP ("Step-by-step"),
	ONCE ("Play once");

	private final String name;

	private AnimationMode(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}