package persist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T> {
    private final Gson gson;
    private final String filePath;
    private final Type listType;

    protected GenericDAO(String filePath, Type listType) {
        this.filePath = filePath;
        this.listType = listType;
        gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy")
                .create();
    }

    public void create(T entity) {
        List<T> entities = readAll();
        entities.add(entity);
        writeAll(entities);
    }

    public T read(int id) {
        List<T> entities = readAll();
        for (T entity : entities) {
            if (getId(entity) == id) {
                return entity;
            }
        }
        return null;
    }

    public List<T> readAll() {
        try (FileReader reader = new FileReader(filePath)) {
            List<T> entities = gson.fromJson(reader, listType);
            return entities != null ? entities : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void update(T entity) {
        List<T> entities = readAll();
        for (int i = 0; i < entities.size(); i++) {
            if (getId(entities.get(i)) == getId(entity)) {
                entities.set(i, entity);
                writeAll(entities);
                return;
            }
        }
    }

    public void delete(int id) {
        List<T> entities = readAll();
        entities.removeIf(entity -> getId(entity) == id);
        writeAll(entities);
    }

    private void writeAll(List<T> entities) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(entities, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract int getId(T entity);
}
