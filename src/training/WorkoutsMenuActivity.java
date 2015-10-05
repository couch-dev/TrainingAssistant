package training;

import material.Colors;
import general.MenuActivity;
import general.MenuAdapter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.couchdev.trainingassistant.R;

/**
 * This is the menu for {@link ChooseWorkoutActivity}. Here the user can set if and how the workouts get checked after it was
 * finished.
 * @author Tim Reimer
 *
 */
public class WorkoutsMenuActivity extends MenuActivity implements OnItemClickListener  {

    private String[] items;
	private boolean[] checkable;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        
        items = getResources().getStringArray(R.array.workoutsMenu);
        checkable = new boolean[items.length];
        checkable[0] = true;
        checkable[1] = true;
        ListView lv = (ListView) findViewById(R.id.menuList);
    	MenuAdapter adapter = new MenuAdapter(this, R.layout.item_menu, items, checkable);
    	lv.setAdapter(adapter);
    	lv.setOnItemClickListener(this);
    	
        // apply theme color
        Button done = (Button) findViewById(R.id.doneButton);
        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        done.setBackground(image);
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        divider.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lv.setDivider(divider);
        lv.setDividerHeight(2);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void applySetting(int position) {
	}
}
