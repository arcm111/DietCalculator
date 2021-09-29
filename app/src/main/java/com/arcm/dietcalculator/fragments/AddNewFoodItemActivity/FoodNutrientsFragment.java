package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.R;

import org.jetbrains.annotations.NotNull;

public class FoodNutrientsFragment extends Fragment {
    public static final String TITLE = "Nutrients";
    
    private TextView caltv;
    private TextView carbtv;
    private TextView sugartv;
    private TextView proteintv;
    private TextView fattv;
    private TextView satfattv;
    private TextView fibretv;
    private ProgressBar calsPbar;
    private ProgressBar carbsPbar;
    private ProgressBar proteinPbar;
    private ProgressBar fatPbar;
    private ProgressBar sfatPbar;
    private ProgressBar fibrePbar;
    private ProgressBar sugarPbar;

    private boolean valuesInitiated = false;
    private NutrientsFragment.NutrientsViewModel nvm;

    public FoodNutrientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.nvm = new ViewModelProvider(requireActivity()).get(NutrientsFragment.NutrientsViewModel.class);
        nvm.getWeightMutableLiveData().observe(this, weight -> updateValues(nvm.getNutrients()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrients_section, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.caltv = view.findViewById(R.id.calories_editText);
        this.carbtv = view.findViewById(R.id.carbs_value);
        this.sugartv = view.findViewById(R.id.sugar_value);
        this.proteintv = view.findViewById(R.id.protein_value);
        this.fattv = view.findViewById(R.id.fat_value);
        this.satfattv = view.findViewById(R.id.sfat_value);
        this.fibretv = view.findViewById(R.id.fiber_value);
        this.calsPbar = view.findViewById(R.id.calories_progressbar);
        this.carbsPbar = view.findViewById(R.id.carbs_progressbar);
        this.proteinPbar = view.findViewById(R.id.protein_progressbar);
        this.fatPbar = view.findViewById(R.id.fat_progressbar);
        this.sfatPbar = view.findViewById(R.id.sfat_progressbar);
        this.sugarPbar = view.findViewById(R.id.sugar_progressbar);
        this.fibrePbar = view.findViewById(R.id.fibre_progressbar);
    }

    protected void setCalValue(String value) {
        caltv.setText(value);
    }
    
    protected void setCarbValue(String value) {
        carbtv.setText(value);
    }
    
    protected void setSugarValue(String value) {
        sugartv.setText(value);
    }
    
    protected void setProteinValue(String value) {
        proteintv.setText(value);
    }
    
    protected void setFatValue(String value) {
        fattv.setText(value);
    }
    
    protected void setSatfatValue(String value) {
        satfattv.setText(value);
    }
    
    protected void setFibreValue(String value) {
        fibretv.setText(value);
    }
        
    protected void setCalsMaxValue(int value) {
		calsPbar.setMax(value);
	}

    protected void setCarbsMaxValue(int value) {
		carbsPbar.setMax(value);
	}

    protected void setProteinMaxValue(int value) {
		proteinPbar.setMax(value);
	}

    protected void setFatMaxValue(int value) {
		fatPbar.setMax(value);
	}

    protected void setSfatMaxValue(int value) {
		sfatPbar.setMax(value);
	}

    protected void setSugarMaxValue(int value) {
		sugarPbar.setMax(value);
	}

    protected void setFibreMaxValue(int value) {
		fibrePbar.setMax(value);
	}

    protected void setCalsProgressValue(int value) {
		calsPbar.setProgress(value);
	}

    protected void setCarbsProgressValue(int value) {
		carbsPbar.setProgress(value);
	}

    protected void setProteinProgressValue(int value) {
		proteinPbar.setProgress(value);
	}

    protected void setFatProgressValue(int value) {
		fatPbar.setProgress(value);
	}

    protected void setSfatProgressValue(int value) {
		sfatPbar.setProgress(value);
	}

    protected void setSugarProgressValue(int value) {
		sugarPbar.setProgress(value);
	}

    protected void setFibreProgressValue(int value) {
		fibrePbar.setProgress(value);
	}

    protected void initValues(Nutrients item) {
        setCalValue(item.getCalsAsText());
        setCarbValue(item.getCarbsAsText());
        setSugarValue(item.getSugarAsText());
        setProteinValue(item.getProteinAsText());
        setFatValue(item.getFatAsText());
        setSatfatValue(item.getSaturatedFatAsText());
        setFibreValue(item.getFibreAsText());

        setCarbsProgressValue(item.getCarbsPercentage());
        setProteinProgressValue(item.getProteinPercentage());
        setFatProgressValue(item.getFatPercentage());
        setSfatProgressValue(item.getSaturatedFatPercentage());
        setSugarProgressValue(item.getSugarPercentage());
        setFibreProgressValue(item.getFibrePercentage());

        setValuesInitiated();
    }

    public void updateValues(Nutrients item) {
        if (isValuesInitiated()) {
            setCalValue(item.getCalsAsText());
            setCarbValue(item.getCarbsAsText());
            setSugarValue(item.getSugarAsText());
            setProteinValue(item.getProteinAsText());
            setFatValue(item.getFatAsText());
            setSatfatValue(item.getSaturatedFatAsText());
            setFibreValue(item.getFibreAsText());
        } else initValues(item);
    }

    protected void setValuesInitiated() {
        this.valuesInitiated = true;
    }

    protected boolean isValuesInitiated() {
        return valuesInitiated;
    }

    protected DietDate getDate() {
        return nvm.date;
    }
}