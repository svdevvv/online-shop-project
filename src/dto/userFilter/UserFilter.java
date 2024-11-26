package dto.userFilter;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class UserFilter {
    private int limit;
    private int offset;
    private String role;

}
