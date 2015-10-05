package material;

import java.io.Serializable;

/**
 * The class for instantiate exercises.
 * @author Tim Reimer
 *
 */
public class Exercise implements Serializable{
	
	private static final long serialVersionUID = 1862131267953402439L;
	
	private String name;
	private boolean repeats;
	private int amount;
	
	public Exercise(String name){
		this.name = name;
		repeats = true;
		amount = 10;
	}
	
	/**
	 * Get the name of this exercise.
	 * @return This exercises name as string.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Whether this exercises is trained by repeating it a fixed amount of times or by exercising it over a duration of time.
	 * @return {@code true} if its repeated several times. {@code false} if its done over time.
	 */
	public boolean isRepeats() {
		return repeats;
	}

	/**
	 * Sets whether this exercises is trained by repeating it a fixed amount of times or by exercising it over a duration of 
	 * time.
	 * @param repeats {@code true} if it should be repeated several times. {@code false} if it should be done over time.
	 */
	public void setRepeats(boolean repeats) {
		this.repeats = repeats;
	}

	/**
	 * Get the amount of repeats or seconds of this exercise depending on what {@link #isRepeats()} returns.
	 * @return The amount either of repeats or of seconds.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Set the amount of repeats or seconds of this exercise depending on what {@link #isRepeats()} returns.
	 * @param amount The amount either of repeats or of seconds.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public boolean equals(Object o) {
		return name.equals(((Exercise) o).getName());
	}

}
