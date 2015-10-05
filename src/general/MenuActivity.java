package general;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import de.couchdev.trainingassistant.R;

/**
 * This is the superclass for all menus.
 * @author Tim Reimer
 *
 */
public abstract class MenuActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);   
        
    }
    
	public void done(View v){
		finish();
	}
	
	public abstract void applySetting(int position);
}
