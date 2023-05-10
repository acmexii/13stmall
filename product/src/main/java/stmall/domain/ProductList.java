package stmall.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ProductList_table")
@Data
public class ProductList {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
