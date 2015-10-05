package training;

import material.Colors;
import material.Exercise;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * This popup can be created by {@link Properties1Activity}. It sets the properties for a single exercise of an existing
 * workout.
 * @author Tim Reimer
 *
 */
public class ExercisePropertiesPopup extends AlertDialog implements android.view.View.OnClickListener {

	private int position;
	private Exercise exercise;
	private Context context;
	private Button repeatsButton;
	private Button timeButton;
	private boolean time;
	private NumberPicker numPicker;
	private TextView secondsText;

	public ExercisePropertiesPopup(Context context, int position, Exercise exercise) {
		super(context);
		this.context = context;
		this.position = position;
		this.exercise = exercise;
		build();
	}
	
	private void build(){      
        final Properties1Activity activity = (Properties1Activity) context;
        
        Builder builder = new Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_exerciseproperties, null);
        repeatsButton = (Button) view.findViewById(R.id.switchRepeats);
        timeButton = (Button) view.findViewById(R.id.switchTime);
        repeatsButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        numPicker = (NumberPicker) view.findViewById(R.id.switchingPicker);
        numPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        secondsText = (TextView) view.findViewById(R.id.secondsText);
        time = exercise.isRepeats();
        onClick(null);
    	if(exercise.getAmount()>5){
    		numPicker.setValue(exercise.getAmount());
    	}
    	
        builder.setView(view);
        builder.setTitle(exercise.getName());
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   exercise.setAmount(numPicker.getValue());
                	   exercise.setRepeats(!time);
                	   activity.setPropertiesForExercise(position, exercise);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        builder.create().show();
	}

	@Override
	public void onClick(View v) {
		if(time){
			timeButton.setBackgroundResource(R.drawable.switch_off_selector);
			Drawable selector = getContext().getResources().getDrawable(R.drawable.switch_on_selector);
			selector.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
			repeatsButton.setBackground(selector);
			repeatsButton.setTextColor(getContext().getResources().getColor(R.color.white));
			timeButton.setTextColor(getContext().getResources().getColor(R.color.grey));
			numPicker.setMinValue(5);
			numPicker.setMaxValue(100);
			secondsText.setVisibility(View.GONE);
			
		} else{
			repeatsButton.setBackgroundResource(R.drawable.switch_off_selector);
			Drawable selector = getContext().getResources().getDrawable(R.drawable.switch_on_selector);
			selector.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
			timeButton.setBackground(selector);
			timeButton.setTextColor(getContext().getResources().getColor(R.color.white));
			repeatsButton.setTextColor(getContext().getResources().getColor(R.color.grey));
			numPicker.setMinValue(10);
			numPicker.setMaxValue(90);
			secondsText.setVisibility(View.VISIBLE);
		}
		time = !time;
	}

}
