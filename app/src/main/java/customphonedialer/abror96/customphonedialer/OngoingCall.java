package customphonedialer.abror96.customphonedialer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.jetbrains.annotations.Nullable;

import io.reactivex.subjects.BehaviorSubject;

import static android.content.Context.WINDOW_SERVICE;

public class OngoingCall {

    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private static Call call;

    private Context context;

    public OngoingCall(Context context) {
        this.context = context;
    }

    private Object callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            state.onNext(newState);
        }
    };

    public final void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback((Call.Callback) callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback) callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    public void answer() {
        assert call != null;
        call.answer(VideoProfile.STATE_AUDIO_ONLY);
    }

    public void hangup() {
        assert call != null;
        call.disconnect();

    }



}
