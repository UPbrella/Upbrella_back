package upbrella.be.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import upbrella.be.store.entity.StoreImage;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleImageUrlResponse {

    private long id;
    private String imageUrl;

    public static SingleImageUrlResponse createImageUrlResponse(StoreImage imageUrl) {

        return SingleImageUrlResponse.builder()
                .id(imageUrl.getId())
                .imageUrl(imageUrl.getImageUrl())
                .build();
    }
}
