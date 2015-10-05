package main;

import general.MenuActivity;

import java.util.Calendar;

import material.Colors;
import statistics.StatisticsActivity;
import training.ChooseWorkoutActivity;
import workouts.WorkoutsActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.couchdev.trainingassistant.R;

/**
 * This is the initial activity. Here the user can decide where he wants to navigate to within this application.
 * @author Tim Reimer
 *
 */
public class MainActivity extends Activity {

	private SavingManager manager;
	@SuppressWarnings("unused")
	private Colors colors;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        manager = new SavingManager(getApplicationContext());
        manager.loadStatistics();
        colors = new Colors(this);
        
        Calendar calendar = Calendar.getInstance();
        int woy = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        manager.setTime(year, woy);
    }
    
    /**
     * Applies the theme color to all views.
     */
    private void applyThemeColor(){
        Button settings = (Button) findViewById(R.id.settingsButton);
        Drawable image = getResources().getDrawable(R.drawable.settings_button);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        settings.setBackground(image);
        Button stats = (Button) findViewById(R.id.statsButton);
        Button about = (Button) findViewById(R.id.aboutButton);
        Button workouts = (Button) findViewById(R.id.workoutsButton);
        Button training = (Button) findViewById(R.id.startTrainingButton);
        image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        stats.setBackground(image);
        Drawable image2 = getResources().getDrawable(R.drawable.white_button_selector);
        image2.setColorFilter(Colors.lighten(Colors.getThemeColor()), Mode.MULTIPLY);
        about.setBackground(image2);
        Drawable image3 = getResources().getDrawable(R.drawable.white_button_selector);
        image3.setColorFilter(Colors.darken(Colors.getThemeColor()), Mode.MULTIPLY);
        workouts.setBackground(image3);
        Drawable image4 = getResources().getDrawable(R.drawable.white_button_selector);
        image4.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        training.setBackground(image4);
    }
    
    /**
     * Starts the activity that shows a list of all created workouts.
     * @param v The clicked view. Here a button.
     */
    public void trainingSets(View v){
    	Intent intent = new Intent(this, WorkoutsActivity.class);
    	startActivity(intent);
    }

    /**
     * Starts the activity where the user can choose a workout and start the training.
     * @param v The clicked view. Here a button.
     */
    public void startTraining(View v){
    	Intent intent = new Intent(this, ChooseWorkoutActivity.class);
    	startActivity(intent);
    }
    
    /**
     * Starts the activity where the user can the the statistics of his so far training.
     * @param v The clicked view. Here a button.
     */
    public void showStatistics(View v){
    	Intent intent = new Intent(this, StatisticsActivity.class);
    	startActivity(intent);
    }

    /**
     * Starts the activity where the user can get more information about the whole application.
     * @param v The clicked view. Here a button.
     */
    public void about(View v){
    	Intent intent = new Intent(this, AboutActivity.class);
    	startActivity(intent);
    }
    
    /**
     * Starts a {@link MenuActivity} which enables the user to edit the main settings for this application.
     * @param v The clicked view. Here the settings button.
     */
    public void settings(View v){
    	Intent intent = new Intent(this, MainMenuActivity.class);
    	startActivity(intent);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	applyThemeColor();
    }
    
}
