package server.database.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Invoice {
    private Long id;
    @NonNull
    private String orderName;
    @NonNull
    private Long price;
}
