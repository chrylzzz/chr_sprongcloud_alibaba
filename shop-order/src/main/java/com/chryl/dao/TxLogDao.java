package com.chryl.dao;

import com.chryl.po.ChrTxLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
public interface TxLogDao extends JpaRepository<ChrTxLog, String> {
}
