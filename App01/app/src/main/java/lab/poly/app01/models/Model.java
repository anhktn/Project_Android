package lab.poly.app01.models;

public class Model {
    private String _id;
    private String ten_ph36088;
    private String the_loai_ph36088;
    private int gia_ph36088;
    private int so_luong_ban_ph36088;
    private String ngay_ban_ph36088;
    private String hinh_anh_ph36088;

    public Model() {
    }

    public Model(String _id, String ten_ph36088, String the_loai_ph36088, int gia_ph36088, int so_luong_ban_ph36088, String ngay_ban_ph36088, String hinh_anh_ph36088) {
        this._id = _id;
        this.ten_ph36088 = ten_ph36088;
        this.the_loai_ph36088 = the_loai_ph36088;
        this.gia_ph36088 = gia_ph36088;
        this.so_luong_ban_ph36088 = so_luong_ban_ph36088;
        this.ngay_ban_ph36088 = ngay_ban_ph36088;
        this.hinh_anh_ph36088 = hinh_anh_ph36088;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen_ph36088() {
        return ten_ph36088;
    }

    public void setTen_ph6088(String ten_ph6088) {
        this.ten_ph36088 = ten_ph36088;
    }

    public String getThe_loai_ph36088() {
        return the_loai_ph36088;
    }

    public void setThe_loai_ph36088(String the_loai_ph36088) {
        this.the_loai_ph36088 = the_loai_ph36088;
    }

    public int getGia_ph36088() {
        return gia_ph36088;
    }

    public void setGia_ph36088(int gia_ph36088) {
        this.gia_ph36088 = gia_ph36088;
    }

    public int getSo_luong_ban_ph36088() {
        return so_luong_ban_ph36088;
    }

    public void setSo_luong_ban_ph36088(int so_luong_ban_ph36088) {
        this.so_luong_ban_ph36088 = so_luong_ban_ph36088;
    }

    public String getNgay_ban_ph36088() {
        return ngay_ban_ph36088;
    }

    public void setNgay_ban_ph36088(String ngay_ban_ph36088) {
        this.ngay_ban_ph36088 = ngay_ban_ph36088;
    }

    public String getHinh_anh_ph36088() {
        return hinh_anh_ph36088;
    }

    public void setHinh_anh_ph36088(String hinh_anh_ph36088) {
        this.hinh_anh_ph36088 = hinh_anh_ph36088;
    }
}
