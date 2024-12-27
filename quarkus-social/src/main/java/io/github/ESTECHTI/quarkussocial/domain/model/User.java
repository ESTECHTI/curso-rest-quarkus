package io.github.ESTECHTI.quarkussocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    /*
    * A forma comentada abaixo é sem o PanacheEntity, pois quando é usado o PanacheEntity, o "id" já é mappeado nele mesmo.
    * */
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY) /*GeneratedValue ta dizendo que o campo id é um autoincremento. O GenerationType.IDENTITY delega ao banco de dados a geração desse valor "id".*/
   private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

}
