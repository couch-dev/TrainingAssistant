package training;

import java.util.List;

import material.Tags;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Utilities;

/**
 * This adapter shows a list of tags of one workout along with their intensity values as colored bars. 
 * @author Tim Reimer
 *
 */
public class TagInfoAdapter extends ArrayAdapter<String> {

	private int resource;
	private List<Double> values;

	public TagInfoAdapter(Context context, int resource, List<String> objects, List<Double> values) {
		super(context, resource, objects);
		this.resource = resource;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(getContext());
			view = li.inflate(resource, null);
		}
		TextView text = (TextView) view.findViewById(R.id.tagText);
		String item = getItem(position);
		text.setText(item.equals("Seitliche Bauchmuskeln") ? "S. Bauchmuskeln:" : item+":");
		//View full = view.findViewById(R.id.fullBar);
		View intens = view.findViewById(R.id.intensityBar);
		double max = Utilities.max(values)[0];
		double width = 300;//full.getWidth(); // in pixels
		width = width*values.get(position)/max;
		width = width == 0 ? 1 : width;
		intens.getLayoutParams().width = (int) Math.round(width);
		intens.setBackgroundColor(Tags.getColor(item, getContext()));
		return view;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
