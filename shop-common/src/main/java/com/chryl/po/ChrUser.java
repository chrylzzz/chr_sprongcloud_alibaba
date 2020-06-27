package com.chryl.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Chr.yl on 2020/6/7.
 *
 * @author Chr.yl
 */
@Entity(name = "chr_user")
@Data
public class ChrUser implements Serializable {

    private static final long serialVersionUID = -3804108461815405271L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "age")
    private Integer age;

    @Column(name = "is_use")
    private Integer isUse;//是否使用

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "avatar")
    private String avatar;//头像

    @Column(name = "phone")
    private String phone;
}
