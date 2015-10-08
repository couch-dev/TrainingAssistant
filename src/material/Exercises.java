package material;

import java.util.ArrayList;
import java.util.HashMap;

import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Utilities;
import android.content.Context;

/**
 * An enum that has stored overall information about every exercise. It also provides several methods for handling the
 * exercises dependencies on the different tags and their intensities.
 * @see Tags
 * @author Tim Reimer
 *
 */
public enum Exercises {

	BURPEES(R.string.burpees, 0.95, -1),
	OH_PUSH_UPS(R.string.oh_pushups, 0.75, -1),
	SCHWIMMER(R.string.swimmer, 0.65, -1),
	ABFAHRTSHOCKE(R.string.downhill_pos, 0.75, -1),
	LANGARMSTUETZ(R.string.straight_arms_pos, 0.7, -1),
	AS_KNIEBEUGEN(R.string.longstep_squats, 0.75, -1),
	PUSH_UPS(R.string.pushups, 0.75, -1),
	KAEFER(R.string.bug, 0.8, -1),
	CRUNCHES(R.string.cruches, 0.75, -1),
	KNIEBEUGEN(R.string.squats, 0.75, -1),
	SCHNELLE_KNIEBEUGEN(R.string.fast_squats, 0.85, -1),
	SKIPPINGS(R.string.skippings, 0.7, -1),
	FLEDERMAUS(R.string.bat, 0.7, -1),
	SQUAT_JUMPS(R.string.squat_jumps, 0.9, -1),
	CLIMBERS(R.string.climbers, 0.75, -1),
	WECHSELSPRUENGE(R.string.alternating_jumps, 0.65, -1),
	JUMPING_JACKS(R.string.jumping_jacks, 0.6, -1),
	LANGARM_UNTERARM(R.string.straight_to_forearms, 0.85, -1),
	BRIDGING(R.string.bridging, 0.6, -1),
	JUMPS_TO_OVER_HEAD(R.string.jumps_to_oh, 0.8, -1),
	SCHUNKELN(R.string.wobble, 0.55, -1),
	SEITSTUETZ(R.string.side_pos, 0.7, -1),
	LATERALFLEXION(R.string.trunk_rotation, 0.65, -1),
	SITUPS(R.string.situps, 0.75, -1),
	ARNOLD_DIPS(R.string.arnold_dips, 0.75, R.string.lean_on),
	KLIMMZUEGE(R.string.pullups, 0.85, R.string.pull_up),
	CURLS(R.string.dumbbell_curls, 0.7, R.string.dumbbell),
	HANDFLAECHEN(R.string.palm_pressing, 0.5, -1),
	BENCH_PRESS(R.string.bench_press, 0.65, R.string.bell),
	FLYING(R.string.flying, 0.7, R.string.dumbbell),
	SHRUGS(R.string.shrugs, 0.5, R.string.dumbbell),
	FOREARM_ROTATION(R.string.forearm_rotations, 0.5, R.string.dumbbell);

	private int type;
	private double value;
	private int note;
	/**
	 * The maximum intensity for a tag for an exercise.<br>
	 * (If a tag is the only tag for one exercise the intensity equals {@code maxIntensity})
	 */
	private static final double maxIntensity = 10.0;

	Exercises(int resID, double val, int note){
		type = resID;
		value = val;
		this.note = note;
	}

	/** 
	 * Get the string value of this {@code Exercises}.
	 * @param context The context where this method is called.
	 * @return A string value.
	 */
	public String getString(Context context)
	{
		return context.getString(type);
	}
	
	/**
	 * Get the note for this {@code Exercise} if it has a note attached to it.
	 * @param context The context where this method is called.
	 * @return A note as string or {@code null} if no note is attached.
	 */
	public String getNote(Context context){
		String str = null;
		try{
			str = context.getString(note);
		} catch(Exception e){};
		return str;
	}
	
	/**
	 * Get the double value of this {@code Exercises}.
	 * @return A double value.
	 */
	public double getValue()
	{
		return value;
	}
	
	/**
	 * Get the tags related to the exercise <b>ex</b>.
	 * @param ex The exercise to get the tags for.
	 * @param context The context where this method is called.
	 * @return A list of tags as strings for the given exercise, which contains at least one tag.
	 */
	public static ArrayList<String> getTags(String ex, Context context){
		ArrayList<String> tags = new ArrayList<String>();
		if(ex.equals(WECHSELSPRUENGE.getString(context)) || ex.equals(SKIPPINGS.getString(context))){
			tags.add(Tags.CALVES.getString(context));
		}
		if(ex.equals(BURPEES.getString(context)) || ex.equals(SQUAT_JUMPS.getString(context)) || ex.equals(SCHNELLE_KNIEBEUGEN.getString(context))
				|| ex.equals(KNIEBEUGEN.getString(context)) || ex.equals(AS_KNIEBEUGEN.getString(context))
				|| ex.equals(ABFAHRTSHOCKE.getString(context))){
			tags.add(Tags.THIGHS.getString(context));
		}
		if(ex.equals(BRIDGING.getString(context)) || ex.equals(SCHNELLE_KNIEBEUGEN.getString(context)) || ex.equals(KNIEBEUGEN.getString(context))
				|| ex.equals(ABFAHRTSHOCKE.getString(context))){
			tags.add(Tags.BUTTOCKS.getString(context));
		}
		if(ex.equals(LATERALFLEXION.getString(context)) || ex.equals(SITUPS.getString(context)) || ex.equals(SCHUNKELN.getString(context))
				|| ex.equals(CRUNCHES.getString(context)) || ex.equals(KAEFER.getString(context)) || ex.equals(LANGARMSTUETZ.getString(context))){
			tags.add(Tags.ABS.getString(context));
		}
		if(ex.equals(LATERALFLEXION.getString(context)) || ex.equals(SEITSTUETZ.getString(context))
				|| ex.equals(SCHUNKELN.getString(context)) || ex.equals(FLEDERMAUS.getString(context)) || ex.equals(KAEFER.getString(context))){
			tags.add(Tags.SIDEABS.getString(context));
		}
		if(ex.equals(HANDFLAECHEN.getString(context)) || ex.equals(ARNOLD_DIPS.getString(context)) || ex.equals(PUSH_UPS.getString(context))
				|| ex.equals(BENCH_PRESS.getString(context)) || ex.equals(FLYING.getString(context))){
			tags.add(Tags.BREAST.getString(context));
		}
		if(ex.equals(LANGARM_UNTERARM.getString(context)) || ex.equals(FLEDERMAUS.getString(context)) || ex.equals(PUSH_UPS.getString(context))
				|| ex.equals(LANGARMSTUETZ.getString(context)) || ex.equals(OH_PUSH_UPS.getString(context))){
			tags.add(Tags.SHOULDERS.getString(context));
		}
		if(ex.equals(KLIMMZUEGE.getString(context)) || ex.equals(SEITSTUETZ.getString(context)) || ex.equals(BRIDGING.getString(context))
				|| ex.equals(SCHWIMMER.getString(context)) || ex.equals(LANGARMSTUETZ.getString(context))){
			tags.add(Tags.BACK.getString(context));
		}
		if(ex.equals(HANDFLAECHEN.getString(context)) || ex.equals(KLIMMZUEGE.getString(context)) || ex.equals(CURLS.getString(context))){
			tags.add(Tags.BICEPS.getString(context));
		}
		if(ex.equals(ARNOLD_DIPS.getString(context)) || ex.equals(LANGARM_UNTERARM.getString(context)) || ex.equals(PUSH_UPS.getString(context))){
			tags.add(Tags.TRICEPS.getString(context));
		}
		if(ex.equals(KLIMMZUEGE.getString(context)) || ex.equals(FOREARM_ROTATION.getString(context))){
			tags.add(Tags.FOREARMS.getString(context));
		}
		if(ex.equals(JUMPS_TO_OVER_HEAD.getString(context)) || ex.equals(JUMPING_JACKS.getString(context))
				|| ex.equals(WECHSELSPRUENGE.getString(context)) || ex.equals(CLIMBERS.getString(context))
				|| ex.equals(SQUAT_JUMPS.getString(context)) || ex.equals(SKIPPINGS.getString(context))
				|| ex.equals(SCHNELLE_KNIEBEUGEN.getString(context)) || ex.equals(BURPEES.getString(context))){
			tags.add(Tags.STAMINA.getString(context));
		}
		if(ex.equals(SHRUGS.getString(context))){
			tags.add(Tags.NECK.getString(context));
		}
		return tags;
	}
	
	/**
	 * Get a mapping with exercises and their intensities for a given tag.
	 * @param tag The tag to get the exercises for.
	 * @param context The context where this method is called.
	 * @return A mapping of the exercises linked to <b>tag</b> and their intensities.
	 */
	public static HashMap<String,Double> getExercises(String tag, Context context){
		HashMap<String,Double> exercises = new HashMap<String,Double>();
		for(Exercises e: values()){
			String ex = e.getString(context);
			ArrayList<String> tags = getTags(ex, context);
			if(tags.contains(tag)){
				exercises.put(ex, getIntensity(tag, ex, context));
			}
		}
		return exercises;
	}
	
	/**
	 * 
	 * Get the intensity of <b>tag</b> for the exercise <b>ex</b>.
	 * @param tag The tag you want the intensity for.
	 * @param ex The name of the exercise which may be tagged with <b>tag</b>.
	 * @param context The context where this method is called.
	 * @return A {@code double} value representing the intensity.
	 */
	public static double getIntensity(String tag, String ex, Context context){
		double intensity = 0.0;
		ArrayList<String> tags = getTags(ex, context);
		if(tags.contains(tag)){
			intensity = getIntensity(tags, ex, context);
		}
		return intensity;
	}
	
	/**
	 * Get the intensity for one tag in the given list of <b>tags</b> for the exercise <b>ex</b>.
	 * @param tags The list of tags for the exercise.
	 * @param ex The exercise the list of tags belongs to.
	 * @param context The context where this method is called.
	 * @return The intensity for one of the tags. (The intensities are the same for every tag for one exercise)
	 */
	public static double getIntensity(ArrayList<String> tags, String ex, Context context){
		double size = (double) tags.size();
		double half = maxIntensity*0.5;
		double fac = size/(maxIntensity*(1-(1/Utilities.e)));
		double overallIntens = half+half*fac;
		double exVal = getValueOf(ex, context);
		double intensity = overallIntens/size*(exVal*exVal*2);
		return intensity;
	}

	/**
	 * Get the value of overall intensity for the exercise <b>ex</b>.
	 * @param ex The exercise to get the value from.
	 * @param context The context where this method is called.
	 * @return A value between {@code 0.5} and {@code 1.0} or {@code -1.0} if the exercise <b>ex</b> does not exist.
	 */
	private static double getValueOf(String ex, Context context) {
		for(Exercises e: Exercises.values()){
			if(e.getString(context).equals(ex)){
				return e.getValue();
			}
		}
		return -1.0;
	}
	
	/**
	 * Get the note for an {@code Exercise} if it exists and has a note attached to it.
	 * @param ex The exercise to get the note for.
	 * @param context The context where this method is called.
	 * @return A note as string or {@code null} if no note is attached.
	 */
	public static String getNoteFor(String ex, Context context){
		for(Exercises e: Exercises.values()){
			if(e.getString(context).equals(ex)){
				return e.getNote(context);
			}
		}
		return null;
	}

	/**
	 * Checks how many of the tags in <b>tags</b> match with the tags of the <b>exercise</b>.
	 * @param exercise The exercise to check.
	 * @param tags A list of tags.
	 * @param context The context where this method is called.
	 * @return A {@code double} value combined from the intensities of matching tags and the percentage of how many
	 * of the tags for the exercise match with the passed list of tags.
	 */
	public static double getTagMatch(String exercise, ArrayList<String> tags, Context context) {
		double match = 0.0;
		int matches = 0;
		ArrayList<String> exerciseTags = getTags(exercise, context);
		for(String tag: exerciseTags){
			double intensity = getIntensity(tag, exercise, context);
			if(tags.contains(tag)){
				match += intensity;
				matches++;
			}
		}
		int mismatches = exerciseTags.size()-matches;
		double percentage = matches/(double)tags.size();
		match = match*percentage-mismatches*match*0.1;
		return match;
	}

	/**
	 * Rates the intensity within a range from {@code 0} to {@code 5} stars in steps of size {@code 0.5}.
	 * @param intensity The intensity to rate.
	 * @return The rating as a {@code double} value between {@code 0.0} and {@code 5.0}.
	 */
	public static double getStars(double intensity) {
		double stars = 0.0;
		if(intensity < 40){
			stars = 0.0;
		} else if(intensity < 60){
			stars = 0.5;
		} else if(intensity < 70){
			stars = 1.0;
		} else if(intensity < 80){
			stars = 1.5;
		} else if(intensity < 95){
			stars = 2.0;
		} else if(intensity < 110){
			stars = 2.5;
		} else if(intensity < 130){
			stars = 3.0;
		} else if(intensity < 150){
			stars = 3.5;
		} else if(intensity < 200){
			stars = 4.0;
		} else if(intensity < 250){
			stars = 4.5;
		} else{
			stars = 5.0;
		}
		return stars;
	}
	
}
