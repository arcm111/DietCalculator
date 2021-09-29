package com.arcm.dietcalculator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.arcm.dietcalculator.DietDate;

import java.util.List;

@Dao
public abstract class DiaryItemDao {
    @Transaction
    @Query("SELECT * FROM table_diary WHERE date=:date")
    abstract LiveData<List<DiaryItem>> getItemsByDateAsLiveData(DietDate date);

    @Transaction
    @Query("SELECT * FROM table_diary WHERE date=:date")
    abstract List<DiaryItem> getItemsByDate(DietDate date);

    @Transaction
    @Query("SELECT * FROM table_diary WHERE id=:id LIMIT 1")
    abstract DiaryItem getItemById(long id);

    @Insert
    abstract long insertUnit(UnitEntry unitEntry);

    @Insert
    abstract void insertDiaryEntry(DiaryEntry diaryEntry);

    public void insertDiaryItem(DiaryItem item) {
        if (item.unitEntry != null) {
            item.unitEntry.foodId = item.foodItem.getId();
            item.diaryEntry.unitId = insertUnit(item.unitEntry);
        }
        insertDiaryEntry(item.diaryEntry);
    }
}
