package general;

import training.WorkoutsMenuActivity;
import main.SavingManager;
import material.Colors;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.couchdev.trainingassistant.R;

/**
 * An adapter for several menus.
 * TODO: Comment code.
 * @author Tim Reimer
 *
 */
public class MenuAdapter extends ArrayAdapter<String> implements OnClickListener, OnSeekBarChangeListener, OnItemSelectedListener {

	private int resource;
	private String[] sets;
	private boolean[] checkable;
	private boolean[] checked;
	private SavingManager mngr;

	public MenuAdapter(Context context, int resource, String[] items, boolean[] checkable) {
		super(context, resource, items);
		this.resource = resource;
		this.checkable = checkable;
		checked = new boolean[checkable.length];
		sets = items;
		mngr = SavingManager.getInstance();
		boolean[] settings = mngr.loadSettings();
		if(context instanceof WorkoutsMenuActivity){
			checked[0] = settings[SavingManager.WORKOUT_SETTING_ID1];
			checked[1] = settings[SavingManager.WORKOUT_SETTING_ID2];
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view==null){
			LayoutInflater li = LayoutInflater.from(getContext());
			view = li.inflate(resource, null);
		}
		TextView name =  (TextView) view.findViewById(R.id.itemText);
		name.setText(sets[position]);
		Button check = (Button) view.findViewById(R.id.itemCheck);
		if(checkable[position]){
			check.setVisibility(View.VISIBLE);
			check.setOnClickListener(this);
			if(checked[position]){
				Resources res = getContext().getResources();
		        Drawable image = res.getDrawable(R.drawable.check_button2);
		        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
				check.setBackground(image);
			} else{
				check.setBackgroundResource(R.drawable.check_button1);
			}
		} else{
			check.setVisibility(View.GONE);
		}
		if(sets[position].equals("volume")){
			name.setVisibility(View.INVISIBLE);
			View v = new View(getContext());
			Drawable image = getContext().getResources().getDrawable(R.drawable.volume);
			image.setColorFilter(getContext().getResources().getColor(R.color.black), Mode.MULTIPLY);
			v.setBackground(image);
			LayoutParams params = new LayoutParams(70, 70);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.setMargins(40, 0, 0, 0);
			v.setLayoutParams(params);
			v.setId(View.generateViewId());
			((RelativeLayout) view).addView(v);
			SeekBar bar = new SeekBar(getContext());
			params = new LayoutParams(500, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.addRule(RelativeLayout.RIGHT_OF, v.getId());
			params.setMargins(30, 0, 0, 0);
			bar.setLayoutParams(params);
			bar.setOnSeekBarChangeListener(this);
			((RelativeLayout) view).addView(bar);
	        AudioManager audio = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
	        bar.setMax(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
	        double vol = mngr.getVolume();
	        int max = bar.getMax();
	        int progress = (int) (max * vol);
	        bar.setProgress(progress);
		} else if(sets[position].equals("theme color")){
			name.setText(getContext().getResources().getString(R.string.theme_color)+":");
			Spinner colorSpinner = new Spinner(getContext());
			ColorSpinnerAdapter adapter = new ColorSpinnerAdapter(getContext(), R.layout.item_colorspinner, Colors.getColors());
			colorSpinner.setAdapter(adapter);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF, name.getId());
			params.setMargins(20, 20, 0, 0);
			colorSpinner.setLayoutParams(params);
			((RelativeLayout) view).addView(colorSpinner);
			int colorPos = Colors.getColorPosition(Colors.getThemeColor());
			colorSpinner.setSelection(colorPos);
			colorSpinner.setOnItemSelectedListener(this);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v instanceof Button){
			Button check = (Button) v;
			RelativeLayout layout = (RelativeLayout) v.getParent();
			ListView list = (ListView) layout.getParent();
			int position = list.getPositionForView(layout);
			checked[position] = !checked[position];
			if(position == 0){
				mngr.saveSettings(checked[position], SavingManager.WORKOUT_SETTING_ID1);
			} else{
				mngr.saveSettings(checked[position], SavingManager.WORKOUT_SETTING_ID2);
			}
			if(checked[position]){
				Resources res = getContext().getResources();
		        Drawable image = res.getDrawable(R.drawable.check_button2);
		        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
				check.setBackground(image);
				if(position==1){
					Toast.makeText(getContext(), R.string.check_workouts_howto, Toast.LENGTH_LONG).show();
				}
			} else{
				check.setBackgroundResource(R.drawable.check_button1);
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser){
			double max = seekBar.getMax();
			double volume = progress / max;
			mngr.setVolume(volume);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mngr.setThemeColor(Colors.getColor(position));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
