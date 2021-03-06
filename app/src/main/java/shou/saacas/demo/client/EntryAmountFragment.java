package shou.saacas.demo.client;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EntryAmountFragment extends Fragment implements PaymentTask.Callback {
    private TextView amountText;
    private Button settleButton;

    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_amount, container, false);

        dialog = new ProgressDialog(getActivity());

        amountText = (TextView)view.findViewById(R.id.amount_text);
        settleButton = (Button) view.findViewById(R.id.settle_button);
        settleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage(getString(R.string.payment_in_progress));
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(100);
                dialog.setProgress(0);
                dialog.show();

                int amount = Integer.parseInt(amountText.getText().toString());

                PaymentTask task = new PaymentTask(EntryAmountFragment.this, amount);
                task.execute();
            }
        });

        return view;
    }

    @Override
    public void onSuccess(String result) {
        dialog.dismiss();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new CompletePaymentFragment())
                .commit();
    }

    @Override
    public void onProgress(int progress) {
        dialog.incrementProgressBy(progress);
    }

    @Override
    public void onFailure(Exception error) {
        dialog.dismiss();
    }
}
