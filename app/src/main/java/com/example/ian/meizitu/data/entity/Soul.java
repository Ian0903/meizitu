package com.example.ian.meizitu.data.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by Ian on 2018/4/4.
 */

public class Soul implements Serializable {
    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") public long id;
}
