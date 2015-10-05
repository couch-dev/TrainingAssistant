package training;

import general.BackButtonActivity;

import java.util.ArrayList;

import main.SavingManager;
import material.Colors;
import material.Exercise;
import material.Workout;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * With this activity the user can specify the properties of his selected workout. For example how much he wants to
 * repeat a specific exercise.
 * @author Tim Reimer
 *
 */
public class Properties1Activity extends BackButtonActivity implements OnItemClickListener, OnItemLongClickListener, OnTouchListener {

	public static final String EXERCISE = "exercise";
	private Workout set;
	private boolean interval;
	private Button setButton;
	private Button intervalButton;
	private ArrayList<Workout> workouts;
	private SavingManager mngr;
	private int pos;
	private int _yDelta;
	private SimpleExerciseAdapter adapter;
	private RelativeLayout itemLayout;
	private boolean dragStarted;
	private Exercise draggedItem;
	private int draggedItemPos;
	private int initialMargin;
	private int oldPos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties1);
        createBackButton();
        
        mngr = SavingManager.getInstance();
        workouts = mngr.loadWorkouts();
        
    	pos = getIntent().getIntExtra(ChooseWorkoutActivity.POSITION, -1);
    	set = workouts.get(pos);
    	ListView lv = (ListView) findViewById(R.id.setList);
    	adapter = new SimpleExerciseAdapter(this, R.layout.item_trainingset_simple, set.getExercises());
    	lv.setAdapter(adapter);
    	lv.setOnItemClickListener(this);
    	lv.setOnItemLongClickListener(this);
    	lv.setOnTouchListener(this);
    	setButton = (Button) findViewById(R.id.setTrainingButton);
    	intervalButton = (Button) findViewById(R.id.intervalTrainingButton);
		switchTrainType(null);
    	if(!set.isInterval()){
    		switchTrainType(null);
    	}
    	
		TextView text = new TextView(this);
		text.setTextColor(getResources().getColor(R.color.black));
		text.setTextAppearance(this, R.style.text_black_20);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 60;
		params.topMargin = 40;
		params.bottomMargin = 40;
		text.setLayoutParams(params);
		text.setId(R.id.text);
		itemLayout = new RelativeLayout(this);
		itemLayout.addView(text);
		
        // apply theme color
		itemLayout.setBackgroundColor(Colors.getThemeColor()-0xcc000000);
        Button next = (Button) findViewById(R.id.nextButton);
        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        next.setBackground(image);
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        divider.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lv.setDivider(divider);
        lv.setDividerHeight(2);
        View lineBottom = findViewById(R.id.lineBottom);
        image = getResources().getDrawable(R.drawable.line);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lineBottom.setBackground(image);
        View lineTop = findViewById(R.id.lineTop);
        lineTop.setBackground(image);
	}

	@Override
	protected void onResume() {
		super.onResume();
        workouts = mngr.loadWorkouts();
    	set = workouts.get(pos);
	}
	
	/**
	 * Switches the training type between set training and interval training.
	 * @param view The clicked view. Here on of the switch buttons.
	 */
	public void switchTrainType(View view) {
		interval = !interval;
		if(!interval){
			Drawable selector = getResources().getDrawable(R.drawable.switch_on_selector);
			selector.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
			setButton.setBackground(selector);
			setButton.setTextColor(getResources().getColor(R.color.white));
			intervalButton.setBackgroundResource(R.drawable.switch_off_selector);
			intervalButton.setTextColor(getResources().getColor(R.color.grey));
		} else{
			Drawable selector = getResources().getDrawable(R.drawable.switch_on_selector);
			selector.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
			intervalButton.setBackground(selector);
			intervalButton.setTextColor(getResources().getColor(R.color.white));
			setButton.setBackgroundResource(R.drawable.switch_off_selector);
			setButton.setTextColor(getResources().getColor(R.color.grey));
		}
		set.setInterval(interval);
		workouts.set(pos, set);
		mngr.saveWorkouts(workouts);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TrainingActivity.DONE && resultCode == TrainingActivity.DONE){
			setResult(TrainingActivity.DONE);
			finish();
		}
	}
	
	/**
	 * Leads the user to the next activity where he can specify another small set of settings.
	 * @param view The clicked view. Here the "next" button.
	 */
	public void next(View view){
		set.setInterval(interval);
		workouts.set(pos, set);
		mngr.saveWorkouts(workouts);
		Intent intent = new Intent(this, Properties2Activity.class);
		intent.putExtra(ChooseWorkoutActivity.POSITION, pos);
		startActivityForResult(intent, TrainingActivity.DONE);
	}
	
	/**
	 * Shows some information about the intensity of this workout to the user.
	 * @param v The clicked view. Here the info button.
	 */
	public void info(View v){
		new WorkoutInfoPopup(this, set);
	}
	
	/**
	 * Saves the properties for one exercise after its properties were edited by the user.
	 * @param position The position of the exercise in the list of exercises.
	 * @param exercise The edited exercise.
	 */
	public void setPropertiesForExercise(int position, Exercise exercise) {
		ArrayList<Exercise> exs = set.getExercises();
		exs.set(position, exercise);
		set.setExercises(exs);
		workouts.set(pos, set);
		mngr.saveWorkouts(workouts);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		new ExercisePropertiesPopup(this, position, set.getExercises().get(position));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int[] location = new int[]{0,0};
		view.getLocationOnScreen(location);
		params.topMargin = location[1]-50;
		itemLayout.setLayoutParams(params);
		TextView text = (TextView) itemLayout.findViewById(R.id.text);
		draggedItem = adapter.getItem(position);
		draggedItemPos = position;
		dragStarted = true;
		oldPos = draggedItemPos;
		text.setText(draggedItem.getName());
    	RelativeLayout root = (RelativeLayout) findViewById(R.id.rootLayout);
		root.addView(itemLayout);
		adapter.dragStarted(position);
		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(draggedItem != null){
		    final int Y = (int) event.getRawY();
        	int height = itemLayout.getHeight();
        	RelativeLayout root = (RelativeLayout) findViewById(R.id.rootLayout);
		    switch (event.getAction() & MotionEvent.ACTION_MASK) {
		        case MotionEvent.ACTION_DOWN:
		            break;
		        case MotionEvent.ACTION_UP:
		    		root = (RelativeLayout) findViewById(R.id.rootLayout);
		    		root.removeView(itemLayout);
		    		draggedItem = null;
		    		ArrayList<Exercise> modified = adapter.dragEnded();
		    		set.setExercises(modified);
		    		workouts.set(pos, set);
		    		mngr.saveWorkouts(workouts);
		            break;
		        case MotionEvent.ACTION_POINTER_DOWN:
		            break;
		        case MotionEvent.ACTION_POINTER_UP:
		            break;
		        case MotionEvent.ACTION_MOVE:
		            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemLayout.getLayoutParams();
		            if(dragStarted){
		            	initialMargin = layoutParams.topMargin;
			            _yDelta = Y - initialMargin;
			            dragStarted = false;
		            }
		            int newMargin = Y - _yDelta;
		            if(newMargin > 150 && newMargin < 900 && height>0){
		            	layoutParams.topMargin = newMargin;
			            int marginDelta = newMargin - initialMargin;
			            int posDelta = marginDelta / height;
			            int newPos = draggedItemPos + posDelta;
			            if(newPos!=oldPos && newPos>=0 && newPos<adapter.getCount()){
			            	adapter.remove(draggedItem);
			            	oldPos = newPos;
			            	adapter.add(newPos, draggedItem);
			            }
		            }
		            itemLayout.setLayoutParams(layoutParams);
		            break;
		    }
		    return true;
		}
	    return false;
	}

}
