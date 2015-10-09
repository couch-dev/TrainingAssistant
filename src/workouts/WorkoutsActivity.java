package workouts;

import general.BackButtonActivity;

import java.util.ArrayList;

import main.SavingManager;
import material.Colors;
import material.Workout;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * This activity shows a list of all so far created workouts. They can now be edited or deleted or the user can create new
 * ones.
 * @author Tim Reimer
 *
 */
public class WorkoutsActivity extends BackButtonActivity {

	private ArrayList<Workout> workouts;
	private WorkoutAdapter adapter;
	public static int NEW_SET = 1;
	public static int CHANGE_SET = 2;
	public static String SET = "result_set";
	public SavingManager mngr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        createBackButton();
        
        mngr = SavingManager.getInstance();
        workouts = mngr.loadWorkouts();
        
        ListView lv = (ListView) findViewById(R.id.workoutList);
        adapter = new WorkoutAdapter(this, R.layout.item_trainingset, workouts);
        lv.setAdapter(adapter);
        
        // apply theme color
        Button create = (Button) findViewById(R.id.newButton);
        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        create.setBackground(image);
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        divider.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lv.setDivider(divider);
        lv.setDividerHeight(2);
        View lineBottom = findViewById(R.id.lineBottom);
        image = getResources().getDrawable(R.drawable.line);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lineBottom.setBackground(image);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        TextView notAvailable = (TextView) findViewById(R.id.notAvailable);
        if(!workouts.isEmpty()){
        	notAvailable.setVisibility(View.GONE);
        } else{
        	notAvailable.setVisibility(View.VISIBLE);
        }
	}
	
	/**
	 * Starts the activity to choose how to create the new workout.
	 * @param v The clicked view. Here a button.
	 */
	public void newWorkout(View v){
		Intent intent = new Intent(this, CreateWorkoutActivity.class);
		startActivityForResult(intent, NEW_SET);
	}
	
	/**
	 * Removes the workout at position <b>index</b>
	 * @param index The index of the workout to remove.
	 */
	public void removeWorkout(int index){
		mngr.loadSettings();
		String name = workouts.get(index).getName();
		if(mngr.getWeekWorkouts().contains(name)){
			mngr.checkWorkout(name);
		}
		mngr.removeFromWorkoutStats(name);
		workouts.remove(index);
		adapter.notifyDataSetChanged();
		mngr.saveWorkouts(workouts);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        if (requestCode == NEW_SET) {
	        	Workout set = (Workout) data.getSerializableExtra(SET);
	        	String name = checkForNameCollision(set.getName());
	    		set.setName(name);
	        	workouts.add(set);
	    		adapter.notifyDataSetChanged();
	    		mngr.saveWorkouts(workouts);
	        } else if(requestCode == CHANGE_SET){
	        	Workout set = (Workout) data.getSerializableExtra(SET);
	        	String oldName = data.getStringExtra(ChooseExercisesActivity.NAME);
	        	String name = set.getName();
	        	if(name.equals(oldName)){
        			workouts.set(workouts.indexOf(set), set);
	        	}else{
	        		name = checkForNameCollision(name);
	        		set.setName(name);
	        		Workout old = new Workout();
	        		old.setName(oldName);
        			workouts.set(workouts.indexOf(old), set);
        			mngr.updateWorkoutStats(oldName, name);
	        	}
	        	adapter.notifyDataSetChanged();
	    		mngr.saveWorkouts(workouts);
    			if(mngr.getWeekWorkouts().contains(oldName)){
    				mngr.checkWorkout(oldName);
    				mngr.checkWorkout(name);
    			}
	        }
	    }
	}
	
	private String checkForNameCollision(String name){
    	// get all currently used names
		SavingManager mngr = SavingManager.getInstance();
		ArrayList<Workout> sets = mngr.loadWorkouts();
		ArrayList<String> names = new ArrayList<String>();
		for(Workout wo: sets){
			names.add(wo.getName());
		}
		// check for name collisions
		String newName = name;
		int i = 1;
		while(names.contains(newName)){
			i++;
			newName = name+" "+i;
		}
		return newName;
	}

	/**
	 * Starts the activity to edit the existing workout at <b>position</b>.<br>
	 * This method is invoked by the list views adapter.
	 * @param position The position of the workout to edit.
	 */
	public void onClickItemAt(int position) {
		Intent intent = new Intent(this, ChooseExercisesActivity.class);
		intent.putExtra(SET, workouts.get(position));
		startActivityForResult(intent, CHANGE_SET);
	}

	/**
	 * Shows a window that asks the user if he really wants to delete the workout at <b>index</b>
	 * @param index The index of the workout in the list of workouts.
	 */
	public void askRemoveSet(int index) {
		new DeleteWorkoutPopup(this, index, workouts.get(index).getName());
	}
	
}
