package com.chryl.po;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

/**
 * 消息事务状态记录
 * Created by Chr.yl on 2020/6/26.
 *
 * @author Chr.yl
 */
@Entity(name = "chr_txlog")
@Data
public class ChrTxLog {

    private String txId;

    private Date date;
}
