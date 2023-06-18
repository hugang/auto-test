package io.hugang.autotest.entity;


import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "command")
@Data
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String command;

    private String scriptId;

    private String target;

    private String value;

    private String description;

}
