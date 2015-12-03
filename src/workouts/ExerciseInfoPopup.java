package workouts;

import java.util.ArrayList;

import material.Exercises;
import material.Tags;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Html;
import de.couchdev.trainingassistant.R;

/**
 * This popup only shows some information about a single exercise to the user. It shows what {@link Tags} are linked to it.
 * @author Tim Reimer
 *
 */
public class ExerciseInfoPopup extends AlertDialog {

	private String exercise;

	public ExerciseInfoPopup(Context context, String exercise) {
		super(context);
		this.exercise = exercise;
		build();
	}
	
	private void build(){      
        Builder builder = new Builder(getContext(), R.style.ThemeDialog);
        builder.setTitle(exercise);
        String message = getContext().getString(R.string.this_exercise_trains)+" ";
        ArrayList<String> tags = Exercises.getTags(exercise, getContext());
        for(String tag: tags){
        	message += "<b>"+tag+"</b>, ";
        }
        message = message.substring(0, message.length()-2)+".";
        String note = Exercises.getNoteFor(exercise, getContext());
        if(note != null){
        	message += "<br><i>"+getContext().getString(R.string.you_need)+"</i> "+note+".";
        }
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        builder.create().show();
	}
	
}
