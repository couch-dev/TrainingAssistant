package general;

import material.Colors;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import de.couchdev.trainingassistant.R;

/**
 * An activity that can be extended if you want your activity to have a custom menu button as well as a back button.
 * This class provides an abstract method {@link #menu(View)} that has to be the defined {@code onClick} method of the button.
 * It also has to be implemented by your activity.
 * To make the menu button visible with the theme color of this application you have to call {@link #createMenuButton()} in your
 * {@code onCreate} method.
 * @see BackButtonActivity
 * @author Tim Reimer
 *
 */
public abstract class BackAndMenuButtonActivity extends BackButtonActivity{
	
	/**
	 * Makes the button with the id <i>menuButton</i> visible and colors it in the theme color of this application
	 * which has to be defined in {@code R.color.theme}. The custom drawable for this button has to be defined in
	 * {@code R.drawable.menu_button}.
	 */
	public void createMenuButton(){
        Button menu = (Button) findViewById(R.id.menuButton);
        Drawable image = getResources().getDrawable(R.drawable.menu_button);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        menu.setBackground(image);
        menu.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Implement this method to e.g. start a new activity that provides a menu.
	 * @param v The clicked view. Here the menu button.
	 */
	public abstract void menu(View v);
}
