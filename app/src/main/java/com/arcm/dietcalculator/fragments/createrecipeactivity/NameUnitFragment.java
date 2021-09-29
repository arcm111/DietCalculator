package com.arcm.dietcalculator.fragments.createrecipeactivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcm.dietcalculator.PopupNewUnitWindow;
import com.arcm.dietcalculator.R;
import com.arcm.dietcalculator.Utilities;
import com.arcm.dietcalculator.database.UnitEntry;
import com.arcm.dietcalculator.databinding.FragmentNameUnitBinding;
import com.arcm.dietcalculator.viewmodels.RecipeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NameUnitFragment extends Fragment {
    private FragmentNameUnitBinding binding;
    private List<UnitEntry> units;
    private UnitsAdapter adapter;
    private RecipeViewModel rvm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        this.rvm = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        this.units = new ArrayList<>();
        units.add(new UnitEntry(-1, "g", 0));
        binding = FragmentNameUnitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new UnitsAdapter(requireContext());
        binding.nextButton.setOnClickListener(this::onNextButtonClicked);
        binding.unitsAddButton.setOnClickListener(this::onUnitsAddButtonClicked);
        binding.unitsRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.unitsRecyclerview.setAdapter(adapter);
    }

    private void onNextButtonClicked(View view) {
        String title = Utilities.sentenceCapitalise(binding.recipeTitleEdittext.getText().toString().trim());
        String author = Utilities.sentenceCapitalise(binding.recipeAuthorEdittext.getText().toString().trim());
        if (title.isEmpty()) {
            Utilities.showErrorDialog(requireContext(), "Recipe title can not be empty!");
        } else {
            rvm.title.setValue(title);
            rvm.author.setValue(author);
            rvm.units.setValue(units);
            NavController controller = NavHostFragment.findNavController(NameUnitFragment.this);
            controller.navigate(R.id.action_NameUnitFragment_to_IngredientsFragment);
        }
    }

    private void onUnitsAddButtonClicked(View view) {
        showPopupWindow(view);
    }

    private void showPopupWindow(View parent) {
        PopupNewUnitWindow window = new PopupNewUnitWindow(requireContext(), null);
        window.setOnSavedListener(this::onUnitCreated);
        window.showAtLocation(parent, Gravity.START | Gravity.TOP, 0, 0);
    }

    private void showPopupWindow(View parent, UnitEntry unit, int position) {
        PopupNewUnitWindow window = new PopupNewUnitWindow(requireContext(), null, unit);
        window.setOnSavedListener(modifiedUnit -> {
            unit.unitTitle = modifiedUnit.unitTitle;
            unit.unitSize = modifiedUnit.unitSize;
            adapter.notifyItemChanged(position);
        });
        window.showAtLocation(parent, Gravity.START | Gravity.TOP, 0, 0);
    }

    private void onUnitCreated(UnitEntry unit) {
        units.add(unit);
        adapter.notifyItemInserted(units.size() - 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.ViewHolder> {
        private final Context context;
        private final LayoutInflater inflater;

        public UnitsAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item_unit_editable_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            UnitEntry unit = units.get(holder.getBindingAdapterPosition());

            if (unit.unitTitle.equals("g")) {
                holder.labelTextview.setText("g");
                holder.optionsImageview.setVisibility(View.GONE);
            } else {
                String txt = unit.unitTitle + " (" + Utilities.formatNumber(unit.unitSize) + " g)";
                holder.position = holder.getBindingAdapterPosition();
                holder.labelTextview.setText(txt);
                holder.optionsImageview.setOnClickListener(holder::showPopupMenu);
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
            public final TextView labelTextview;
            public final ImageView optionsImageview;

            public int position;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                this.labelTextview = itemView.findViewById(R.id.unit_label_textview);
                this.optionsImageview = itemView.findViewById(R.id.unit_options_imageview);
            }

            private void showPopupMenu(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenuInflater().inflate(R.menu.menu_unit_list_item_options, menu.getMenu());
                menu.setOnMenuItemClickListener(this);
                menu.show();
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.edit_optoin) {
                    UnitEntry unit = units.get(position);
                    showPopupWindow(requireView(), unit, position);
                    return true;
                } else if (menuItem.getItemId() == R.id.delete_option) {
                    units.remove(position);
                    adapter.notifyItemRemoved(position);
                    return true;
                }
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return units.size();
        }
    }
}