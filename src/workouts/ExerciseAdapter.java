package workouts;

import java.util.HashMap;
import java.util.List;

import material.Colors;
import material.Exercise;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * The adapter for selecting exercises while creating a new workout.
 * @author Tim Reimer
 *
 */
public class ExerciseAdapter extends ArrayAdapter<String> implements OnClickListener {

	private int resource;
	private List<String> names;
	private List<Exercise> checked;
	private HashMap<Integer,Integer> selected;
	private boolean isSelectedList;

	public ExerciseAdapter(Context context, int resource, List<String> objects, List<Exercise> checked) {
		super(context, resource, objects);
		this.resource = resource;
		names = objects;
		isSelectedList = checked == null;
		if(checked!=null && !checked.isEmpty()){
			this.checked = checked;
		}
		selected = new HashMap<Integer,Integer>();
	}

	/**
	 * Selects the exercise at position <b>viewPos<b> and stores it along with its position in the list of selected exercises.
	 * @param viewPos The position of the view in the list of all views.
	 * @param exPos The position of this exercise in the workouts list of exercises.
	 */
	public void select(int viewPos, int exPos){
		selected.put(viewPos, exPos);
	}
	
	/**
	 * Deselects the exercise at position <b>viewPos<b>.
	 * @param viewPos The position of the view in the list of all views.
	 */
	public void deselect(int viewPos){
		selected.remove(viewPos);
	}
	
	/**
	 * Refreshes all positions of the exercises when their position have changed after one exercise was deselected.
	 * @param position The position at which an exercise was removed/deselected.
	 */
	public void refreshAll(int position){
		for(int i=0; i<names.size(); i++){
			if(selected!=null){
				if(selected.containsKey(i) && selected.containsKey(position)){
					if(selected.get(i) > selected.get(position)){
						select(i, selected.get(i)-1);
					}
				}
			}
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(getContext());
			view = li.inflate(resource, null);
		}
		TextView name =  (TextView) view.findViewById(R.id.exerciseName);
		TextView pos = (TextView) view.findViewById(R.id.exercisePosition);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.exerciseLayout);
		Button info = (Button) view.findViewById(R.id.infoButton);
		info.setOnClickListener(this);
		if(isSelectedList){
			name.setTypeface(null, Typeface.BOLD);
			name.setText((position+1)+". "+names.get(position));
		}else{
			layout.setOnClickListener(this);
			if(checked!=null && checked.contains(new Exercise(names.get(position)))){
				name.setTypeface(null, Typeface.BOLD);
				name.setTextColor(Colors.getThemeColor());
				pos.setVisibility(View.VISIBLE);
				pos.setTextColor(Colors.getThemeColor());
				pos.setText("" + (checked.indexOf(new Exercise(names.get(position)))+1));
			} else if(selected.containsKey(position)){
				name.setTypeface(null, Typeface.BOLD);
				name.setTextColor(Colors.getThemeColor());
				pos.setVisibility(View.VISIBLE);
				pos.setTextColor(Colors.getThemeColor());
				pos.setText("" + selected.get(position));
			} else{
				pos.setVisibility(View.GONE);
				name.setTypeface(null, Typeface.NORMAL);
				name.setTextColor(getContext().getResources().getColor(R.color.black));
			}
			name.setText(names.get(position));
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v instanceof RelativeLayout){
			ListView lv = (ListView) v.getParent();
			for(int i=0; i< lv.getCount(); i++){
				if(lv.getPositionForView(v)==i){
					((ChooseExercisesActivity) getContext()).onItemClick(lv, v, i);
					break;
				}
			}
		} else if(v instanceof Button){
			RelativeLayout parent = (RelativeLayout) v.getParent();
			ListView lv = (ListView) parent.getParent();
			for(int i=0; i< lv.getCount(); i++){
				if(lv.getPositionForView(parent)==i){
					new ExerciseInfoPopup(getContext(), names.get(i));
					break;
				}
			}
		}
	}

}
