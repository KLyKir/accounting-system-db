package server.database.connection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Staff {
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private Long salary;
    @NonNull
    private String workPosition;
}
