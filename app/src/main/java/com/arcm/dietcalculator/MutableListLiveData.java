package com.arcm.dietcalculator;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class MutableListLiveData<T> extends MutableLiveData<List<T>> {
    private final MutableLiveData<Long> lastModified = new MutableLiveData<>();
    private List<T> items;
    private ListObserver<List<T>> callback;

    public MutableListLiveData() {
        this.items = new ArrayList<>();
    }

    public void addItem(T item) {
        items.add(item);
        onListModified();
    }

    public void removeItem(int position) {
        items.remove(position);
        onListModified();
    }

    public void updateItem(int position, T item) {
        items.set(position, item);
        onListModified();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    private void onListModified() {
        lastModified.setValue(System.currentTimeMillis());
    }

    @Override
    public List<T> getValue() {
        return items;
    }

    @Override
    public void setValue(List<T> items) {
        this.items = items;
        onListModified();
    }

    public void observe(@NonNull LifecycleOwner owner, ListObserver<List<T>> callback) {
        this.callback = callback;
        lastModified.observe(owner, this::onListItemsChanged);
    }

    private void onListItemsChanged(long time) {
        if (callback != null) callback.onListItemsChanged(items, items.size());
    }

    public interface ListObserver<T> {
        void onListItemsChanged(T items, int size);
    }
}
