package upbrella.be.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStoreRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    private long classificationId;
    private long subClassificationId;
    private boolean activateStatus;
    @NotBlank
    private String address;
    @NotBlank
    private String addressDetail;
    @NotBlank
    private String umbrellaLocation;
    @NotBlank
    private String businessHour;
    private String contactNumber;
    private String instagramId;
    @Range(min = -90, max = 90)
    private double latitude;
    @Range(min = -180, max = 180)
    private double longitude;
    private String content;
    private String password;
    private List<SingleBusinessHourRequest> businessHours;
}
