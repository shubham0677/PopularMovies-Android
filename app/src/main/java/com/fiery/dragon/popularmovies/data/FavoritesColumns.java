package com.fiery.dragon.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by hp on 11/26/2016.
 */

public interface FavoritesColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @Unique
    public static final String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String RATING = "rating";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String OVERVIEW = "overview";

}
