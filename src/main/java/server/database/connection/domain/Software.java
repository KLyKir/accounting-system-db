package server.database.connection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import server.database.connection.domain.enums.SoftwareType;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Software {

    private Long id;

    @NonNull
    private String name;
    @NonNull
    private SoftwareType softwareType;
}
