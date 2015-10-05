package workouts;

import general.BackButtonActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import main.SavingManager;
import material.Colors;
import material.Exercise;
import material.Exercises;
import material.Tags;
import material.Workout;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Utilities;

/**
 * With this activity the user can select which parts of his body he want to train with the new workout.
 * This workout and its exercises will then be generated with respect to the users selections.
 * @author Tim Reimer
 *
 */
public class FromEffectActivity extends BackButtonActivity implements OnClickListener {

	private EffectAdapter adapter;
	private ArrayList<String> tags;
	private ArrayList<String> checkedTags;
	private HashSet<String> candidateExercises;
	private RadioButton[] radio;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fromeffect);
        createBackButton();
        
        ListView list = (ListView) findViewById(R.id.effectList);
        tags = Tags.getTagsList(this);
        adapter = new EffectAdapter(this, R.layout.item_menu, tags);
        list.setAdapter(adapter);
        
        radio = new RadioButton[4];
        radio[0] = (RadioButton) findViewById(R.id.radioAuto);
        radio[1] = (RadioButton) findViewById(R.id.radio4);
        radio[2] = (RadioButton) findViewById(R.id.radio5);
        radio[3] = (RadioButton) findViewById(R.id.radio6);
        for(RadioButton r: radio)
        	r.setOnClickListener(this);
        
        // apply theme color
        Button generate = (Button) findViewById(R.id.generateButton);
        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        generate.setBackground(image);
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        divider.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        list.setDivider(divider);
        list.setDividerHeight(2);
        View lineBottom = findViewById(R.id.lineBottom);
        image = getResources().getDrawable(R.drawable.line);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lineBottom.setBackground(image);
    }
	
    /**
     * Tries to generate the new workout and saves it if it was generated successfully.
     * @param v The clicked view. Here a button.
     */
	public void generate(View v){
		boolean checked[] = adapter.getChecked();
		Workout workout = generateWorkout(checked);
		if(workout != null){
			Intent result = new Intent();
			result.putExtra(WorkoutsActivity.SET, workout);
			setResult(RESULT_OK, result);
			finish();
		}
	}

	/**
	 * Generates the workout with the following algorithm.
	 * 1. sort all exercises that contain at least one of the checked tags by their tag match
	 * 2. always pick the exercise with the highest tag match. if two or more have the same tag match
	 *    pick the one which contributes most to the currently least contributed tag.
	 * 3. repeat 2. until the workout has reached an acceptable intensity.
	 * 4. give the workout a name
	 * @param checked
	 * @return
	 */
	private Workout generateWorkout(boolean[] checked) {
		Workout workout = new Workout();
		checkedTags = new ArrayList<String>();
		for(int i=0; i<checked.length; i++){
			if(checked[i]){
				checkedTags.add(tags.get(i));
			}
		}
		if(checkedTags.isEmpty()){
			return null;
		}
		int numOfExs = 0;
		if(radio[0].isChecked()){
			int actual = checkedTags.size();
			int max = Tags.getTagsList(this).size();
			if(actual < max/3)
				numOfExs = 4;
			else if(actual <= 2*max/3)
				numOfExs = 5;
			else if(actual == max)
				numOfExs = 7;
			else
				numOfExs = 6;
		} else if(radio[1].isChecked()){
			numOfExs = 4;
		} else if(radio[2].isChecked()){
			numOfExs = 5;
		} else if(radio[3].isChecked()){
			numOfExs = 6;
		}
		for(int i=0; i<4; i++){
			System.out.println("radio["+i+"].isChecked() = "+radio[i].isChecked());
		}
		// get candidate exercises
		candidateExercises = new HashSet<String>();
		for(String tag: checkedTags){
			HashMap<String, Double> exercisesForTag = Exercises.getExercises(tag, this);
			for(String exercise: exercisesForTag.keySet()){
				candidateExercises.add(exercise);
			}
		}
		if(candidateExercises.size() < numOfExs){
			Toast.makeText(this, R.string.too_few_tags, Toast.LENGTH_SHORT).show();
			return null;
		}
		ArrayList<Exercise> pickedExercises = new ArrayList<Exercise>();
		// 3.
		while(pickedExercises.size() < numOfExs){
			// 2.
			// get least contributed tag
			HashMap<String, Double> intensities = workout.getIntensities(this);
			ArrayList<String> missingTags = new ArrayList<String>();
			String lct = "";
			double min = 100.0;
			for(String tag: checkedTags){
				double intensity = 0.0;
				if(intensities.containsKey(tag)){
					intensity = intensities.get(tag);
				} else{
					missingTags.add(tag);
				}
				if(intensity < min){
					lct = tag;
					min = intensity;
				}
			}
			// 1. refresh tag match
			ArrayList<ArrayList<String>> bucketLists = getTagMatchBuckets(missingTags);
			// add exercise
			Exercise exercise = null;
			if(bucketLists.isEmpty()){
				break;
			}
			ArrayList<String> exercises = bucketLists.get(0);
			if(exercises.size() == 1){
				String name = exercises.get(0);
				exercise = new Exercise(name);
				bucketLists.remove(0);
				candidateExercises.remove(name);
			} else{
				double maxContribution = -1.0;
				int index = -1;
				for(int i=0; i< exercises.size(); i++){
					double contrib = Exercises.getIntensity(lct, exercises.get(i), this);
					if(contrib > maxContribution){
						maxContribution = contrib;
						index = i;
					}
				}
				String name = exercises.get(index);
				exercise = new Exercise(name);
				bucketLists.get(0).remove(index);
				candidateExercises.remove(name);
			}
			pickedExercises.add(exercise);
			workout.setExercises(pickedExercises);
		}
		// 4.
		// genarate name of workout
		String genName = checkedTags.get(0);
		if(checkedTags.size() == 2){
			genName += " "+getString(R.string.and)+" "+checkedTags.get(1);
		} else if(checkedTags.size() == Tags.getTagsList(this).size()){
			genName = getString(R.string.all_of_em);
		} else if(checkedTags.size() > 2){
			genName += ", "+checkedTags.get(1)+" "+getString(R.string.and_more);
		}
		// get all currently used names
		SavingManager mngr = SavingManager.getInstance();
		ArrayList<Workout> sets = mngr.loadWorkouts();
		ArrayList<String> names = new ArrayList<String>();
		for(Workout set: sets){
			names.add(set.getName());
		}
		// check for name collisions
		String name = genName;
		int i = 1;
		while(names.contains(name)){
			i++;
			name = genName+" "+i;
		}
		workout.setName(name);
		return workout;
	}

	/**
	 * First part of the generating algorithm.
	 * @param missingTags A list of the tags that have no contributions yet.
	 * @return The desired sorting of the exercises that may be used for this workout.
	 */
	private ArrayList<ArrayList<String>> getTagMatchBuckets(ArrayList<String> missingTags) {
		// sort found exercises by tag match
		LinkedHashMap<String, Double> tagMatches = new LinkedHashMap<String, Double>();
		for(String exercise: candidateExercises){
			double tagMatch = Exercises.getTagMatch(exercise, checkedTags, this);
			double missingTagMatch = Exercises.getTagMatch(exercise, missingTags, this);
			tagMatches.put(exercise, missingTagMatch > tagMatch ? missingTagMatch : tagMatch);
		}
		tagMatches = Utilities.sortHashMapByValues(tagMatches, false);
		// put exercises with same tag match in the same bucket
		ArrayList<ArrayList<String>> bucketLists = new ArrayList<ArrayList<String>>();
		ArrayList<Double> values = new ArrayList<Double>();
		Iterator<Entry<String, Double>> it = tagMatches.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Double> entry = it.next();
			double match = entry.getValue();
			String exercise = entry.getKey();
			if(values.contains(match)){
				bucketLists.get(values.indexOf(match)).add(exercise);
			} else{
				values.add(match);
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(exercise);
				bucketLists.add(newList);
			}
		}
		return bucketLists;
	}

	@Override
	public void onClick(View v) {
		for(RadioButton r: radio)
			r.setChecked(false);
		if(v == radio[0])
			radio[0].setChecked(true);
		else if(v == radio[1])
			radio[1].setChecked(true);
		else if(v == radio[2])
			radio[2].setChecked(true);
		else if(v == radio[3])
			radio[3].setChecked(true);
	}
}
