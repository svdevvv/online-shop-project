package entity.userEntity;

import entity.roleEntity.RoleEntity;
import lombok.*;


import javax.management.relation.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserEntity {
    private Integer id;
    private String login;
    private String password;
    private RoleEntity role;


}