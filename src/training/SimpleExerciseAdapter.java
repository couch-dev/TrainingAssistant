package training;

import java.util.ArrayList;

import material.Colors;
import material.Exercise;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * The adapter for displaying the exercises of one workout in the {@link Properties1Activity}.
 * @author Tim Reimer
 *
 */
public class SimpleExerciseAdapter extends ArrayAdapter<Exercise> {

	private int resource;
	private ArrayList<Exercise> sets;
	private int newPosition;

	public SimpleExerciseAdapter(Context context, int resource, ArrayList<Exercise> objects) {
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
		if(position == newPosition){
			name.setText("");
			view.setBackgroundColor(Colors.getThemeColor());
		} else{
			name.setText(sets.get(position).getName());
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

}
