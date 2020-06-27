package com.chryl.po;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Chr.yl on 2020/6/10.
 *
 * @author Chr.yl
 */
@Entity(name = "chr_goods")
@Data
public class ChrGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Integer goodsId;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "goods_price")
    private String goodsPrice;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "goods_color")
    private String goodsColor;

    @Column(name = "goods_type")
    private String goodsType;

    @Column(name = "goods_status")
    private String goodsStatus;

    @Column(name = "goods_imp")
    private Integer goodsImp;

    @Column(name = "goods_desc")
    private String goodsDesc;

    @Column(name = "goods_stock")
    private Integer goodsStock;
}
