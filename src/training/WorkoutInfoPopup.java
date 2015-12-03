package training;

import java.util.ArrayList;
import java.util.HashMap;

import material.Tags;
import material.Workout;
import material.Exercises;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Utilities;

/**
 * This popup also comes from {@link Properties1Activity}. It shows information about the intensity of an existing workout
 * and also which {@link Tags} it is affects.
 * @author Tim Reimer
 *
 */
public class WorkoutInfoPopup extends AlertDialog {

	private Workout workout;
	private TagInfoAdapter tia;

	public WorkoutInfoPopup(Context context, Workout workout) {
		super(context);
		this.workout = workout;
		build();
	}
	
	private void build(){      
        Builder builder = new Builder(getContext(), R.style.ThemeDialog);
        builder.setTitle(workout.getName());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_workoutinfo, null);
        TextView text = (TextView) view.findViewById(R.id.ratingText);
        ListView list = (ListView) view.findViewById(R.id.tagsList);
        ImageView[] star = new ImageView[5];
        star[0] = (ImageView) view.findViewById(R.id.star1);
        star[1] = (ImageView) view.findViewById(R.id.star2);
        star[2] = (ImageView) view.findViewById(R.id.star3);
        star[3] = (ImageView) view.findViewById(R.id.star4);
        star[4] = (ImageView) view.findViewById(R.id.star5);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Double> values = new ArrayList<Double>();
		HashMap<String, Double> intensities = Utilities.sortHashMapByValues(workout.getIntensities(getContext()), false);
        for(String key: intensities.keySet()){
        	double intens = intensities.get(key);
        	names.add(key);
        	values.add(intens);
        }
        tia = new TagInfoAdapter(getContext(), R.layout.item_taginfo, names, values);
        list.setAdapter(tia);
        double intensity = workout.getFinalIntensity(getContext());
        double stars = Exercises.getStars(intensity);
        int i = 1;
        while(i <= stars){
    		Drawable image = getContext().getResources().getDrawable(R.drawable.star_full);
            star[i-1].setImageDrawable(image);
            i++;
    	}
        if(stars > i-1){
    		Drawable image = getContext().getResources().getDrawable(R.drawable.star_half);
            star[i-1].setImageDrawable(image);
        }
    	text.setText(getContext().getString(R.string.rating)+": ");
        builder.setView(view);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        builder.create().show();
	}
	
}
