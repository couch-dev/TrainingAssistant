package training;

import java.util.ArrayList;

import material.Colors;
import material.Exercise;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * The adapter for displaying the exercises of one workout in the {@link Properties1Activity}.
 * @author Tim Reimer
 *
 */
public class ExerciseAdapter extends ArrayAdapter<Exercise> implements OnTouchListener {

	private int resource;
	private ArrayList<Exercise> sets;
	private int newPosition;

	public ExerciseAdapter(Context context, int resource, ArrayList<Exercise> objects) {
		super(context, resource, objects);
		this.resource = resource;
		sets = objects;
		newPosition = -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(getContext());
			view = li.inflate(resource, null);
		}
		TextView name =  (TextView) view.findViewById(R.id.trainingSetName);
		View image = view.findViewById(R.id.changePosView);
		image.setOnTouchListener(this);
		if(position == newPosition){
			name.setText("");
			image.setVisibility(View.GONE);
			view.setBackgroundColor(Colors.getThemeColor());
		} else{
			name.setText(sets.get(position).getName());
			Drawable img = getContext().getResources().getDrawable(R.drawable.change_pos);
	        img.setColorFilter(Colors.darken(Colors.getThemeColor()), Mode.MULTIPLY);
	        image.setBackground(img);
			image.setVisibility(View.VISIBLE);
			view.setBackgroundResource(R.color.transparent);
		}
		return view;
	}

	/**
	 * Add the dragged item to the new position.
	 * @param location The new index at which the item is to add.
	 * @param exercise The item to add at <b>location</b>.
	 */
	public void add(int location, Exercise exercise) {
		sets.add(location, exercise);
		newPosition = location;
		notifyDataSetChanged();
	}
	
	/**
	 * Tells this adapter that the drag event has been started so it can update the view.
	 * @param position The position of the item that is dragged.
	 */
	public void dragStarted(int position){
		newPosition = position;
		notifyDataSetChanged();
	}
	
	/**
	 * Notify this adapter that the drag event has ended so it can update the view.
	 * @return The modified list of exercises.
	 */
	public ArrayList<Exercise> dragEnded(){
		newPosition = -1;
		notifyDataSetChanged();
		return sets;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
    		RelativeLayout layout = (RelativeLayout) v.getParent();
    		ListView lv = (ListView) layout.getParent();
    		int pos = -1;
    		for(int i=0; i < lv.getChildCount(); i++){
    			if(lv.getChildAt(i) == layout){
    				pos = i;
    				break;
    			}
    		}
    		((Properties1Activity) getContext()).initiateItemDrag(layout, pos);
            break;
		}
		return false;
	}

}
