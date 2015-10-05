package general;

import material.Colors;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import de.couchdev.trainingassistant.R;

/**
 * An activity that can be extended for a custom back button.
 * This class provides the method {@link #back(View)} that has to be the defined {@code onClick} method of the button.
 * It just finishes the invoking activity.<br>
 * To make the button visible with the theme color of this application you have to call {@link #createBackButton()} in your
 * {@code onCreate} method.<br><br>
 * <b>This class is the same as {@link BackButtonActivity} except that it extends {@link FragmentActivity}.</b>
 * @author Tim Reimer
 *
 */
public class BackButtonFragmentActivity extends FragmentActivity{

	/**
	 * Makes the button with the id <i>backButton</i> visible and colors it in the theme color of this application
	 * which has to be defined in {@code R.color.theme}. The custom drawable for this button has to be defined in
	 * {@code R.drawable.back_button}.
	 */
	public void createBackButton(){
        Button back = (Button) findViewById(R.id.backButton);
        Drawable image = getResources().getDrawable(R.drawable.back_button);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        back.setBackground(image);
        back.setVisibility(View.VISIBLE);
        View line = findViewById(R.id.line);
        image = getResources().getDrawable(R.drawable.line);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        line.setBackground(image);
	}
	
	/**
	 * Finishes the activity. Has the same effect as calling {@link #finish()}.
	 * @param v The clicked view. Here the back button.
	 */
	public void back(View v){
		finish();
	}
}
