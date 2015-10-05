package workouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import de.couchdev.trainingassistant.R;

/**
 * This is shown to the user when he wants to delete a workout. He can choose to cancel or to delete it.
 * @author Tim Reimer
 *
 */
public class DeleteWorkoutPopup extends AlertDialog {

	private Context context;
	private int position;
	private String name;

	public DeleteWorkoutPopup(Context context, int position, String name) {
		super(context);
		this.context = context;
		this.position = position;
		this.name = name;
		build();
	}
	
	private void build(){      
        final WorkoutsActivity activity = (WorkoutsActivity) context;
        
        Builder builder = new Builder(context);
        builder.setTitle(R.string.delete_workout);
        builder.setMessage(Html.fromHtml(getContext().getString(R.string.really_delete_workout)+": <b>"+name+"</b>"));
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   activity.removeWorkout(position);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        builder.create().show();
	}
	
}
