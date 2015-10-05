package statistics;

import general.MenuActivity;
import general.MenuAdapter;
import main.SavingManager;
import material.Colors;
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
 * This is the menu for {@link StatisticsActivity}. Here the user can reset statistics.
 * @author Tim Reimer
 *
 */
public class StatisticsMenuActivity extends MenuActivity implements OnItemClickListener  {

    private String[] items;
    private boolean[] checkable;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        
        items = getResources().getStringArray(R.array.statisticsMenu);
        checkable = new boolean[items.length];
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
		new ApplySettingsPopup(this, position, items[position]);
	}

	@Override
	public void applySetting(int position) {
		SavingManager mngr = SavingManager.getInstance();
		if(position == 0){ // reset statistics of this week
			mngr.resetStatistic(false);
		} else if(position == 1){ // reset all statistics
			mngr.resetStatistic(true);
		}
	}
}
