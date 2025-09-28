package br.edu.utfpr.heliobrandao.viniveritas.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.edu.utfpr.heliobrandao.viniveritas.modelo.Conversor;
import br.edu.utfpr.heliobrandao.viniveritas.modelo.Vinho;

@Database(entities = {Vinho.class}, version = 1, exportSchema = false)
@TypeConverters({Conversor.class})
public abstract class VinhosDatabase extends RoomDatabase {

    public abstract VinhoDao getVinhoDao();

    private static VinhosDatabase INSTANCE;

    public static VinhosDatabase getInstance(final Context context) {

        if (INSTANCE == null) {

            synchronized (VinhosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                                    VinhosDatabase.class,
                                                    "vinhos.db")
                                                    .allowMainThreadQueries()
                                                    .build();
                }
            }
        }

        return INSTANCE;

    }

}
