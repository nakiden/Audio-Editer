package sample.model;

/**
 * Created by force on 19.03.2016.
 */
public class MyFile {
    private int id;
    private String name;
    private String size;
    private String path;

    public MyFile(int id, String name, String size,String path) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.path = path;
    }
    public MyFile(String name, String size,String path) {
        this.name = name;
        this.size = size;
        this.path = path;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) { this.size = size; }

    public String getPath() {
        return path;
    }
    public void setPath(String path) { this.path = path; }

}
