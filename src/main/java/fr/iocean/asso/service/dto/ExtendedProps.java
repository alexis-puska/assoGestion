package fr.iocean.asso.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtendedProps implements Serializable {

    private static final long serialVersionUID = -7099145225995009690L;

    private long id;
    private String entity;
}
