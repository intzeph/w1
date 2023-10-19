package com.example.wildtech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class sliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public sliderAdapter(Context context) {

        this.context = context;

    }

    int images[]={
            R.drawable.onboard1,
            R.drawable.onboard2,
            R.drawable.onboard3
    };

    int headings []={
            R.string.slide_title_1,
            R.string.slide_title_2,
            R.string.slide_title_3
    };

    int description[]={
            R.string.slide_descript_1,
            R.string.slide_descript_2,
            R.string.slide_descript_3,

    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout,container, false);

        ImageView imageView=view.findViewById(R.id.sliderImage);
        TextView heading=view.findViewById(R.id.title);
        TextView desc=view.findViewById(R.id.description);


        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(description[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
