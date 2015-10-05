package material;

import java.util.ArrayList;

import training.TagInfoAdapter;
import android.content.Context;
import de.couchdev.trainingassistant.R;

/**
 * An enum of all tags. A tag is a part of the body which can be affected by several exercises.
 * @author Tim Reimer
 *
 */
public enum Tags {

	CALVES(R.string.calves),
	THIGHS(R.string.thighs),
	BUTTOCKS(R.string.buttocks),
	ABS(R.string.abs),
	SIDEABS(R.string.sideabs),
	BREAST(R.string.breast),
	SHOULDERS(R.string.shoulders),
	BACK(R.string.back_tag),
	BICEPS(R.string.biceps),
	TRICEPS(R.string.triceps),
	FOREARMS(R.string.forearms),
	STAMINA(R.string.stamina),
	NECK(R.string.neck);
	
	private int id;
	
	Tags(int resID){
		id = resID;
	}
	
	/**
	 * Get the string for this tag.
	 * @param context The context where this method is called.
	 * @return The string representation of this tag.
	 */
	public String getString(Context context){
		return context.getString(id);
	}
	

	/**
	 * Get the color that is linked to the <b>tag</b>. (For the colored bars)
	 * @param tag The tag to get the color for.
	 * @param context The context where this method is called.
	 * @see TagInfoAdapter
	 * @return A single color value in the form 0xAARRGGBB.
	 */
	public static int getColor(String tag, Context context){
		int color = -1;
		if(tag.equals(NECK.getString(context))){
			color = Colors.getColor(0);
		} else if(tag.equals(SHOULDERS.getString(context))){
			color = Colors.getColor(1);
		} else if(tag.equals(BICEPS.getString(context))){
			color = Colors.getColor(2);
		} else if(tag.equals(TRICEPS.getString(context))){
			color = Colors.getColor(3);
		} else if(tag.equals(FOREARMS.getString(context))){
			color = Colors.getColor(4);
		} else if(tag.equals(BREAST.getString(context))){
			color = Colors.getColor(5);
		} else if(tag.equals(ABS.getString(context))){
			color = Colors.getColor(6);
		} else if(tag.equals(SIDEABS.getString(context))){
			color = Colors.getColor(7);
		} else if(tag.equals(BACK.getString(context))){
			color = Colors.getColor(8);
		} else if(tag.equals(BUTTOCKS.getString(context))){
			color = Colors.getColor(9);
		} else if(tag.equals(THIGHS.getString(context))){
			color = Colors.getColor(10);
		} else if(tag.equals(CALVES.getString(context))){
			color = Colors.getColor(11);
		} else if(tag.equals(STAMINA.getString(context))){
			color = Colors.getColor(12);
		} else{
			color = Colors.getThemeColor();
		}
		return color;
	}
	
	/**
	 * Get all possible tags as list.
	 * @param context The context where this method is called.
	 * @return All tags as {@code ArrayList<String>}.
	 */
	public static ArrayList<String> getTagsList(Context context) {
		ArrayList<String> tags = new ArrayList<String>();
		tags.add(NECK.getString(context)); tags.add(SHOULDERS.getString(context)); tags.add(BICEPS.getString(context));
		tags.add(TRICEPS.getString(context)); tags.add(FOREARMS.getString(context)); tags.add(BREAST.getString(context));
		tags.add(ABS.getString(context)); tags.add(SIDEABS.getString(context)); tags.add(BACK.getString(context));
		tags.add(BUTTOCKS.getString(context)); tags.add(THIGHS.getString(context)); tags.add(CALVES.getString(context));
		tags.add(STAMINA.getString(context));
		return tags;
	}
}
