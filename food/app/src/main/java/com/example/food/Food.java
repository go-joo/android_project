package com.example.food;

import java.io.Serializable;
import java.util.ArrayList;

public class Food implements Serializable {
    private int seq;
    private String name;
    private String tel;
    private String address;
    private double latitude;
    private double longitude;
    private String description;
    private int image;
    private int keep;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }

    @Override
    public String toString() {
        return "Food{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", keep=" + keep +
                ", photo='" + photo + '\'' +
                '}';
    }

    static public ArrayList<Food> createData() {
        ArrayList<Food> array = new ArrayList<>();
        Food food = new Food();
        food.setSeq(0);
        food.setName("명태어장");
        food.setAddress("인천광역시 서구 경서동 976-41");
        food.setTel("0325688665");
        food.setLatitude(37.5242415);
        food.setLongitude(126.6277588);
        food.setImage(R.drawable.image1);
        food.setDescription("밑반찬도 잘나오고 콩나물은 그냥 먹어도 되고 명태조림에 넣어서 비벼먹으면 더 맛있고 구운김에 명태조림을 싸서 먹어요.");
        food.setKeep(1);

        array.add(food);
        food = new Food();
        food.setSeq(1);
        food.setName("선식당");
        food.setAddress("인천광역시 서구 경서동 보석로11번길 13");
        food.setTel("0325635635");
        food.setLatitude(37.5255);
        food.setLongitude(126.6308);
        food.setImage(R.drawable.image2);
        food.setDescription("청라5단지 선식당은 퓨전요리식당이예요. 메뉴고르기 힘들 때 오면 딱이에요. 중식,양식 한식이 다 있어요.");
        food.setKeep(0);

        array.add(food);
        food = new Food();
        food.setSeq(2);
        food.setName("홍익돈까스");
        food.setAddress("인천광역시 서구 청라동 132-2");
        food.setTel("0325666344");
        food.setLatitude(37.5389135);
        food.setLongitude(126.6583786);
        food.setImage(R.drawable.image3);
        food.setDescription("홍익 돈까스는 홍익의 정신을 바탕으로 고객 만족을 최우선으로 생각하며 최고의 맛과 서비스를 제공하기위해 노력합니다.");
        array.add(food);
        food.setKeep(1);
        return array;
    }
}
