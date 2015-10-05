package general;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import de.couchdev.trainingassistant.R;

public class ColorSpinnerAdapter implements SpinnerAdapter {

	private Context context;
	private int resource;
	private ArrayList<Integer> colors;

	public ColorSpinnerAdapter(Context context, int itemResource, ArrayList<Integer> colors) {
		this.context = context;
		this.resource = itemResource;
		this.colors = colors;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public int getCount() {
		return colors.size();
	}

	@Override
	public Object getItem(int position) {
		return colors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0L;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(context);
			view = li.inflate(resource, null);
		}
		View color = view.findViewById(R.id.colorView);
		color.setBackgroundColor(colors.get(position));
		return view;
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return colors.isEmpty();
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

}
