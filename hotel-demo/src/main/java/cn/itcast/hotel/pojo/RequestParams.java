package cn.itcast.hotel.pojo;

import lombok.Data;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-09 11:14
 **/
@Data
public class RequestParams {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String city;
    private String brand;
    private Integer maxPrice;
    private Integer minPrice;
    private String starName;
    private String location;
}
