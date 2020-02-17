package customphonedialer.abror96.customphonedialer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class Feedback extends AppCompatActivity
{

    CheckBox  checkBox1,checkBox2,checkBox3;
    Button btn_Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initViews();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkBox1.isChecked())
                {



                }
                if(checkBox2.isChecked())
                {

                }
                if(checkBox3.isChecked())
                {

                }

            }
        });



    }

    private void initViews()
    {
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        btn_Submit = (Button)  findViewById(R.id.btn_Submit);
    }


}
