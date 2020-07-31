package msm.MemeSena.Base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import msm.MemeSena.Api.ApiClient;
import msm.MemeSena.Api.ApiService;

public abstract class BaseActivity extends AppCompatActivity {
    public static int adDisplayCount = 0;
    public static int adDisplayIntervalCount = 0; // Value of interval for display ads (0 - means everytime)
    private ProgressDialog mProgressDialog;

    public static int getAdDisplayIntervalCount() {
        return (adDisplayIntervalCount < 0) ? 1 : adDisplayIntervalCount + 1;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static final class MessageDialogFragment extends DialogFragment {
        private static final String ARG_TITLE_ID = "title_id";
        private static final String ARG_MESSAGE = "message";

        public static DialogFragment newInstance(@StringRes int titleId, String message) {
            DialogFragment fragment = new MessageDialogFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_TITLE_ID, titleId);
            arguments.putString(ARG_MESSAGE, message);
            fragment.setArguments(arguments);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            @StringRes final int title = getArguments().getInt(ARG_TITLE_ID);
            String message = getArguments().getString(ARG_MESSAGE);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MessageDialogFragment.this.dismiss();
                        }
                    });
            if (title != 0) {
                dialogBuilder.setTitle(title);
            }
            return dialogBuilder.create();
        }
    }

    public ApiService getApiClientObj() {
        return ApiClient.getClient()
                .create(ApiService.class);
    }

    protected void showDialog(String message) {
        if (!isShowingProgressDialog()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void updateProgressDialogMessage(String message) {
        mProgressDialog.setMessage(message);
    }

    protected void hideDialog() {
        if (isShowingProgressDialog()) {
            mProgressDialog.dismiss();
        }
    }

    protected boolean isShowingProgressDialog() {
        if (mProgressDialog == null) {
            return false;
        }
        return mProgressDialog.isShowing();
    }
}