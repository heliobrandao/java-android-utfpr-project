package br.edu.utfpr.heliobrandao.viniveritas.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import br.edu.utfpr.heliobrandao.viniveritas.modelo.Vinho;

@Dao
public interface VinhoDao {

    @Insert
    long insert(Vinho vinho);

    @Delete
    int delete(Vinho vinho);

    @Update
    int update(Vinho vinho);

    @Query("SELECT * FROM vinho WHERE id = :id")
    Vinho queryForId(long id);

    @Query("SELECT * FROM vinho ORDER BY nome ASC")
    List<Vinho> queryAllAscending();

    @Query("SELECT * FROM vinho ORDER BY nome Desc")
    List<Vinho> queryAllDownward();

}
