package com.kaiman.sports.main.workshops.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaiman.sports.R;
import com.kaiman.sports.main.workshops.model.DisciplineViewModel;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class DisciplineSpinnerAdapter extends ArrayAdapter<DisciplineViewModel> {

    public DisciplineSpinnerAdapter(Context context, List<DisciplineViewModel> list){
        super(context, R.layout.item_discipline_spinner, R.id.textName, list);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        DisciplineViewModel item = getItem(position);
        assert item != null;

        TextView textName = view.findViewById(R.id.textName);
        //ImageView imagePhoto = view.findViewById(R.id.imagePhoto);

        textName.setText(item.name);
        /*Picasso.get()
                .load(item.photoUrl)
                .transform(new CircleTransform())
                .into(imagePhoto);*/
        return view;
    }
}