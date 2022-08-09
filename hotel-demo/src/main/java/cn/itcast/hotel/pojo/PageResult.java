package cn.itcast.hotel.pojo;

import lombok.Data;

import java.util.List;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-09 11:16
 **/
@Data
public class PageResult {
    private Long total;
    private List<HotelDoc> hotels;

    public PageResult() {

    }

    public PageResult(Long total, List<HotelDoc> hotels) {
        this.total = total;
        this.hotels = hotels;
    }
}
