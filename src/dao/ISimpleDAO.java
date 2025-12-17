package dao;

import java.util.List;

public interface ISimpleDAO<T, ID>
{
    void create(T t) throws Exception;
    T readOne(ID id) throws Exception; // read
    List<T> readAll() throws Exception;
    List<T> readBy(String filtro) throws Exception;
    void update(T t) throws Exception;
    void delete(ID id) throws Exception;
}
