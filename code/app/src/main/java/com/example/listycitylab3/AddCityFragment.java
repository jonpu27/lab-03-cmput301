package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener{
        void addCity(City city);
        void editCity(City city, int position);
    }

    private AddCityDialogListener listener;

    public static AddCityFragment newInstance(City city, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Bundle args = getArguments();

        if (args != null && args.containsKey("city")) {
            // ===== Edit Mode =====
            City city = (City) args.getSerializable("city");
            int position = args.getInt("position");

            // Pre-fill with existing values
            editCityName.setText(city.getName());
            editProvinceName.setText(city.getProvince());

            return builder
                    .setView(view)
                    .setTitle("Edit City")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        city.setName(cityName);
                        city.setProvince(provinceName);
                        listener.editCity(city, position);
                    })
                    .create();
        } else {

            return builder
                    .setView(view)
                    .setTitle("Add a city")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        listener.addCity(new City(cityName, provinceName));
                    })
                    .create();
        }
    }
}

