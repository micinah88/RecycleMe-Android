package com.hackuvic.twoblocksaway.recycleme.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hackuvic.twoblocksaway.recycleme.database.IType;
import com.hackuvic.twoblocksaway.recycleme.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jason, Tzu Hsiang Chen
 * @since March 11, 2018
 */

public class TypeDao extends Dao {
    private static final String TAG = TypeDao.class.getSimpleName();

    /**
     * Inherit Dao constructor.
     *
     * @param context - current context.
     */
    public TypeDao(Context context) {
        super(context, IType.TYPE_TABLE_NAME);
    }

    /**
     * Update an type.
     *
     * @param event - type object.
     */
    public void update(Type type) {
        ContentValues values = new ContentValues();
        values.put(IType.TYPE_COUNT_COLUMN, type.getCount());
        String[] args = {String.valueOf(type.getId())};
        Log.i(TAG, "Updating type");
        super.update(IType.TYPE_ID_COLUMN, args, values);
    }

    /**
     * Find all types
     * @return types - a list of type if it is found
     */
    public List<Type> findAllTypes() {
        List<Type> types = null;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT * FROM " + IType.TYPE_TABLE_NAME, null);
            int count = cursor.getCount();
            Log.d(TAG, "Found types " + count + " row");
            if (count > 0 && cursor.moveToFirst()) {
                types = new ArrayList<>(count);
                do {
                    Type type = new Type();
                    type.setId(cursor.getLong(0));
                    type.setName(cursor.getString(1));
                    type.setCount(cursor.getInt(2));
                    types.add(type);
                } while (cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return types;
    }
}
