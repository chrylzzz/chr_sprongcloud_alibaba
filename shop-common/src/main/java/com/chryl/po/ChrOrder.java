package com.chryl.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@Entity(name = "chr_order")
@Data
public class ChrOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Integer oid;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "username")
    private String username;

    @Column(name = "goodsid")
    private Integer goodsid;

    @Column(name = "goodsname")
    private String goodsname;

    @Column(name = "goodsprice")
    private BigDecimal goodsprice;

    @Column(name = "number")
    private Integer number;
}
