package com.project.dayshedule.dayshedule.Dishes;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.project.dayshedule.dayshedule.Enum.RequestType;
import com.project.dayshedule.dayshedule.Enum.ResultType;
import com.project.dayshedule.dayshedule.Interface.OnTaskCompleted;
import com.project.dayshedule.dayshedule.Models.DishesModel;
import com.project.dayshedule.dayshedule.R;
import com.project.dayshedule.dayshedule.RequestData;
import com.project.dayshedule.dayshedule.RequestDataParameters;

import org.json.JSONException;
import org.json.JSONObject;

public class DishesDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DishesModel dishObject;
    Bundle dishBundle;
    int dishGID;
    int tabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_details);
        setStatusBar();

        dishBundle = getIntent().getExtras();
        dishGID = dishBundle.getInt("id");
        tabIndex = dishBundle.getInt("tab");

        getDishDetailsTest();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatusBar(){
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dishes_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    DishesDetailsTab1 t1 = new DishesDetailsTab1();
                    t1.setDish(dishObject);
                    return t1;
                case 1:
                    DishesDetailsTab2 t2 = new DishesDetailsTab2();
                    t2.setDish(dishObject);
                    return t2;
                case 2:
                    DishesDetailsTab3 t3 = new DishesDetailsTab3();
                    t3.setDish(dishObject);
                    return t3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private void getDishDetailsTest(){
        new RequestData(createGetDishParameters(), ResultType.OBJECT, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object obj) throws JSONException {
                JSONObject jObject = (JSONObject) obj;
                dishObject = new DishesModel(jObject.getInt("GID"), jObject.get("Name").toString(), jObject.get("Description").toString());
                setHeaderText(dishObject.getName());

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
                TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
                tab.select();
            }
        }).execute();
    }

    private RequestDataParameters createGetDishParameters(){
        RequestDataParameters param = new RequestDataParameters();
        param.setmContext(DishesDetailsActivity.this);
        param.setMessage("Trwa pobieranie danych...");
        param.setUrl("DishesController.php?dishGID=" + dishGID);
        param.setReqType(RequestType.GET);
        return param;
    }

    private void setHeaderText(String content){
        TextView headerText = (TextView) findViewById(R.id.dishDetails_headerName);
        headerText.setText(content);
    }
}
