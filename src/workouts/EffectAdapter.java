package workouts;

import java.util.ArrayList;

import material.Colors;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * An adapter for showing all effects providing the possibility of selecting them.
 * @author Tim Reimer
 *
 */
public class EffectAdapter extends ArrayAdapter<String> {

	private int resource;
	private boolean[] checked;

	public EffectAdapter(Context context, int resource, ArrayList<String> objects) {
		super(context, resource, objects);
		this.resource = resource;
		checked = new boolean[objects.size()];
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(getContext());
			view = li.inflate(resource, null);
		}
		TextView text = (TextView) view.findViewById(R.id.itemText);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(30, 40, 30, 40);
		text.setLayoutParams(params);
		text.setText(getItem(position));
		final Button check = (Button) view.findViewById(R.id.itemCheck);
		updateImage(check, position);
		check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checked[position] = !checked[position];
				updateImage(check, position);
			}
		});
		return view;
	}

	/**
	 * Get information about which items are currently selected.
	 * @return An array of boolean values where {@code true} means the item is selected/checked.
	 */
	public boolean[] getChecked(){
		return checked;
	}
	
	/**
	 * Refreshes the view of the check button at the <b>position</b>.
	 * @param check The check button to update.
	 * @param position The position of the button.
	 */
	private void updateImage(Button check, int position){
		if(checked[position]){
			Resources res = getContext().getResources();
	        Drawable image = res.getDrawable(R.drawable.check_button2);
	        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
			check.setBackground(image);
		} else{
			check.setBackgroundResource(R.drawable.check_button1);
		}
	}

}
