package main;

import general.MenuActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import material.Exercise;
import material.Workout;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import de.couchdev.trainingassistant.R;

/**
 * The class that manages all saving and loading issues.
 * @author Tim Reimer
 *
 */
public class SavingManager {

	private int startYear = 2015;
	private static SavingManager instance;
	private Context context;
	private int yearIndex;
	private int woy;
	private int[][] stats;
	private int startWeek;
	private int lastWeek;
	private HashMap<String, Integer> exerciseStats;
	private HashMap<String, Integer> workoutStats;
	private HashMap<String, Integer> exStatsWeek;
	private ArrayList<Double> intensities;
	private ArrayList<Double> intensWeek;
	private boolean[] settings;
	private static final String STATS_FILE = "statistics";
	private static final String SETTINGS_FILE = "settings";
	private static final String WORKOUTS_FILE = "workouts";
	private static final String FILE_ENDING = ".trainassist";
	private HashSet<String> weekWorkouts;
	private double volume;
	private int themeColor;
	private static File PATH;
	
	/**
	 * For automatic workout checking.
	 */
	public static final int WORKOUT_SETTING_ID1 = 0;
	/**
	 * For manual workout checking.
	 */
	public static final int WORKOUT_SETTING_ID2 = 1;
	
	/**
	 * Call this once when you start the application.
	 * @param context The applications context.
	 */
	public SavingManager(Context context){
		instance = this;
		this.context = context;
		PATH = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
		if(!PATH.exists()){
			PATH.mkdirs();
		}
	}
	
	/**
	 * Call this to get an instance of this SavingManager.
	 * @return This applications SavingManager.
	 */
	public static SavingManager getInstance(){
		return instance;
	}

	/**
	 * Saves all <b>workouts</b> to the file specified by <b>filename</b>.
	 * @param workouts The list of workouts to save.
	 * @param filename The filename of the file where to store the data in.
	 */
	public void saveWorkouts(ArrayList<Workout> workouts){
		File file = new File(PATH, WORKOUTS_FILE+FILE_ENDING);
		FileOutputStream fos;
		try {
		    fos = new FileOutputStream(file);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(workouts);
		    oos.close();
		} catch (FileNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (IOException e) {
			Toast.makeText(context, context.getString(R.string.error_saving), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Load the workouts from the stored file.
	 * @param filename The name of the stored file without ending.
	 * @return A list of workouts.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Workout> loadWorkouts(){
		ArrayList<Workout> trainingSets = null;
		File file = new File(PATH, WORKOUTS_FILE+FILE_ENDING);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			ObjectInputStream ois= new ObjectInputStream(fis);
			trainingSets = (ArrayList<Workout>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
			trainingSets = new ArrayList<Workout>();
		} catch (IOException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (ClassNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
		}
		if(trainingSets == null){
			trainingSets = new ArrayList<Workout>();
		}
		return trainingSets;
	}
	
	/**
	 * Saves all values that are import for the statistics.
	 */
	private void saveStatistics(){
		File file = new File(PATH, STATS_FILE+FILE_ENDING);
		FileOutputStream fos;
		try {
		    fos = new FileOutputStream(file);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(stats);
		    oos.writeObject(startWeek);
		    oos.writeObject(lastWeek);
		    oos.writeObject(exerciseStats);
		    oos.writeObject(exStatsWeek);
		    oos.writeObject(intensities);
		    oos.writeObject(intensWeek);
		    oos.writeObject(workoutStats);
		    oos.close();
		} catch (FileNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (IOException e) {
			Toast.makeText(context, context.getString(R.string.error_saving), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Loads all values needed for the statistics.
	 */
	@SuppressWarnings("unchecked")
	public void loadStatistics(){
		File file = new File(PATH, STATS_FILE+FILE_ENDING);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			ObjectInputStream ois= new ObjectInputStream(fis);
			stats = (int[][]) ois.readObject();
			startWeek = (Integer) ois.readObject();
			lastWeek = (Integer) ois.readObject();
			exerciseStats = (HashMap<String,Integer>) ois.readObject();
			exStatsWeek = (HashMap<String,Integer>) ois.readObject();
			intensities = (ArrayList<Double>) ois.readObject();
			intensWeek = (ArrayList<Double>) ois.readObject();
			workoutStats = (HashMap<String, Integer>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
			stats = new int[10][53];
			exerciseStats = new HashMap<String, Integer>();
			exStatsWeek = new HashMap<String, Integer>();
			intensities = new ArrayList<Double>();
			workoutStats = new HashMap<String, Integer>();
		} catch (IOException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (ClassNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
		}
		if(stats == null){
			stats = new int[10][53];
		}
		if(exerciseStats == null){
			exerciseStats = new HashMap<String, Integer>();
		}
		if(exStatsWeek == null){
			exStatsWeek = new HashMap<String, Integer>();
		}
		if(intensities == null){
			intensities = new ArrayList<Double>();
		}
		if(intensWeek == null){
			intensWeek = new ArrayList<Double>();
		}
		if(workoutStats == null){
			workoutStats = new HashMap<String, Integer>();
		}
	}

	/**
	 * Sets what current year and week of year it is.
	 * @param year Current year.
	 * @param weekOfYear Current week of year.
	 */
	public void setTime(int year, int weekOfYear) {
		this.woy = weekOfYear;
		if(lastWeek < woy-1){ // new week
			lastWeek = woy-1;
			exStatsWeek = new HashMap<String, Integer>();
			intensWeek = new ArrayList<Double>();
			saveStatistics();
			if(!loadSettings()[WORKOUT_SETTING_ID2]){
				weekWorkouts = new HashSet<String>();
				saveSettings();
			}
		}
		for(int i=0; i< stats.length-1; i++){
			yearIndex = year-startYear;
			try{
				stats[yearIndex][weekOfYear] = stats[yearIndex][weekOfYear];
				break;
			} catch(IndexOutOfBoundsException e){
				startYear++;
			}
		}
	}
	
	/**
	 * Increases the value of finished workouts for the current week of year and saves it.
	 */
	private void increaseStats(){
		stats[yearIndex][woy]++;
		boolean start = startWeek==0;
		if(start){
			for(int i=0; i<woy; i++){
				if(stats[yearIndex][i] != 0){
					start = false;
					break;
				}
			}
		}
		if(start){
			startWeek = woy;
		}
		saveStatistics();
	}
	
	/**
	 * Get the current values of finished exercises per week of year.
	 * @return
	 */
	public int[][] getStatistics(){
		return stats;
	}
	
	/**
	 * Get the index of the current year for the array returned by {@link SavingManager#getStatistics()}.
	 * @return The index of the current year.
	 */
	public int getYearIndex(){
		return yearIndex;
	}
	
	/**
	 * Get which current week of year it is.
	 * @return The number of the current week of year.
	 */
	public int getWeekOfYear(){
		return woy;
	}

	/**
	 * Resets the statistics.
	 * @param all {@code true} if all statistics should be deleted. {@code false} if only the statistics of the last week
	 * should be deleted.
	 */
	public void resetStatistic(boolean all) {
		if(all){
			stats = new int[10][53];
			startWeek = 0;
			exerciseStats = new HashMap<String, Integer>();
			exStatsWeek = new HashMap<String, Integer>();
			intensities = new ArrayList<Double>();
			intensWeek = new ArrayList<Double>();
		} else{
			stats[yearIndex][woy] = 0;
			if(startWeek == woy){
				startWeek = 0;
			}
			if(exStatsWeek.keySet() != null && !exStatsWeek.keySet().isEmpty()){
				for(String name: exStatsWeek.keySet()){
					exerciseStats.put(name, exerciseStats.get(name)-exStatsWeek.get(name));
				}
			}
			exStatsWeek = new HashMap<String, Integer>();
			if(intensWeek != null && !intensWeek.isEmpty()){
				for(Double d: intensWeek){
					intensities.remove(d);
				}
			}
			intensWeek = new ArrayList<Double>();
		}
		saveStatistics();
		weekWorkouts = new HashSet<String>();
		saveSettings();
	}
	
	/**
	 * Get the week of year the user started using this application.
	 * @return The first week of year recorded.
	 */
	public int getStartWeek(){
		return startWeek;
	}

	/**
	 * Increases the number of how often the <b>workout</b> and each of its exercises where trained.
	 * @param workout The trained workout.
	 */
	public void increaseStats(Workout workout) {
		String wo = workout.getName();
		if(workoutStats.containsKey(wo)){
			workoutStats.put(wo, workoutStats.get(wo)+1);
		} else{
			workoutStats.put(wo, 1);
		}
		ArrayList<Exercise> exercises = workout.getExercises();
		for(Exercise ex: exercises){
			String name = ex.getName();
			if(exerciseStats.containsKey(name)){
				exerciseStats.put(name, exerciseStats.get(name)+1);
			} else{
				exerciseStats.put(name, 1);
			}
			if(exStatsWeek.containsKey(name)){
				exStatsWeek.put(name, exStatsWeek.get(name)+1);
			} else{
				exStatsWeek.put(name, 1);
			}
		}
		increaseStats();
		saveStatistics();
	}
	
	/**
	 * Gets the mapping of exercises and their number of being trained.
	 * @return A map of all so far trained exercises and their number of how often they were part of a finished workout.
	 */
	public HashMap<String, Integer> getExerciseStatistics(){
		return exerciseStats;
	}

	/**
	 * Saves all settings of the {@link MenuActivity}s.
	 */
	private void saveSettings(){
		File file = new File(PATH, SETTINGS_FILE+FILE_ENDING);
		FileOutputStream fos;
		try {
		    fos = new FileOutputStream(file);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(settings);
		    oos.writeObject(weekWorkouts);
		    oos.writeDouble(volume);
		    oos.writeInt(themeColor);
		    oos.close();
		} catch (FileNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (IOException e) {
			Toast.makeText(context, context.getString(R.string.error_saving), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Saves all settings that are represented as {@code boolean} values.
	 * @param value The {@code boolean} value of the setting.
	 * @param index The index of the setting. This is one of the static values that have the ending <i>_ID</i>
	 * like {@link #WORKOUT_SETTING_ID1}.
	 */
	public void saveSettings(boolean value, int index){
		if(index>=0 && index<settings.length){
			settings[index] = value;
			if(!settings[WORKOUT_SETTING_ID1] && !settings[WORKOUT_SETTING_ID2]){
				weekWorkouts.clear();
			}
			saveSettings();
		}
	}
	
	/**
	 * Loads all settings that were previously saved by {@link #saveSettings()} and return the settings with {@code boolean}
	 * values.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean[] loadSettings(){
		int length = 2;
		File file = new File(PATH, SETTINGS_FILE+FILE_ENDING);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			ObjectInputStream ois= new ObjectInputStream(fis);
			settings = (boolean[]) ois.readObject();
			weekWorkouts = (HashSet<String>) ois.readObject();
			volume = ois.readDouble();
			themeColor = ois.readInt();
			ois.close();
		} catch (FileNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
			settings = new boolean[length];
			weekWorkouts = new HashSet<String>();
			volume = 0.9;
		} catch (IOException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (ClassNotFoundException e) {
			Log.i("SavingManager", "Exception caught", e);
		} catch (ClassCastException e){
			weekWorkouts = new HashSet<String>();
		}
		if(settings == null){
			settings = new boolean[length];
		}
		if(weekWorkouts == null){
			weekWorkouts = new HashSet<String>();
		}
		return settings;
	}
	
	/**
	 * Get all workouts that were so far done. (or at least checked)
	 * @return A set of workouts.
	 */
	public HashSet<String> getWeekWorkouts(){
		loadSettings();
		return weekWorkouts;
	}
	
	/**
	 * Checks or unchecks a workout and thus adds or removes it from the set returned by {@link #getWeekWorkouts()}.
	 * @param workout The workout to check or uncheck.
	 */
	public void checkWorkout(String workout){
		if(!weekWorkouts.contains(workout)){
			weekWorkouts.add(workout);
		} else{
			weekWorkouts.remove(workout);
		}
		saveSettings();
	}
	
	/**
	 * Adds a workout to the set returned by {@link #getWeekWorkouts()} if it does not contain the <b>workout</b> yet.
	 * @param workout The workout to add.
	 * @return {@code true} if the workout was successfully added. {@code false} if the set already contains this workout.
	 */
	public boolean addWorkout(String workout){
		boolean added = weekWorkouts.add(workout);
		saveSettings();
		return added;
	}
	
	/**
	 * Sets the volume for the sounds of this application and saves it.
	 * @param vol The volume to set.
	 */
	public void setVolume(double vol){
		loadSettings();
		volume = vol;
		saveSettings();
	}
	
	/**
	 * Get the volume for sounds for this application.
	 * @return The volume as {@code double} value.
	 */
	public double getVolume(){
		loadSettings();
		return volume;
	}
	
	/**
	 * Adds the <b>intensity</b> to the list of saved intensities.
	 * @param intensity The intensity to add.
	 */
	public void addIntensitiy(double intensity){
		intensWeek.add(intensity);
		intensities.add(intensity);
		saveStatistics();
	}
	
	/**
	 * Get all saved intensities.
	 * @return The list of all saved intensities.
	 */
	public ArrayList<Double> getIntensities(){
		return intensities;
	}
	
	/**
	 * Get the intensities of this week.
	 * @return The list of intensities of the current week.
	 */
	public ArrayList<Double> getWeekIntensities(){
		return intensWeek;
	}

	/**
	 * Sets the theme color for this application and saves it.
	 * @param color The color to set as theme color.
	 */
	public void setThemeColor(int color){
		loadSettings();
		themeColor = color;
		saveSettings();
	}
	
	/**
	 * Get the theme color for this application.
	 * @return The saved theme color.
	 */
	public int getThemeColor(){
		loadSettings();
		return themeColor;
	}
	
	/**
	 * Get a mapping of workouts and their time being trained.
	 * @return A {@code HashMap} of names of workouts and the corresponding number of how often each workout was trained.
	 */
	public HashMap<String, Integer> getWorkoutStats(){
		return workoutStats;
	}

	/**
	 * Remove a workout from the statistics. (Because it was deleted)
	 * @param name The name of the workout to remove.
	 */
	public void removeFromWorkoutStats(String name) {
		loadStatistics();
		workoutStats.put(name, 0);
		workoutStats.remove(name);
		saveStatistics();
	}
	
}
