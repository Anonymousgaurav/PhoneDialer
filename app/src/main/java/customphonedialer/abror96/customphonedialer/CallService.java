package customphonedialer.abror96.customphonedialer;

import android.telecom.Call;
import android.telecom.InCallService;

public class CallService extends InCallService {

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        new OngoingCall(getApplicationContext()).setCall(call);
        CallActivity.start(this, call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        new OngoingCall(getApplicationContext()).setCall(null);
    }
}
