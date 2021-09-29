package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_recipes")
public class RecipeEntry {
    public final static String DEFAULT_AUTHOR = "Me";

    @PrimaryKey
    public long id;

    public String name;

    @NonNull
    public String author = DEFAULT_AUTHOR;

    @Embedded
    public Nutrients nutrients;
}
