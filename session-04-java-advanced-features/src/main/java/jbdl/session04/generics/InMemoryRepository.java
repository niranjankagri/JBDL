package jbdl.session04.generics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A generic repository, the shape Spring Data's CrudRepository<T, ID> has (Session 11): one class
 * stores any entity type, keyed by any id type, with full type safety.
 *
 * @param <T>  the entity type
 * @param <ID> the id type
 */
public class InMemoryRepository<T, ID> {

    private final Map<ID, T> rows = new LinkedHashMap<>();
    private final Function<T, ID> idOf;

    public InMemoryRepository(Function<T, ID> idOf) {
        this.idOf = idOf;
    }

    public T save(T entity) {
        rows.put(idOf.apply(entity), entity);
        return entity;
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(rows.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(rows.values());
    }

    public boolean deleteById(ID id) {
        return rows.remove(id) != null;
    }

    public int count() {
        return rows.size();
    }
}
