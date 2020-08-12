package vn.hexagon.vietnhat.ui.list.phone.time;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.hexagon.vietnhat.R;
import vn.hexagon.vietnhat.data.model.fone.TimeRange;

public class TimeRangeAdapter extends RecyclerView.Adapter<TimeRangeAdapter.TimeRangeViewHolder> {

    private List<TimeRange> timeRanges;
    private int lastSelectedPosition = -1;
    private boolean[] tracking;


    public TimeRangeAdapter(List<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
        tracking = new boolean[timeRanges.size()];
    }

    public TimeRange getSelectedItem() {
        for (int i = 0; i < tracking.length; i++) {
            if (tracking[i]) {
                return timeRanges.get(i);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public TimeRangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeRangeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_range, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeRangeViewHolder holder, int position) {
        TimeRange item = timeRanges.get(position);
        if (item != null) {
            holder.tvTimeRange.setText(String.format("%s-%s", item.getTimeStart(), item.getTimeEnd()));
            holder.tvTimeRange.setSelected(tracking[position]);
        }
    }

    @Override
    public int getItemCount() {
        return timeRanges.size();
    }

    class TimeRangeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTimeRange;

        public TimeRangeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeRange = itemView.findViewById(R.id.tvTimeRange);
            tvTimeRange.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            tracking[getAdapterPosition()] = !tracking[getAdapterPosition()];
            if (lastSelectedPosition != -1) {
                tracking[lastSelectedPosition] = !tracking[lastSelectedPosition];
                notifyItemChanged(lastSelectedPosition);
            }
            lastSelectedPosition = getAdapterPosition();
            notifyItemChanged(getAdapterPosition());
        }
    }
}
