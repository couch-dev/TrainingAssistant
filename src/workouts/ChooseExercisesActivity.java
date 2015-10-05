package workouts;

import general.BackButtonActivity;

import java.util.ArrayList;
import java.util.Collections;

import material.Colors;
import material.Exercise;
import material.Exercises;
import material.Workout;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Android;

/**
 * With this activity the user can create a new workout by defining which exercises it consists of or can edit an existing
 * workout. The user also has to give this workout a name.
 * @author Tim Reimer
 *
 */
public class ChooseExercisesActivity extends BackButtonActivity implements OnLayoutChangeListener {

	public static final String NAME = "old name";
	private Workout set;
	private ListView listview;
	private ArrayList<String> exercises;
	private ArrayList<String> selected;
	private ExerciseAdapter adapter;
	private ExerciseAdapter selectedAdapter;
	private ListView selectedList;
	private String oldName;
	private int scrollHeight;
	private int oldScrollHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseexercises);
        createBackButton();
        
    	set = new Workout();
    	selected = new ArrayList<String>();
        Workout intent_set = (Workout) getIntent().getSerializableExtra(WorkoutsActivity.SET);
        if(intent_set != null){
        	set = intent_set;
        	EditText et = (EditText) findViewById(R.id.setName);
        	oldName = set.getName();
            et.setText(oldName);
            for(Exercise e: set.getExercises()){
            	selected.add(e.getName());
            }
    	}
        selectedList = (ListView) findViewById(R.id.selectedList);
        selectedAdapter = new ExerciseAdapter(this, R.layout.item_exercise_selected, selected, null);
        selectedList.setAdapter(selectedAdapter);
        Android.setListViewHeightBasedOnChildren(selectedList);
        listview = (ListView) findViewById(R.id.exercisesList);
        exercises = new ArrayList<String>();
        for(Exercises e: Exercises.values()){
        	exercises.add(e.getString(this));
        }
        Collections.sort(exercises);
        adapter = new ExerciseAdapter(this, R.layout.item_exercise, exercises, set.getExercises());
        listview.setAdapter(adapter);
        Android.setListViewHeightBasedOnChildren(listview);
		ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.addOnLayoutChangeListener(this);
		
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
        // apply theme color
        Button save = (Button) findViewById(R.id.saveSet);
        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        save.setBackground(image);
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        divider.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        listview.setDivider(divider);
        listview.setDividerHeight(2);
        View lineBottom = findViewById(R.id.lineBottom);
        image = getResources().getDrawable(R.drawable.line);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lineBottom.setBackground(image);
	}
	
	/**
	 * Saves the new or edited workout if it has a name and at least one exercise is selected.
	 * @param v The clicked view. Here the save button.
	 */
	public void saveSet(View v){
		EditText setName = (EditText) findViewById(R.id.setName);
		String name = setName.getText().toString();
		if(name.isEmpty()){
			Toast.makeText(this, R.string.dont_forget_name, Toast.LENGTH_LONG).show();
		} else if(set.isEmpty()){
			Toast.makeText(this, R.string.must_not_be_empty, Toast.LENGTH_LONG).show();
		}else{
			set.setName(name);
			Intent result = new Intent();
			result.putExtra(WorkoutsActivity.SET, set);
			result.putExtra(NAME, oldName);
			setResult(RESULT_OK, result);
			finish();
		}
	}
	
	/**
	 * Selects or deselects an exercise and adds or removes it from the list of selected exercises.
	 * It also refreshes the view.<br>
	 * This method is used by the adapter of the list view for the list of exercises.
	 * @param parent The adapter view. Here the {@code ListView}.
	 * @param view The view of the one item that is clicked. Mostly a {@code RelativeLayout}.
	 * @param position The position of this item in his parent view.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position) {
		TextView text = (TextView) view.findViewById(R.id.exerciseName);
		ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
		oldScrollHeight = scroll.getChildAt(0).getHeight();
		TextView pos = (TextView) view.findViewById(R.id.exercisePosition);
		String name = text.getText().toString();
		Exercise ex = new Exercise(name);
		if(!set.contains(ex)){
			set.add(ex);
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Colors.getThemeColor());
			pos.setVisibility(View.VISIBLE);
			pos.setTextColor(Colors.getThemeColor());
			pos.setText(""+set.size());
			adapter.select(position, set.size());
			selected.add(name);
		} else{
			int index = set.indexOf(ex);
			text.setTypeface(null, Typeface.NORMAL);
			text.setTextColor(getResources().getColor(R.color.black));
			set.remove(ex);
			pos.setVisibility(View.GONE);
			for(int i=0; i<parent.getChildCount(); i++){ // refresh visible items
				View v = parent.getChildAt(i);
				TextView tv = (TextView) v.findViewById(R.id.exerciseName);
				TextView p = (TextView) v.findViewById(R.id.exercisePosition);
				String s = tv.getText().toString();
				Exercise e = new Exercise(s);
				if(set.contains(e) && set.indexOf(e)>=index){
					int n = Integer.parseInt(p.getText().toString());
					p.setText(""+(n-1));
				}
			}
			adapter.refreshAll(position); // store refreshed data for every item
			adapter.deselect(position);
			selected.remove(name);
		}
		selectedAdapter.notifyDataSetChanged();
		Android.setListViewHeightBasedOnChildren(selectedList);
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
		scrollHeight = scroll.getChildAt(0).getHeight();
		if(scrollHeight != oldScrollHeight){
			int diff = scrollHeight - oldScrollHeight;
			scroll.scrollBy(0, diff);
			System.out.println("height = "+oldScrollHeight+", newHeight = "+scrollHeight+", diff = "+diff);
		} else{
			scroll.scrollTo(0, 0);
		}
		oldScrollHeight = scrollHeight;
	}
}
