package customphonedialer.abror96.customphonedialer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customphonedialer.abror96.customphonedialer.R;
import customphonedialer.abror96.customphonedialer.Tabs.FalseNumber;
import customphonedialer.abror96.customphonedialer.Tabs.Interested;
import customphonedialer.abror96.customphonedialer.Tabs.NotInterested;
import kotlin.collections.ArraysKt;

import static android.Manifest.permission.CALL_PHONE;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

public class DialerActivity extends AppCompatActivity {

    @BindView(R.id.phoneNumberInput)
    EditText phoneNumberInput;
    @BindView(R.id.btn_Call)
    Button btn_Call;

    TabLayout tabLayout;
    ViewPager viewPager;


    public static int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        ButterKnife.bind(this);

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        if (getIntent() != null && getIntent().getData() != null)
            phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Interested(), "Interested");
        adapter.addFrag(new NotInterested(), "Not Interested");
        adapter.addFrag(new FalseNumber(), "False Numbers");
        viewPager.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //  offerReplacingDefaultDialer();

        btn_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        phoneNumberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                makeCall();
                return true;
            }
        });
    }


    private void makeCall() {
        if (android.support.v4.content.PermissionChecker.checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED) {
            Uri uri = Uri.parse("tel:" + phoneNumberInput.getText().toString().trim());

            startActivity(new Intent(Intent.ACTION_CALL, uri));
        }
    }

    private void offerReplacingDefaultDialer() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

        if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
            Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && ArraysKt.contains(grantResults, PERMISSION_GRANTED)) {
            makeCall();
        }
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
