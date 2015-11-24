package training;

import general.BackButtonActivity;

import java.util.ArrayList;
import java.util.Iterator;

import main.SavingManager;
import material.Colors;
import material.Exercise;
import material.Workout;
import de.couchdev.trainingassistant.R;
import de.couchdev.utils.Format;
import de.couchdev.utils.Random;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is the activity for the guided training of a single workout. The user just has to click one button after he
 * finished with one exercise and the activity is showing him what exercise is up next and thus guides the user through
 * his workout.
 * @author Tim Reimer
 *
 */
public class TrainingActivity extends BackButtonActivity{

	private static final String PAUSE = "Pause";
	private static final String COOLDOWN = "Cooldown";
	private static final String WARMUP = "Warmup";
	private static final String END = "End";
	private static final String BACK = "Back";
	private static final int COUNTDOWN = 3; // seconds
	public static final int DONE = 2;
	private Handler handler = new Handler();
	private SavingManager mngr;
	private Workout workout;
	private TextView text1;
	private TextView text2;
	private Iterator<String> action;
	private TextView time;
	private String tmp;
	private TextView title;

	// fields for the sound output
    private final double duration = 0.25; // seconds
    private final int sampleRate = 8000;
    private final double numSamples = duration * sampleRate;
    private final double sample[] = new double[(int) numSamples];
    private final double freqOfTone = 932.33; // A#5/Bb5 in Hz
	private Handler soundHandler = new Handler();
    private final byte generatedSnd[] = new byte[(int) (2 * numSamples)];
	private int volumeBefore;
	private boolean start;
	private AudioTrack audioTrack;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        createBackButton();

        mngr = SavingManager.getInstance();
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeBefore = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int index = (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*mngr.getVolume());
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        
        ArrayList<Workout> trainingSets = mngr.loadWorkouts();
    	int pos = getIntent().getIntExtra(ChooseWorkoutActivity.POSITION, -1);
    	workout = trainingSets.get(pos);
    	
    	ArrayList<String> actionList = new ArrayList<String>();
    	actionList.add(WARMUP);
    	if(workout.isInterval()){
    		for(int i=0; i<workout.getIterations(); i++){
	    		for(Exercise ex: workout.getExercises()){
	    			String exercise = ex.getName()+","+ex.isRepeats()+","+ex.getAmount();
	    			actionList.add(exercise);
	    			actionList.add(PAUSE+","+10);
	    		}
	    		actionList.remove(actionList.size()-1);
	    		actionList.add(PAUSE+","+workout.getPauseTime());
	    	}
    	} else{
    		for(Exercise ex:workout.getExercises()){
    			for(int i=0; i<workout.getIterations(); i++){
	    			String exercise = ex.getName()+","+ex.isRepeats()+","+ex.getAmount();
	    			actionList.add(exercise);
	    			actionList.add(PAUSE+","+10);
    			}
	    		actionList.remove(actionList.size()-1);
	    		actionList.add(PAUSE+","+workout.getPauseTime());
    		}
    	}
		actionList.remove(actionList.size()-1);
		actionList.add(COOLDOWN);
		action = actionList.iterator();

		text1 = (TextView) findViewById(R.id.actionText);
		text2 = (TextView) findViewById(R.id.actionText2);
		title = (TextView) findViewById(R.id.titleText);
		time = (TextView) findViewById(R.id.actionTime);

		// apply theme color
        Button button = (Button) findViewById(R.id.button);
        Drawable image = getResources().getDrawable(R.drawable.training_button_selector);
        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
        button.setBackground(image);
        
		tmp = action.next();
		action(findViewById(R.id.button));
	}
	
	/**
	 * Manages what comes next after the button was clicked.
	 * @param button The clicked view. Here the activities only button.
	 */
	public void action(View button){
		if(tmp == BACK){
			finish();
			return;
		}
		if(tmp != END){
			String[] str = tmp.split(",");
			if(!start && str.length>1 && str[1].equals("false")){
				start = true;
				text1.setText(str[0]);
				text2.setText(getString(R.string.starting_in)+" "+COUNTDOWN+" "+getString(R.string.seconds));
				text1.setVisibility(View.VISIBLE);
				text2.setVisibility(View.VISIBLE);
				countdown(COUNTDOWN, button, start);
				return;
			}
			if(start){
				start = false;
			}
			if(action.hasNext()){
				tmp = action.next();
				String[] split = tmp.split(",");
				if(split.length == 3){
					String times = split[1].equals("true") ? getString(R.string.times) : getString(R.string.secs);
					title.setText(Html.fromHtml(getString(R.string.up_next)+": <b>"+split[0]+", "+split[2]+" "+times+"</b>"));
				} else if(split.length == 2){
					title.setText(Html.fromHtml(getString(R.string.up_next)+": <b>"+split[0]+", "+split[1]
							+ " "+getString(R.string.secs)+"</b>"));
				} else if(split.length == 1){
					title.setText(Html.fromHtml(getString(R.string.up_next)+": <b>"+split[0]+"</b>"));
				}
			} else{
				tmp = END;
				title.setText(getString(R.string.training));
			}
			if(str[0].equals(WARMUP)){
				text1.setText(getString(R.string.warmup));
				text2.setText("");
				text2.setVisibility(View.GONE);
				((Button) button).setText(getString(R.string.done));
			} else if(str[0].equals(COOLDOWN)){
				text1.setText(getString(R.string.cooldown));
				text2.setText("");
				text2.setVisibility(View.GONE);
				((Button) button).setText(getString(R.string.done));
			} else if(str[0].equals(PAUSE)){
				text1.setText(getString(R.string.pause));
				text2.setText(str[1]+" "+getString(R.string.seconds));
				text2.setVisibility(View.VISIBLE);
				((Button) button).setText(getString(R.string.next));
				int secs = Integer.parseInt(str[1]);
				countdown(secs, button, false);
			} else{
				text1.setText(str[0]);
				if(str[1].equals("true")){
					text2.setText(str[2]+" "+getString(R.string.repeats));
					text2.setVisibility(View.VISIBLE);
					((Button) button).setText(getString(R.string.done));
				} else{
					text2.setText(str[2]+" "+getString(R.string.seconds));
					text2.setVisibility(View.VISIBLE);
					((Button) button).setText(getString(R.string.next));
					int secs = Integer.parseInt(str[2]);
					countdown(secs, button, false);
				}
			}
		} else{
			String praise = getPraiseString();
			text1.setText(praise);
			text2.setText("");
			text2.setVisibility(View.GONE);
			String[] text = praise.replace("?","").split("!");
			((Button) button).setText(text[text.length-1].trim()+"!");
			tmp = BACK;
			mngr.increaseStats(workout);
			String name = workout.getName();
			if(mngr.loadSettings()[SavingManager.WORKOUT_SETTING_ID1]){
				mngr.addWorkout(name);
			}
			mngr.addIntensitiy(workout.getFinalIntensity(this));
			setResult(DONE);
		}
	}
	
	/**
	 * Selects randomly a string from a set of strings that praises the user for finishing a workout.
	 * @return A random string.
	 */
	private String getPraiseString(){
		String[] praise = getResources().getStringArray(R.array.praiseArray);
		String again = "";
		Random rand = new Random(0, 100);
		if(rand.next()%3==0){
			again += "\n"+getString(R.string.again_toworrow);
		}
		rand = new Random(0, praise.length-1);
		return praise[rand.next()]+again;
	}
	
	/**
	 * Displays a countdown that counts from <b>seconds</b> to zero and then invokes the next {@link #action(View)}.
	 * @param seconds The amount of seconds to count.
	 * @param v The action button.
	 * @param start {@code true} if immediately after this countdown another precountdown will be started.
	 * {@code false} for all other cases.
	 */
	private void countdown(final int seconds, final View v, final boolean start){
		v.setEnabled(false);
		v.setBackgroundResource(R.drawable.grey_button);
		time.setVisibility(View.VISIBLE);
		Runnable runnable = new Runnable() 
		{
			private int millis = seconds*1000;
			private int step = 100;
			@Override
			public void run() {
				time.setText(Format.int2str(millis/1000,2)+"."+((millis%1000)/step));
				millis -= step;
				handler.postDelayed(this, step);
				if(millis==-step){
					handler.removeCallbacks(this);
					v.setEnabled(true);
			        Drawable image = getResources().getDrawable(R.drawable.white_button_selector);
			        image.setColorFilter(Colors.getThemeColor(), Mode.MULTIPLY);
			        v.setBackground(image);
					time.setVisibility(View.GONE);
					if(start){
						playSound(1);
						action(v);
					}else{
						playSound(2);
						action(v);
					}
			        handler.removeCallbacksAndMessages(this);
				}
			}
		};
		runnable.run();
	}

	/**
	 * Helping method for playing a sound. This method creates the sound.
	 */
    private void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    /**
     * Plays the created sound.
     */
    @SuppressWarnings("deprecation")
	private void playSample(){
    	if(audioTrack!=null){
    		audioTrack.stop();
	    	audioTrack.flush();
	    	audioTrack.release();
    	}
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, (int) numSamples,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        try{
        	audioTrack.play();
        } catch(IllegalStateException e){
        	e.printStackTrace();
        }
    }
    
    /**
     * Plays a sound <b>repeats</b> times with a short break in between.
     * @param repeats How often the sound should be played repeatedly.
     */
    private void playSound(final int repeats){
    	if(repeats < 1){
    		return;
    	}
    	final Thread thread = new Thread(new Runnable() {
            public void run() {
                genTone();
                soundHandler.post(new Runnable() {
                    public void run() {
                        playSample();
                        for(int i=1; i<repeats; i++){
	                        try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
	                        playSample();
                        }
                        soundHandler.removeCallbacksAndMessages(this);
                    }
                    
                });
                soundHandler.removeCallbacksAndMessages(this);
            }
        });
        thread.start();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, volumeBefore, 0);
        handler.removeCallbacksAndMessages(null);
        soundHandler.removeCallbacksAndMessages(null);
    }
}
