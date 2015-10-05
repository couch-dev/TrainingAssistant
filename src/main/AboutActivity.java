package main;

import general.BackButtonFragmentActivity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.couchdev.trainingassistant.R;

/**
 * 
 * @author Tim Reimer
 *
 */
public class AboutActivity extends BackButtonFragmentActivity {

	private static ArrayList<String> titles;
	private static ArrayList<String> texts;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        createBackButton();

        titles = new ArrayList<String>();
        titles.add(getString(R.string.info_general)); titles.add(getString(R.string.info_exercises));
        titles.add(getString(R.string.info_training)); titles.add(getString(R.string.info_contact));
        texts = new ArrayList<String>();
        texts.add(getString(R.string.info_general_text)); texts.add(getString(R.string.info_exercises_text));
        texts.add(getString(R.string.info_training_text)); texts.add(getString(R.string.info_contact_text));
        ViewPager pager = (ViewPager) findViewById(R.id.aboutPager);
        pager.setAdapter(new AboutPagerAdapter(getSupportFragmentManager()));
    }
    
    public class AboutPagerAdapter extends FragmentStatePagerAdapter 
	{

		public AboutPagerAdapter(FragmentManager fragmentManager) 
	    {
	        super(fragmentManager);
	    }
	    
	    @Override
	    public Fragment getItem(int i) {
	        Fragment fragment = new AboutObjectFragment();
	        Bundle args = new Bundle();
	        args.putInt(AboutObjectFragment.ARG_OBJECT, i);
	        fragment.setArguments(args);
	        return fragment;
	    }

	    @Override
	    public int getCount()
	    {
	    	return titles.size();
	    }
	}
	
	public static class AboutObjectFragment extends Fragment {
	    public static final String ARG_OBJECT = "position";

	    public AboutObjectFragment() {
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.about_object, container, false);
	        Bundle args = getArguments();
	        
			int pos = args.getInt(ARG_OBJECT);
			String title = titles.get(pos);
			String text = texts.get(pos);
			TextView titleView = (TextView) rootView.findViewById(R.id.infoTitle);
			titleView.setText(title);
			TextView textView = (TextView) rootView.findViewById(R.id.infoText);
			textView.setText(Html.fromHtml(text));
			textView.setMovementMethod(LinkMovementMethod.getInstance());
	        return rootView;
	    }
		
	}
}
