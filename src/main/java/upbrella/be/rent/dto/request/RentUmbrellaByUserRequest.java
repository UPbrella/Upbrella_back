package upbrella.be.rent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentUmbrellaByUserRequest {

    private String region;
    private int storeId;
    private int umbrellaId;
    private String statusDeclaration;
}