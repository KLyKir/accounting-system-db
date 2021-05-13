package server.database.domain.enums;

import lombok.Getter;

public enum SoftwareType {
    @Getter
    MOBILE{
        private String type = "Mobile";

    },
    @Getter
    API{
        private String type = "Web";
    },
    @Getter
    DESKTOP{
        private String type = "Desktop";
    }
}
