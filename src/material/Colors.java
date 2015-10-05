package material;

import java.util.ArrayList;

import main.SavingManager;
import de.couchdev.trainingassistant.R;
import android.content.Context;
import android.graphics.Color;

/**
 * This class is needed if theme color could be changed by the user.
 * 
 * @author Tim Reimer
 *
 */
public class Colors {

	private static ArrayList<Integer> colors;
	private static int theme;

	public Colors(Context context){
		colors = new ArrayList<Integer>();
        colors.add(context.getResources().getColor(R.color.brown));
        colors.add(context.getResources().getColor(R.color.dark_red));
        colors.add(context.getResources().getColor(R.color.red));
        colors.add(context.getResources().getColor(R.color.orange));
        colors.add(context.getResources().getColor(R.color.yellow));
        colors.add(context.getResources().getColor(R.color.green));
        colors.add(context.getResources().getColor(R.color.mint));
        colors.add(context.getResources().getColor(R.color.dark_green));
        colors.add(context.getResources().getColor(R.color.cyan));
        colors.add(context.getResources().getColor(R.color.blue));
        colors.add(context.getResources().getColor(R.color.dark_blue));
        colors.add(context.getResources().getColor(R.color.violet));
        colors.add(context.getResources().getColor(R.color.purple));
        colors.add(context.getResources().getColor(R.color.black));
        
        SavingManager mngr = SavingManager.getInstance();
        int color = mngr.getThemeColor();
        if(colors.contains(color)){
        	theme = color;
        } else{
        	theme = colors.get(3);
        	mngr.setThemeColor(theme);
        }
	}
	
	public static int getThemeColor(){
		return theme;
	}
	
	public static void setThemeColor(int position){
		theme = colors.get(position);
	}
	
	public static ArrayList<Integer> getColors(){
		return colors;
	}

	public static int getColorPosition(int color){
		return colors.indexOf(color);
	}
	
	public static int getColor(int position){
		return colors.get(position);
	}

	public static int lighten(int color){
        float[] hsv = new float[]{0.0f, 0.0f, 0.0f};
        Color.colorToHSV(color, hsv);
        hsv[0] = (hsv[0]+10) > 360? hsv[0]+10-360: hsv[0]+10;
        hsv[2] = (hsv[2]+0.2f) > 1.0f ? 1.0f : hsv[2]+0.2f;
        return Color.HSVToColor(hsv);
	}
	
	public static int lightenSlightly(int color){
        float[] hsv = new float[]{0.0f, 0.0f, 0.0f};
        Color.colorToHSV(color, hsv);
        hsv[0] = (hsv[0]+5) > 360? hsv[0]+5-360: hsv[0]+5;
        hsv[2] = (hsv[2]+0.1f) > 1.0f ? 1.0f : hsv[2]+0.1f;
        return Color.HSVToColor(hsv);
	}
	
	public static int darken(int color){
        float[] hsv = new float[]{0.0f, 0.0f, 0.0f};
        Color.colorToHSV(color, hsv);
        hsv[0] = (hsv[0]-10) < 0? hsv[0]-10+360: hsv[0]-10;
        hsv[2] = (hsv[2]-0.2f) < 0.0f ? 0.0f : hsv[2]-0.2f;
        return Color.HSVToColor(hsv);
	}
}
