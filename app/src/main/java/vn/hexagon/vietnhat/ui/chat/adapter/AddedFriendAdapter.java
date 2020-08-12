package vn.hexagon.vietnhat.ui.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vn.hexagon.vietnhat.R;
import vn.hexagon.vietnhat.data.model.chat.FriendItem;

public class AddedFriendAdapter extends RecyclerView.Adapter<AddedFriendAdapter.AddedFriendViewHolder> {

    public ArrayList<FriendItem> addedFriendList;

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AddedFriendAdapter(ArrayList<FriendItem> addedFriendList) {
        if (addedFriendList == null) {
            this.addedFriendList = new ArrayList<>();
        } else {
            this.addedFriendList = addedFriendList;
        }
    }

    public void addFriend(FriendItem friendItem) {
        if (addedFriendList == null) addedFriendList = new ArrayList<>();
        addedFriendList.add(friendItem);
        notifyDataSetChanged();
    }

    public void removeFriend(int position) {
        if (addedFriendList != null && addedFriendList.size() > position && position > -1) {
            addedFriendList.remove(position);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public AddedFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddedFriendViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_added_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddedFriendViewHolder holder, int position) {
        FriendItem item = addedFriendList.get(position);
        if (item != null) {
            Glide.with(holder.imageUserAvatar)
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .load(item.getAvatar())
                    .placeholder(R.drawable.ic_ava_nodata)
                    .error(R.drawable.ic_ava_nodata)
                    .into(holder.imageUserAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return addedFriendList.size();
    }

    public FriendItem getItem(int position) {
        if (addedFriendList != null && addedFriendList.size() > position) {
            return addedFriendList.get(position);
        }
        return null;
    }

    class AddedFriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUserAvatar;
        private ImageView imageCancel;


        public AddedFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUserAvatar = (ImageView) itemView.findViewById(R.id.imageUserAvatar);
            imageCancel = (ImageView) itemView.findViewById(R.id.imageCancel);
            imageCancel.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
