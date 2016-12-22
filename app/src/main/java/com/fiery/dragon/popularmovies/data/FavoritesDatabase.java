package com.fiery.dragon.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by hp on 11/26/2016.
 */

@Database(version = FavoritesDatabase.VERSION)
public final class FavoritesDatabase {
    private FavoritesDatabase(){}

    public static final int VERSION = 1;

    @Table(FavoritesColumns.class) public static final String FAVORITES = "favorites";
}
