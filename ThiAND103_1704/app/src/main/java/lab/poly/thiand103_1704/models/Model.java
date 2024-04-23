package lab.poly.thiand103_1704.models;

public class Model {
    private String _id;

    private String ph36088_ten_thietbi;
    private String ph36088_mota;
    private String ph36088_hinh_anh;
    private String ph36088_ngay_nhap;
    private int ph36088_trang_thai;

    public Model() {
    }

    public Model(String _id, String ph36088_ten_thietbi, String ph36088_mota, String ph36088_hinh_anh, String ph36088_ngay_nhap, int ph36088_trang_thai) {
        this._id = _id;
        this.ph36088_ten_thietbi = ph36088_ten_thietbi;
        this.ph36088_mota = ph36088_mota;
        this.ph36088_hinh_anh = ph36088_hinh_anh;
        this.ph36088_ngay_nhap = ph36088_ngay_nhap;
        this.ph36088_trang_thai = ph36088_trang_thai;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPh36088_ten_thietbi() {
        return ph36088_ten_thietbi;
    }

    public void setPh36088_ten_thietbi(String ph36088_ten_thietbi) {
        this.ph36088_ten_thietbi = ph36088_ten_thietbi;
    }

    public String getPh36088_mota() {
        return ph36088_mota;
    }

    public void setPh36088_mota(String ph36088_mota) {
        this.ph36088_mota = ph36088_mota;
    }

    public String getPh36088_hinh_anh() {
        return ph36088_hinh_anh;
    }

    public void setPh36088_hinh_anh(String ph36088_hinh_anh) {
        this.ph36088_hinh_anh = ph36088_hinh_anh;
    }

    public String getPh36088_ngay_nhap() {
        return ph36088_ngay_nhap;
    }

    public void setPh36088_ngay_nhap(String ph36088_ngay_nhap) {
        this.ph36088_ngay_nhap = ph36088_ngay_nhap;
    }

    public int getPh36088_trang_thai() {
        return ph36088_trang_thai;
    }

    public void setPh36088_trang_thai(int ph36088_trang_thai) {
        this.ph36088_trang_thai = ph36088_trang_thai;
    }
}
