package vn.hexagon.vietnhat.ui.list.phone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.hexagon.vietnhat.R;
import vn.hexagon.vietnhat.data.model.fone.ProductModel;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    private List<ProductModel> productModels;

    public BrandAdapter(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BrandViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_phone_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        holder.tvContent.setText(productModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvContent.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(productModels.get(getAdapterPosition()));
                }
            });
        }
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductModel selectedItem);
    }
}
