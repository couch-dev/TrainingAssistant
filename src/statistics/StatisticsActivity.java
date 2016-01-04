package statistics;

import general.BackAndMenuButtonActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import training.TagInfoAdapter;
import main.SavingManager;
import material.Colors;
import material.Exercises;
import material.Tags;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Android;
import de.couchdev.utils.Format;
import de.couchdev.utils.Utilities;

/**
 * This activity provides an overview over the users so far training with a few statistical values.
 * @author Tim Reimer
 *
 */
public class StatisticsActivity extends BackAndMenuButtonActivity{

	private boolean topScrolled;
	private boolean statsAvailable;
	private SavingManager mngr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        createBackButton();
        createMenuButton();
        
        mngr = SavingManager.getInstance();
        
        // apply theme color
        View lineBottom = findViewById(R.id.lineBottom);
        Drawable image = getResources().getDrawable(R.drawable.line);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        lineBottom.setBackground(image);
        View lineTop = findViewById(R.id.lineTop);
        lineTop.setBackground(image);
	}
	
	/**
	 * Here all the statistical measurements happen.
	 */
	@Override
	protected void onResume() {
		super.onResume();
        setWeek();
        setStars();
        setAverageNumber();
        setFavorites();
        setTagStatistics();
        setUntrained();
        TextView notAvailable = (TextView) findViewById(R.id.notAvailable);
        if(statsAvailable){
        	notAvailable.setVisibility(View.GONE);
        } else{
        	notAvailable.setVisibility(View.VISIBLE);
        }
	}
	
	private void setStars(){
		TextView intensText = (TextView) findViewById(R.id.intensityText);
        ArrayList<Double> intensities = mngr.getIntensities();
        ImageView[] star = new ImageView[5];
        star[0] = (ImageView) findViewById(R.id.star1);
        star[1] = (ImageView) findViewById(R.id.star2);
        star[2] = (ImageView) findViewById(R.id.star3);
        star[3] = (ImageView) findViewById(R.id.star4);
        star[4] = (ImageView) findViewById(R.id.star5);
        if(intensities.isEmpty()){
        	intensText.setText("");
        	intensText.setVisibility(View.GONE);
        	for(int i=0; i<5; i++){
        		star[i].setVisibility(View.GONE);
        	}
        	statsAvailable = statsAvailable || false;
        } else{
            double mean = Utilities.sum(intensities) / (double) intensities.size();
            intensText.setVisibility(View.VISIBLE);
        	for(int i=0; i<5; i++){
        		star[i].setVisibility(View.VISIBLE);
        	}
            intensText.setText(getString(R.string.overall_rating)+":");
            double stars = Exercises.getStars(mean);
            int i = 1;
            while(i <= stars){
        		Drawable image = getResources().getDrawable(R.drawable.star_full);
                star[i-1].setImageDrawable(image);
                i++;
        	}
            if(stars > i-1){
        		Drawable image = getResources().getDrawable(R.drawable.star_half);
                star[i-1].setImageDrawable(image);
            }
        	statsAvailable = true;
        }		
	}
	
	private void setAverageNumber(){
        int startWeek = mngr.getStartWeek();
        int woy = mngr.getWeekOfYear();
        int[][] stats = mngr.getStatistics();
        int yearIndex = mngr.getYearIndex();
        double avg = 0;
        String avgText = "";
        TextView text3 = (TextView) findViewById(R.id.wosPerWeek);
        if(startWeek!=0 && startWeek!=woy){
	        for(int i=startWeek; i<woy; i++){
	        	int stat = stats[yearIndex][i];
	        	avg += stat;
	        }
	        avg = avg/(double)(woy-startWeek);
	        int avg1 = (int) avg;
	        int avg2 = (int) ((avg - (double)avg1)*100);
	        avgText = "<b>"+avg1+"."+Format.int2str(avg2, 2)+"</b> "+getString(R.string.workouts_per_week);
	        text3.setText(Html.fromHtml(avgText));
	        text3.setVisibility(View.VISIBLE);
        	statsAvailable = true;
        } else{
        	text3.setVisibility(View.GONE);
        	statsAvailable = statsAvailable || false;
        }
	}

	private void setFavorites(){
        HashMap<String, Integer> exStats = mngr.getExerciseStatistics();
        Collection<Integer> values = null;
        if(!exStats.isEmpty()){
        	values = exStats.values();
        }
        String exText = "";
        if(values!=null){
        	ArrayList<Integer> vals = new ArrayList<Integer>();
        	for(Integer i: values){
        		if(i > 2){
        			vals.add(i);
        		}
        	}
        	if(!vals.isEmpty()){
	        	Collections.sort(vals);
	            int max = vals.get(vals.size()-1);
	            ArrayList<String> names = new ArrayList<String>();
	            for(String name: exStats.keySet()){
	            	if(exStats.get(name) == max){
	            		names.add(name);
	            	}
	            }
	            if(names.size() == 1){
	            	exText = getString(R.string.favorite_is)+" <b>"+names.get(0)+"</b>.";
	            } else if(names.size() > 1){
	            	exText = getString(R.string.favorites_are)+": ";
	            	for(int i=0; i<5; i++){
	            		if(names.size()<=i){
	            			break;
	            		}
	            		String name = names.get(i);
	            		exText += "<b>"+name+"</b>, ";
	            	}
	            	exText = exText.substring(0, exText.length()-2);
	            	if(names.size()>5){
	            		exText += " "+getString(R.string.and_more);
	            	}
	            	exText += ".";
	            }
	            TextView text2 = (TextView) findViewById(R.id.favosText);
	            text2.setVisibility(View.VISIBLE);
	            text2.setText(Html.fromHtml(exText));
	        	statsAvailable = true;
        	} else{
	            TextView text2 = (TextView) findViewById(R.id.favosText);
	            text2.setVisibility(View.GONE);
	        	statsAvailable = statsAvailable || false;
        	}
        } else{
            TextView text2 = (TextView) findViewById(R.id.favosText);
            text2.setVisibility(View.GONE);
        	statsAvailable = statsAvailable || false;
        }
	}
	
	private void setTagStatistics(){
		ListView tagsListView = (ListView) findViewById(R.id.tagsList);
		TextView text = (TextView) findViewById(R.id.trainingText);
        LinkedHashMap<String, Double> tags = new LinkedHashMap<String, Double>();
        for(String s: Tags.getTagsList(this)){
        	tags.put(s, 0.0);
        }
        HashMap<String, Integer> exercises = mngr.getExerciseStatistics();
        for(String ex: exercises.keySet()){
        	for(String tag: tags.keySet()){
        		double intens = Exercises.getIntensity(tag, ex, this);
        		tags.put(tag, tags.get(tag)+intens);
        	}
        }
        if(Utilities.sum(tags.values()) == 0.0){
        	tagsListView.setVisibility(View.GONE);
        	text.setVisibility(View.GONE);
        	statsAvailable = statsAvailable || false;
        } else{
        	tagsListView.setVisibility(View.VISIBLE);
        	text.setVisibility(View.VISIBLE);
            ArrayList<String> tagNames = new ArrayList<String>();
            ArrayList<Double> tagValues = new ArrayList<Double>();
            Iterator<Entry<String, Double>> it = tags.entrySet().iterator();
            while(it.hasNext()){
            	Entry<String, Double> entry = it.next();
            	tagNames.add(entry.getKey());
            	tagValues.add(entry.getValue());
            }
            TagInfoAdapter adapter = new TagInfoAdapter(this, R.layout.item_taginfo_big, tagNames, tagValues);
            tagsListView.setAdapter(adapter);
            // argument 2 = position of TextView in item_taginfo_big
            Android.setListViewHeightBasedOnChildrenChildInPos(tagsListView, 2);
        	statsAvailable = true;
        }
	}
	
	private void setUntrained(){
		String exText = "";
		HashMap<String, Integer> exStats = mngr.getExerciseStatistics();
        Set<String> usedNames = exStats.keySet();
        TextView otherText = (TextView) findViewById(R.id.unusedText);
        ArrayList<String> unused = new ArrayList<String>();
        for(Exercises e: Exercises.values()){
        	if(!usedNames.contains(e.getString(this))){
        		unused.add(e.getString(this));
        	}
        }
        if(!unused.isEmpty() && !usedNames.isEmpty()){
        	exText = getString(R.string.try_one_of_these)+": ";
        	for(int i=0; i<3; i++){
        		if(unused.size() > i){
	        		String name = unused.get(i);
	        		exText += "<b>"+name+"</b>, ";
        		}
        	}
        	if(!unused.isEmpty()){
	        	exText = exText.substring(0, exText.length()-2);
	        	exText += ".";
	        	otherText.setVisibility(View.VISIBLE);
	        	otherText.setText(Html.fromHtml(exText));
	        	statsAvailable = true;
        	} else{
        		otherText.setVisibility(View.GONE);
            	statsAvailable = statsAvailable || false;
        	}
        } else{
        	otherText.setVisibility(View.GONE);
        	statsAvailable = statsAvailable || false;
        }
	}
	
	private void setWeek(){
        int[][] stats = mngr.getStatistics();
        int woy = mngr.getWeekOfYear();
        int yearIndex = mngr.getYearIndex();
        TextView text = (TextView) findViewById(R.id.yourWeekText);
        int workouts = stats[yearIndex][woy];
    	String string = getString(R.string.your_week)+": <b>"+workouts+"</b> "+getString(R.string.workout);
        if(workouts == 1){
        	text.setText(Html.fromHtml(string));
        } else{
        	text.setText(Html.fromHtml(string+"s"));
        }
        if(workouts>0){
        	int wos = workouts;
        	if(workouts > 7){
        		wos = 7;
        	}
	        View progress = new View(this);
	        int color = 0;
	        if(wos < 3){
	        	color = getResources().getColor(R.color.red);
	        } else if(wos < 5){
	        	color = getResources().getColor(R.color.yellow);
	        } else{
	        	color = getResources().getColor(R.color.green);
	        }
	        progress.setBackgroundColor(color);
	        DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        float logicalDensity = metrics.density;
	        @SuppressWarnings("deprecation")
			int width = getWindowManager().getDefaultDisplay().getWidth()-(int) Math.ceil(64 * logicalDensity);
	        width = width*wos/7;
	        LayoutParams params = new LayoutParams(width , LayoutParams.WRAP_CONTENT);
	        params.addRule(RelativeLayout.ALIGN_LEFT, R.id.weekTable);
	        params.addRule(RelativeLayout.ALIGN_TOP, R.id.weekTable);
	        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.weekTable);
	        params.setMargins(2, 5, 1, 5);
	        progress.setLayoutParams(params);
	        RelativeLayout layout = (RelativeLayout) findViewById(R.id.statisticsLayout);
	        layout.addView(progress);
	        if(workouts > 7){
	        	wos = workouts-7;
		        progress = new View(this);
		        progress.setBackgroundResource(R.drawable.progress_bg);
		        double ratio = wos/7;
		        if(ratio < 1.0){
		        	width = (int) (width*ratio);
		        }
		        params = new LayoutParams(width , LayoutParams.WRAP_CONTENT);
		        params.addRule(RelativeLayout.ALIGN_LEFT, R.id.weekTable);
		        params.addRule(RelativeLayout.ALIGN_TOP, R.id.weekTable);
		        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.weekTable);
		        params.setMargins(2, 5, 1, 5);
		        progress.setLayoutParams(params);
		        layout.addView(progress);
	        }
        }
	}
	
	@Override
	public void menu(View v){
		Intent intent = new Intent(this, StatisticsMenuActivity.class);
		startActivity(intent);
	}	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		final ScrollView sv = (ScrollView)findViewById(R.id.scrollStats);
		if(!topScrolled)
		{
			sv.scrollTo(0, 0);
			topScrolled = true;
		}
	}
	
}
