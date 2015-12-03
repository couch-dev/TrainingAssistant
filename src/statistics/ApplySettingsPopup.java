package statistics;

import general.MenuActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import de.couchdev.trainingassistant.R;

/**
 * This popup is shown to the user when he wants to apply some settings. He then has to confirm to apply them.
 * @author Tim Reimer
 *
 */
public class ApplySettingsPopup extends AlertDialog {

	private int position;
	private String name;
	private Context context;

	public ApplySettingsPopup(Context context, int position, String name) {
		super(context);
		this.context = context;
		this.position = position;
		this.name = name;
		build();
	}
	
	private void build(){      
		final MenuActivity activity = (MenuActivity) context;
        Builder builder = new Builder(getContext(), R.style.ThemeDialog);
        builder.setTitle(R.string.apply_settings);
        builder.setMessage(name);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   activity.applySetting(position);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        builder.create().show();
	}
	
}
