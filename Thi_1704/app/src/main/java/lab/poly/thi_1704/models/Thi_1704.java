package lab.poly.thi_1704.models;

public class Thi_1704 {
    private String _id;
    private String hoten_ph36088;
    private String mon_thi_ph36088;
    private String hinh_anh_ph36088;
    private String ngay_thi_ph36088;
    private int ca_thi_ph36088;

    public Thi_1704() {
    }

    public Thi_1704(String _id, String hoten_ph36088, String mon_thi_ph36088, String hinh_anh_ph36088, String ngay_thi_ph36088, int ca_thi_ph36088) {
        this._id = _id;
        this.hoten_ph36088 = hoten_ph36088;
        this.mon_thi_ph36088 = mon_thi_ph36088;
        this.hinh_anh_ph36088 = hinh_anh_ph36088;
        this.ngay_thi_ph36088 = ngay_thi_ph36088;
        this.ca_thi_ph36088 = ca_thi_ph36088;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHoten_ph36088() {
        return hoten_ph36088;
    }

    public void setHoten_ph36088(String hoten_ph36088) {
        this.hoten_ph36088 = hoten_ph36088;
    }

    public String getMon_thi_ph36088() {
        return mon_thi_ph36088;
    }

    public void setMon_thi_ph36088(String mon_thi_ph36088) {
        this.mon_thi_ph36088 = mon_thi_ph36088;
    }

    public String getHinh_anh_ph36088() {
        return hinh_anh_ph36088;
    }

    public void setHinh_anh_ph36088(String hinh_anh_ph36088) {
        this.hinh_anh_ph36088 = hinh_anh_ph36088;
    }

    public String getNgay_thi_ph36088() {
        return ngay_thi_ph36088;
    }

    public void setNgay_thi_ph36088(String ngay_thi_ph36088) {
        this.ngay_thi_ph36088 = ngay_thi_ph36088;
    }

    public int getCa_thi_ph36088() {
        return ca_thi_ph36088;
    }

    public void setCa_thi_ph36088(int ca_thi_ph36088) {
        this.ca_thi_ph36088 = ca_thi_ph36088;
    }
}
