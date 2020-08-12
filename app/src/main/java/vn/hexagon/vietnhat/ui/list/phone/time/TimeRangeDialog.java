package vn.hexagon.vietnhat.ui.list.phone.time;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.hexagon.vietnhat.R;
import vn.hexagon.vietnhat.data.model.fone.TimeRange;

public class TimeRangeDialog extends DialogFragment {

    public static final String TAG = TimeRangeDialog.class.getCanonicalName();

    private RecyclerView rvTimeRange;
    private Button buttonAccept;

    private GridLayoutManager gridLayoutManager;
    private TimeRangeAdapter timeRangeAdapter;
    private ArrayList<TimeRange> timeRanges;

    public static TimeRangeDialog newInstance(ArrayList<TimeRange> timeRanges) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("ARG_TIME_RANGE", timeRanges);
        TimeRangeDialog fragment = new TimeRangeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time_range_chooser, container, false);


        rvTimeRange = (RecyclerView) view.findViewById(R.id.rvTimeRange);
        buttonAccept = (Button) view.findViewById(R.id.buttonAccept);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        rvTimeRange.setHasFixedSize(true);
        rvTimeRange.setLayoutManager(gridLayoutManager);

        if (getArguments() != null) {
            timeRanges = getArguments().getParcelableArrayList("ARG_TIME_RANGE");
            if (timeRanges != null) {
                timeRangeAdapter = new TimeRangeAdapter(timeRanges);
                rvTimeRange.setAdapter(timeRangeAdapter);
            }
        }
        buttonAccept.setOnClickListener(v -> {

            if (timeRangeAdapter != null && onTimeRangeClickListener != null) {
                TimeRange item = timeRangeAdapter.getSelectedItem();
                if (item != null) {
                    onTimeRangeClickListener.onTimeSelected(timeRangeAdapter.getSelectedItem());
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Vui lòng chọn 1 khung giờ!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Có lỗi xảy ra! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }

        return dialog;
    }

    private OnTimeRangeClickListener onTimeRangeClickListener;

    public void setOnTimeRangeClickListener(OnTimeRangeClickListener onTimeRangeClickListener) {
        this.onTimeRangeClickListener = onTimeRangeClickListener;
    }

    public interface OnTimeRangeClickListener {
        void onTimeSelected(TimeRange timeRange);
    }


}
