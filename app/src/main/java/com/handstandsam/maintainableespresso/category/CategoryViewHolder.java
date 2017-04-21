package com.handstandsam.maintainableespresso.category;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handstandsam.maintainableespresso.R;
import com.handstandsam.maintainableespresso.models.Item;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


class CategoryViewHolder extends RecyclerView.ViewHolder {
    private static final String IMAGE_BASE_URL = "https://s3.amazonaws.com/maintainable-espresso/images/";

    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.image)
    AppCompatImageView imageView;

    private Item item;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), item.getLabel(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final int colors[] = {R.color.material_amber, R.color.material_blue,
            R.color.material_blue_grey, R.color.material_pink,
            R.color.material_cyan, R.color.material_deep_orange, R.color.material_deep_purple,
            R.color.material_green, R.color.material_blue, R.color.material_light_blue,
            R.color.material_brown, R.color.material_amber};

    public void bindData(Item item, int position) {
        int colorIdx = position % colors.length;
        Timber.d("idx: " + colorIdx);
        int colorResource = colors[colorIdx];
        itemView.setBackgroundResource(colorResource);
        this.item = item;

        Glide.with(imageView.getContext()).load(IMAGE_BASE_URL + item.getImage()).into(imageView);
        textView.setText(this.item.getLabel());
    }
}
