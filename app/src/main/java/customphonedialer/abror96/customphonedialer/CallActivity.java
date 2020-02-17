package customphonedialer.abror96.customphonedialer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customphonedialer.abror96.customphonedialer.Adapter.Interested_Adapter;
import customphonedialer.abror96.customphonedialer.Model.Interested_Model;
import customphonedialer.abror96.customphonedialer.helperClass.DatabaseHelper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kotlin.collections.CollectionsKt;

import static customphonedialer.abror96.customphonedialer.Constants.asString;

public class CallActivity extends AppCompatActivity {

    @BindView(R.id.answer)
    Button answer;
    @BindView(R.id.hangup)
    Button hangup;
    @BindView(R.id.callInfo)
    TextView callInfo;


    DatabaseHelper databaseHelper;
    TextView tvTimer;
    private CompositeDisposable disposables;
    private String number;
    private OngoingCall ongoingCall;
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);

        databaseHelper = new DatabaseHelper(CallActivity.this);
        tvTimer = findViewById(R.id.tvTimer);


        ongoingCall = new OngoingCall(getApplicationContext());
        disposables = new CompositeDisposable();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        number = Objects.requireNonNull(getIntent().getData()).getSchemeSpecificPart();
    }

    @OnClick(R.id.answer)
    public void onAnswerClicked() {
        ongoingCall.answer();
    }

    @OnClick(R.id.hangup)
    public void onHangupClicked()
    {
        ongoingCall.hangup();
         feedback();
    }

    private void feedback()
    {
        Dialog feedback_dialog = new Dialog(CallActivity.this);
        feedback_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedback_dialog.setContentView(R.layout.activity_feedback);
        feedback_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        feedback_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        feedback_dialog.show();


        Button btn_Submit = feedback_dialog.findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                disposables.add(
                        OngoingCall.state
                                .filter(new Predicate<Integer>() {
                                    @Override
                                    public boolean test(Integer integer) throws Exception {
                                        return integer == Call.STATE_DISCONNECTED;
                                    }
                                })
                                .delay(1, TimeUnit.SECONDS)
                                .firstElement()
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                             pushtodb();
                                              finish();
                                       // onBackPressed();
                                    }
                                }));

            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        assert updateUi(-1) != null;
        disposables.add(
                OngoingCall.state
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                updateUi(integer);
                            }
                        }));

    }

    @SuppressLint("SetTextI18n")
    private Consumer<? super Integer> updateUi(Integer state) {

        callInfo.setText(asString(state) + "\n" + number);

        if (state != Call.STATE_RINGING) {
            answer.setVisibility(View.GONE);

        }
        else {
            answer.setVisibility(View.VISIBLE);

        }
        if (state == Call.STATE_ACTIVE)
        {
            start();
        }

        if (state == Call.STATE_DISCONNECTED)
        {
            stop();
        }


        if (CollectionsKt.listOf(new Integer[]{
                Call.STATE_DIALING,
                Call.STATE_RINGING,
                Call.STATE_ACTIVE}).contains(state))
        {
            hangup.setVisibility(View.VISIBLE);
        } else
            hangup.setVisibility(View.GONE);

        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
    }

    public static void start(Context context, Call call) {
        Intent intent = new Intent(context, CallActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }

    void pushtodb()
    {
        Interested_Model interested_model = new Interested_Model();
        interested_model.setImobilenum(number);
        databaseHelper.addUser(interested_model);

    }

    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    public void start() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void stop() {
        customHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            tvTimer.setText(getDateFromMillis(timeInMilliseconds));
            customHandler.postDelayed(this, 1000);
        }
    };
}
