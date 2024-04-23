package lab.poly.lab5_and103.model;

import com.google.gson.annotations.SerializedName;

public class FlowerModel {
    @SerializedName("_id")
    private String _id;
    private String name;

    public FlowerModel(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public FlowerModel() {
    }

    public FlowerModel(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
