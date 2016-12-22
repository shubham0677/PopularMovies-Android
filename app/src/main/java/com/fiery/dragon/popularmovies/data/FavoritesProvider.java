package com.fiery.dragon.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by hp on 11/26/2016.
 */


@ContentProvider(authority = FavoritesProvider.AUTHORITY, database = FavoritesDatabase.class)
public final class FavoritesProvider {
    public static final String AUTHORITY =
            "com.fiery.dragon.popularmovies.data.FavoritesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVORITES = "favorites";
    }

    private static Uri buildUri(String ... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for(String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = FavoritesDatabase.FAVORITES)
    public static class Favorites {
        @ContentUri(
                path = Path.FAVORITES,
                type = "vnd.android.cursor.dir/favorite",
                defaultSort = FavoritesColumns._ID + " DESC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVORITES);

        @InexactContentUri(
                name = "FAVORITE_ID",
                path = Path.FAVORITES + "/#",
                type = "vnd.android.cursor.item/favorite",
                whereColumn = FavoritesColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.FAVORITES, String.valueOf(id));
        }
    }
}
