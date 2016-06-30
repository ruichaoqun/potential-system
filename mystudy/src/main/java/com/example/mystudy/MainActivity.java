package com.example.mystudy;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import fragment.GirlsFragment;
import utils.FragmentType;
import widget.ReduceBehavior;

public class MainActivity extends BaseActivity {
    private FloatingActionButton actionButton;
    private boolean isFabClick = false;
    private RelativeLayout mengban;
    private ImageView say;
    private ImageView answer;

    @Override
    int getLayoutId() {return R.layout.activity_main;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionButton.setImageResource(R.drawable.ic_toys_explore_white_24dp);
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this,R.drawable.ic_message_black_36dp).mutate());
        DrawableCompat.setTintList(drawable, ContextCompat.getColorStateList(this, R.color.tint_list_blue));
        Drawable drawable1 = DrawableCompat.wrap(ContextCompat.getDrawable(this,R.drawable.ic_camera_black_36dp).mutate());
        DrawableCompat.setTintList(drawable1, ContextCompat.getColorStateList(this, R.color.tint_list_blue));
        say.setImageDrawable(drawable);
        answer.setImageDrawable(drawable1);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if(viewPager != null){
            setUpViewPager(viewPager);
        }
        viewPager.setOffscreenPageLimit(3);

        TabLayout layout = (TabLayout) findViewById(R.id.tabs);
        layout.setupWithViewPager(viewPager);

    }



    private void setUpViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new GirlsFragment(FragmentType.Android),"Android");
        adapter.addFragment(new GirlsFragment(FragmentType.Xiatuijian),"推荐");
        adapter.addFragment(new GirlsFragment(FragmentType.tuozhanzhiyuan), "拓展");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new MaterialDialog.Builder(MainActivity.this)
                .title("关于")
                .content("这是一个Material Design的展示")
                .positiveText("确定")
                .show();
        return true;
    }

    @Override
    void initResAndListener() {
        actionButton = (FloatingActionButton) findViewById(R.id.fab_operate);
        mengban = (RelativeLayout) findViewById(R.id.mengban);
        say = (ImageView) findViewById(R.id.say_image);
        answer = (ImageView) findViewById(R.id.apply_image);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFabClick){
                    startAlphaAnimation(mengban, 1);
                    startRotationAnimation(v,90);
                }else{
                    startAlphaAnimation(mengban,0);
                   startRotationAnimation(v,0);
                }
            }
        });

        say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"button被点击了",Toast.LENGTH_SHORT).show();
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"button被点击了",Toast.LENGTH_SHORT).show();
            }
        });

        mengban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlphaAnimation(mengban,0);
                startRotationAnimation(actionButton, 0);
            }
        });
    }



    private void startRotationAnimation(View v,int rotate){
        ViewCompat.animate(v).rotation(rotate).setInterpolator(ReduceBehavior.INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
            }

            @Override
            public void onAnimationEnd(View view) {
                isFabClick = isFabClick?false:true;
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }

    public void startAlphaAnimation(View v,int alpha){
        ViewCompat.animate(v).alpha(alpha).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                if (view.getAlpha() == 0) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (view.getAlpha() == 0) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }

    class Adapter extends FragmentStatePagerAdapter{

         private final List<Fragment> fragments = new ArrayList<>();
         private final List<String> titles = new ArrayList<>();

         public Adapter(FragmentManager fm) {
             super(fm);
         }

         public void addFragment(Fragment fragment,String title){
             fragments.add(fragment);
             titles.add(title);
         }

         @Override
         public Fragment getItem(int position) {
             return fragments.get(position);
         }

         @Override
         public int getCount() {
             return fragments.size();
         }

         @Override
         public CharSequence getPageTitle(int position) {
             return titles.get(position);
         }
     }

}
