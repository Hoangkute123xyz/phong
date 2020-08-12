package vn.hexagon.vietnhat.ui.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.hexagon.vietnhat.R;
import vn.hexagon.vietnhat.data.model.chat.FriendItem;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    public List<FriendItem> friendItems;
    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FriendAdapter(List<FriendItem> friendItems) {
        this.friendItems = friendItems;
    }

    public void onAddFriend(int position) {
        if (!friendItems.isEmpty() && position < friendItems.size() && position > -1) {
            friendItems.remove(position);
            notifyDataSetChanged();
        }
    }

    public void onRemoveFriend(FriendItem friendItem) {
        if (friendItem != null) {
            friendItems.add(friendItem);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_recent_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendItem item = friendItems.get(position);
        if (item != null) {
            holder.tvLastSeen.setText(item.getPhone());
            holder.tvUserName.setText(item.getName());
            Glide.with(holder.userAvatar)
                    .load(item.getAvatar())
                    .placeholder(R.drawable.ic_ava_nodata)
                    .error(R.drawable.ic_ava_nodata)
                    .into(holder.userAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return friendItems.size();
    }

    public void replaceData(ArrayList<FriendItem> filterList) {
        if (filterList == null || filterList.isEmpty()) {
            friendItems = new ArrayList<>();
        } else {
            friendItems = filterList;
        }
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userAvatar;
        private TextView tvUserName;
        private TextView tvLastSeen;
        private ImageView imageChecked;


        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.userAvatar);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvLastSeen = (TextView) itemView.findViewById(R.id.tvLastSeen);
            imageChecked = (ImageView) itemView.findViewById(R.id.imageChecked);
            imageChecked.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
