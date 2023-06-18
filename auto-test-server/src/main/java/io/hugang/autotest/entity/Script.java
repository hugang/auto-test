package io.hugang.autotest.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "script")
@Data
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scriptName;

}
