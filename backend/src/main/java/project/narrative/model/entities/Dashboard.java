package project.narrative.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "dashboard")
public class Dashboard {

    @Id
    private Long boardid;
}