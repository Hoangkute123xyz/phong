package vn.hexagon.vietnhat.ui.list.phone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vn.hexagon.vietnhat.R;
import vn.hexagon.vietnhat.data.model.fone.Brand;
import vn.hexagon.vietnhat.data.model.fone.ProductModel;
import vn.hexagon.vietnhat.repository.list.PostListRepository;

public class PhoneFilterAdapter extends RecyclerView.Adapter<PhoneFilterAdapter.PhoneFilterViewHolder> {

    private List<Brand> brandList;
    private Context context;
    private PostListRepository repository;

    private PopupMenu popupWindow;

    public PhoneFilterAdapter(List<Brand> brandList, Context context, PostListRepository repository) {
        this.brandList = brandList;
        this.context = context;
        this.repository = repository;

    }



    @NonNull
    @Override
    public PhoneFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhoneFilterViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fone_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneFilterViewHolder holder, int position) {
        Brand item = brandList.get(position);
        if (item != null) {
            holder.tvBrand.setText(item.getName());
            Glide.with(holder.imagePhone).load(item.getImg())
//                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_phone_cell)
                    .error(R.drawable.ic_phone_cell)
                    .into(holder.imagePhone);
        }
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    class PhoneFilterViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBrand;
        private ImageView imagePhone;


        @SuppressLint("CheckResult")
        private PhoneFilterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            imagePhone = itemView.findViewById(R.id.imageBrand);

            itemView.setOnClickListener(v -> {
                if (brandList != null && brandList.size() > getAdapterPosition()) {
                    if (brandList.get(getAdapterPosition()).getListProduct() == null) {
                        repository.getModel(brandList.get(getAdapterPosition()).getId())
                                .compose(upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                                .subscribe(
                                        response -> {
                                            brandList.get(getAdapterPosition()).setListProduct(response.getData());
                                            showPopUp(brandList.get(getAdapterPosition()).getListProduct());
                                        }, throwable -> {

                                        }
                                );
                    } else {
                        showPopUp(brandList.get(getAdapterPosition()).getListProduct());
                    }
                }
            });
        }

        void showPopUp(List<ProductModel> productModels) {
            if (popupWindow != null) popupWindow.dismiss();
            popupWindow = new PopupMenu(context, itemView);

            if (productModels != null) {
                for (ProductModel item : productModels) {
                    popupWindow.getMenu().add(item.getName());
                }
                popupWindow.setOnMenuItemClickListener(item -> {
                    for (int i = 0; i < productModels.size(); i++) {
                        if (productModels.get(i).getName() == item.getTitle() && onPhoneFilterClickListener != null) {
                            onPhoneFilterClickListener.onFilterClick(productModels.get(i).getBrandId(), productModels.get(i).getId());
                        }
                    }
                    return true;
                });
                popupWindow.show();
            }

        }
    }

    private OnPhoneFilterClickListener onPhoneFilterClickListener;

    public void setOnPhoneFilterClickListener(OnPhoneFilterClickListener onPhoneFilterClickListener) {
        this.onPhoneFilterClickListener = onPhoneFilterClickListener;
    }

    public interface OnPhoneFilterClickListener {
        void onFilterClick(String brandID, String modelID);
    }
}
