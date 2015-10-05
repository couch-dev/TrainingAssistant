package training;

import general.BackButtonActivity;
import java.util.ArrayList;
import main.SavingManager;
import material.Colors;
import material.Workout;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * This is an addition to {@link Properties1Activity} where the user can edit a few more settings.
 * @author Tim Reimer
 *
 */
public class Properties2Activity extends BackButtonActivity{

	private SavingManager mngr;
	private Workout set;
	private int pos;
	private ArrayList<Workout> trainingSets;
	private NumberPicker np;
	private NumberPicker np2;

	@SuppressLint("DefaultLocale") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties2);
        createBackButton();
        
        mngr = SavingManager.getInstance();
        trainingSets = mngr.loadWorkouts();
    	pos = getIntent().getIntExtra(ChooseWorkoutActivity.POSITION, -1);
    	set = trainingSets.get(pos);
    	
    	TextView text = (TextView) findViewById(R.id.propertiesText);
    	np = (NumberPicker) findViewById(R.id.numberPicker);
    	np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    	np.setMinValue(1);
    	np.setMaxValue(10);
    	np.setValue(set.getIterations());
    	np2 = (NumberPicker) findViewById(R.id.numberPicker2);
    	np2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    	np2.setMinValue(10);
    	np2.setMaxValue(60);
    	np2.setValue(set.getPauseTime());
    	if(set.isInterval()){
    		text.setText(getString(R.string.interval_training)+" "+getString(R.string.training).toLowerCase());
    	} else{
    		text.setText(getString(R.string.set_training)+" "+getString(R.string.training).toLowerCase());
    	}

        // apply theme color
        Button back = (Button) findViewById(R.id.backPropButton);
        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        back.setBackground(image);
        Button done = (Button) findViewById(R.id.startButton);
        Drawable image2 = getResources().getDrawable(R.drawable.white_button_selector);
        image2.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        done.setBackground(image2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TrainingActivity.DONE && resultCode == TrainingActivity.DONE){
			setResult(TrainingActivity.DONE);
			finish();
		}
	}
	
	/**
	 * Starts the guided training.
	 * @param view The clicked view. Here the "done" button.
	 */
	public void done(View view){
		set.setIterations(np.getValue());
		set.setPauseTime(np2.getValue());
		trainingSets.set(pos, set);
		mngr.saveWorkouts(trainingSets);
		Intent intent = new Intent(this, TrainingActivity.class);
		intent.putExtra(ChooseWorkoutActivity.POSITION, pos);
		startActivityForResult(intent, TrainingActivity.DONE);
	}
	
	@Override
	public void back(View v) {
		super.back(v);
		set.setIterations(np.getValue());
		set.setPauseTime(np2.getValue());
		trainingSets.set(pos, set);
		mngr.saveWorkouts(trainingSets);
	}
	
}
