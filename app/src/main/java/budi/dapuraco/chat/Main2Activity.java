package budi.dapuraco.chat;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import budi.dapuraco.R;

public class Main2Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mUserId;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        clearNotif();

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("APP CHAT");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth= FirebaseAuth.getInstance();

        //TABSpager
        mViewPager=(ViewPager)findViewById(R.id.mainPager);
        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();

        if (currentUser == null){
            sendToLogin();
        }else {
            mUserId= currentUser.getUid();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.main_log_out) {
            FirebaseAuth.getInstance().signOut();
            sendToLogin();
            return true;
        }else if (id == R.id.main_account_setting) {
            Intent i=new Intent(Main2Activity.this, SettingActivity.class);
            startActivity(i);
            return true;
        }else if (id == R.id.main_all_user) {
            Intent i=new Intent(Main2Activity.this, AllUserActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendToLogin(){
        Intent i=new Intent(Main2Activity.this, LoginChat.class);
        startActivity(i);
    }
    private void clearNotif(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("NotificationData", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }
}
