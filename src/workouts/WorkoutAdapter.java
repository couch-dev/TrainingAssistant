package workouts;

import java.util.List;

import material.Workout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * This adapter displays all workouts along with delete buttons for each one.
 * @author Tim Reimer
 *
 */
public class WorkoutAdapter extends ArrayAdapter<Workout> implements OnClickListener {

	private int resource;
	private List<Workout> sets;
	private Context context;

	public WorkoutAdapter(Context context, int resource, List<Workout> objects) {
		super(context, resource, objects);
		this.resource = resource;
		this.context = context;
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
		name.setText(sets.get(position).getName());
		ImageButton delete = (ImageButton) view.findViewById(R.id.deleteSet);
		delete.setOnClickListener(this);
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.trainingSetLayout);
		rl.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v instanceof ImageButton){
			ListView lv = (ListView) v.getParent().getParent();
			RelativeLayout parent = (RelativeLayout) v.getParent();
			for(int i=0; i< lv.getCount(); i++){
				if(lv.getPositionForView(parent)==i && v.equals(parent.getChildAt(1))){
					((WorkoutsActivity) context).askRemoveSet(i);
					break;
				}
			}
		} else if(v instanceof RelativeLayout){
			ListView lv = (ListView) v.getParent();
			for(int i=0; i< lv.getCount(); i++){
				if(lv.getPositionForView(v)==i){
					((WorkoutsActivity) context).onClickItemAt(i);
					break;
				}
			}
		}
	}

}
