package training;

import general.BackAndMenuButtonActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import main.SavingManager;
import material.Colors;
import material.Workout;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * With this activity the user can choose a workout he want to train with.
 * @author Tim Reimer
 *
 */
public class ChooseWorkoutActivity extends BackAndMenuButtonActivity implements OnItemClickListener, OnItemLongClickListener {

	public class WorkoutComparator implements Comparator<Workout> {
		@Override
		public int compare(Workout lwo, Workout rwo) {
			HashMap<String, Integer> stats = mngr.getWorkoutStats();
			String lname = lwo.getName();
			String rname = rwo.getName();
			int lval = stats.containsKey(lname) ? stats.get(lname) : 0;
			int rval = stats.containsKey(rname) ? stats.get(rname) : 0;
			if(lval < rval){
				return 1;
			}
			if(lval > rval){
				return -1;
			}
			return 0;
		}
	}

	public static final String POSITION = "wo_position";
	private ArrayList<Workout> workouts;
	private SavingManager mngr;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseworkout);
        createBackButton();
        createMenuButton();
        
        mngr = SavingManager.getInstance();
        workouts = mngr.loadWorkouts();
        Collections.sort(workouts, new WorkoutComparator());
        
        ListView lv = (ListView) findViewById(R.id.workoutList);
        SimpleWorkoutAdapter adapter = new SimpleWorkoutAdapter(this, R.layout.item_trainingset_simple, workouts);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        
        // apply theme color
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        divider.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lv.setDivider(divider);
        lv.setDividerHeight(2);
        View lineBottom = findViewById(R.id.lineBottom);
        Drawable image = getResources().getDrawable(R.drawable.line);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TrainingActivity.DONE && resultCode == TrainingActivity.DONE){
			finish();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, Properties1Activity.class);
		ArrayList<Workout> wos = mngr.loadWorkouts();
		int idx = wos.indexOf(workouts.get(position));
		intent.putExtra(POSITION, idx);
		startActivityForResult(intent, TrainingActivity.DONE);
	}

	@Override
	public void menu(View v) {
		Intent intent = new Intent(this, WorkoutsMenuActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if(mngr.loadSettings()[SavingManager.WORKOUT_SETTING_ID2]){
			TextView text = (TextView) view.findViewById(R.id.trainingSetName);
			View line = view.findViewById(R.id.checkLine);
			String name = text.getText().toString();
			mngr.checkWorkout(name);
			HashSet<String> checked = mngr.getWeekWorkouts();
			if(checked.contains(name)){
				text.setTextColor(getResources().getColor(R.color.grey));
				line.setVisibility(View.VISIBLE);
			} else{
				text.setTextColor(getResources().getColor(R.color.black));
				line.setVisibility(View.GONE);
			}
			return true;
		}
		return false;
	}

}
