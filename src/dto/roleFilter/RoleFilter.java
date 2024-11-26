package dto.roleFilter;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class RoleFilter {
    private int limit;
    private int offset;
    private String role;

}
