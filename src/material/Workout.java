package material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

/**
 * The class for instantiate workouts.
 * @author Tim Reimer
 *
 */
public class Workout implements Serializable{
	
	private static final long serialVersionUID = -1794336254945893730L;
	
	private String name;
	private ArrayList<Exercise> exercises;
	private boolean interval;
	private int iterations;
	private int pause;
	private double sumIntensity;
	
	public Workout(){
		exercises = new ArrayList<Exercise>();
		interval = true;
		pause = 20;
		iterations = 3;
		sumIntensity = 0.0;
	}

	/**
	 * Get the number of iterations for this workout.
	 * @return The number of how often all exercises are repeated.
	 */
	public int getIterations(){
		return iterations;
	}
	
	/**
	 * Set the number of iterations for this workout.
	 * @param iterations The number of iteraions.
	 */
	public void setIterations(int iterations){
		this.iterations = iterations;
	}
	
	/**
	 * Get the time between each iteration.
	 * @return The amount of seconds to pause before the next iteration.
	 */
	public int getPauseTime() {
		return pause;
	}

	/**
	 * Set the time between each iteration.
	 * @param pause The number of seconds to pause before the next iteration.
	 */
	public void setPauseTime(int pause) {
		this.pause = pause;
	}

	/**
	 * Whether this workout is a set training or interval training.
	 * @return {@code true} if it is an interval training. {@code false} if it is a set training.
	 */
	public boolean isInterval() {
		return interval;
	}

	/**
	 * Set whether this workout is a set training or interval training.
	 * @param interval {@code true} if it is an interval training. {@code false} if it is a set training.
	 */
	public void setInterval(boolean interval) {
		this.interval = interval;
	}

	/**
	 * Get this workout's name.
	 * @return The name as a string.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Set the name for this workout.
	 * @param name The name to recognize this workout by.
	 */
	public void setName(String name){
		this.name = name.trim();
	}

	/**
	 * Get all exercises this workout contains of.
	 * @return The list of exercises for this workout.
	 */
	public ArrayList<Exercise> getExercises(){
		return exercises;
	}
	
	/**
	 * Set the exercises for this workout.
	 * @param exercises The list of exercises for this workout.
	 */
	public void setExercises(ArrayList<Exercise> exercises){
		this.exercises = exercises;
	}
	
	/**
	 * Remove an exercise from the list of exercises.
	 * @see ArrayList#remove(Object)
	 * @param exercise The exercise to remove.
	 * @return 
	 */
	public boolean remove(Exercise exercise){
		return exercises.remove(exercise);
	}
	
	/**
	 * Add an exercise to the list of exercises.
	 * @see ArrayList#add(Object)
	 * @param exercise The exercise to add.
	 * @return
	 */
	public boolean add(Exercise exercise){
		return exercises.add(exercise);
	}
	
	/**
	 * Check if the list of exercises is empty.
	 * @see ArrayList#isEmpty()
	 * @return
	 */
	public boolean isEmpty(){
		return exercises.isEmpty();
	}

	/**
	 * Check if the list of exercises contains <b>exercise</b>.
	 * @see ArrayList#contains(Object)
	 * @param exercise
	 * @return
	 */
	public boolean contains(Exercise exercise) {
		return exercises.contains(exercise);
	}

	/**
	 * Get the size of the list of exercises.
	 * @see ArrayList#size()
	 * @return
	 */
	public int size() {
		return exercises.size();
	}

	/**
	 * Get the index of <b>exercise</b> in the list of exercises.
	 * @see ArrayList#indexOf(Object)
	 * @param exercise
	 * @return
	 */
	public int indexOf(Exercise exercise) {
		return exercises.indexOf(exercise);
	}
	
	/**
	 * Get the intensity for the whole workout.
	 * @param context The context where this method is called.
	 * @return The {@code double} value representing the workout's intensity.
	 */
	public double getFinalIntensity(Context context){
		getIntensities(context);
        double pauses = 0.0;
        if(isInterval()){
        	pauses = getIterations()-1;
        } else{
        	pauses = exercises.size()-1;
        }
        sumIntensity = sumIntensity * Math.pow(0.98, pauses);
        return sumIntensity;
	}
	
	/**
	 * Get all tags and their intensities for this workout.
	 * @param context The context where this method is called.
	 * @return A mapping of the tags with their intensities for this workout.
	 */
	public HashMap<String, Double> getIntensities(Context context){
		HashMap<String, Double> intensities = new HashMap<String, Double>();
        ArrayList<Exercise> exercises = getExercises();
        int repeats = getIterations();
        sumIntensity = 0.0;
        for(Exercise e: exercises){ // for all exercises of this workout
        	String name = e.getName();
        	double amount = 0.0;
        	if(e.isRepeats()){
        		amount = e.getAmount() / 10.0;
        	} else{
        		amount = e.getAmount() / 20.0;
        	}
        	ArrayList<String> tags = Exercises.getTags(name, context);
        	double intens = Exercises.getIntensity(tags, name, context)*amount*repeats;
        	for(String tag: tags){ // add tags and their intensities to the map
	        	if(intensities.containsKey(tag)){
	        		intensities.put(tag, intensities.get(tag)+intens);
	        	} else{
	        		intensities.put(tag, intens);
	        	}
	        	sumIntensity += intens;
        	}
        }
        return intensities;
	}
	
	@Override
	public boolean equals(Object o) {
		Workout wo = (Workout) o;
		return this.name.equals(wo.name);
	}
}
