package training;

import java.util.HashSet;
import java.util.List;

import main.SavingManager;
import material.Workout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * The adapter for displaying all workouts and change their appearance based on whether they are checked or not.
 * @author Tim Reimer
 *
 */
public class SimpleWorkoutAdapter extends ArrayAdapter<Workout>{

	private int resource;
	private List<Workout> sets;

	public SimpleWorkoutAdapter(Context context, int resource, List<Workout> objects) {
		super(context, resource, objects);
		this.resource = resource;
		sets = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(getContext());
			view = li.inflate(resource, null);
		}
		TextView name =  (TextView) view.findViewById(R.id.trainingSetName);
		String text = sets.get(position).getName();
		name.setText(text);
		SavingManager mngr = SavingManager.getInstance();
		mngr.loadSettings();
		HashSet<String> checked = mngr.getWeekWorkouts();
		if(!checked.isEmpty()){
			View line = view.findViewById(R.id.checkLine);
			if(checked.contains(text)){
				name.setTextColor(getContext().getResources().getColor(R.color.grey));
				line.setVisibility(View.VISIBLE);
			} else{
				name.setTextColor(getContext().getResources().getColor(R.color.black));
				line.setVisibility(View.GONE);
			}
		}
		return view;
	}

}
