package upbrella.be.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upbrella.be.store.dto.request.UpdateStoreRequest;
import upbrella.be.store.dto.response.*;
import upbrella.be.store.entity.BusinessHour;
import upbrella.be.store.entity.Classification;
import upbrella.be.store.entity.StoreDetail;
import upbrella.be.store.entity.StoreMeta;
import upbrella.be.store.exception.NonExistingStoreDetailException;
import upbrella.be.store.repository.StoreDetailRepository;
import upbrella.be.umbrella.service.UmbrellaService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreDetailService {

    private final ClassificationService classificationService;
    private final UmbrellaService umbrellaService;
    private final StoreDetailRepository storeDetailRepository;
    private final BusinessHourService businessHourService;
    private final StoreImageService storeImageService;

    @Transactional
    public void updateStore(Long storeId, UpdateStoreRequest request) {

        StoreDetail storeDetailById = findStoreDetailById(storeId);
        long storeMetaId = storeDetailById.getStoreMeta().getId();

        Classification classification = classificationService.findClassificationById(request.getClassificationId());
        Classification subClassification = classificationService.findSubClassificationById(request.getSubClassificationId());

        List<BusinessHour> businessHours = businessHourService.updateBusinessHour(storeMetaId, request.getBusinessHours());

        StoreMeta storeMetaForUpdate = StoreMeta.createStoreMetaForUpdate(request, classification, subClassification, businessHours);

        StoreMeta foundStoreMeta = storeDetailById.getStoreMeta();
        foundStoreMeta.updateStoreMeta(storeMetaForUpdate);

        storeDetailById.updateStore(foundStoreMeta, request);
    }

    @Transactional(readOnly = true)
    public StoreDetail findStoreDetailById(Long storeId) {

        return storeDetailRepository.findById(storeId)
                .orElseThrow(() -> new NonExistingStoreDetailException("[ERROR] 존재하지 않는 가게입니다."));
    }

    @Transactional
    public StoreFindByIdResponse findStoreDetailByStoreMetaId(long storeDetailId) {

        StoreDetail storeDetail = findStoreDetailById(storeDetailId);
        long storeMetaId = storeDetail.getStoreMeta().getId();

        long availableUmbrellaCount = umbrellaService.countAvailableUmbrellaAtStore(storeMetaId);

        return StoreFindByIdResponse.fromStoreDetail(storeDetail, availableUmbrellaCount);
    }

    @Transactional
    public List<SingleStoreResponse> findAllStores() {

        List<StoreDetail> storeDetails = storeDetailRepository.findAllStores();
        return storeDetails.stream()
                .map(this::createSingleStoreResponse)
                .collect(Collectors.toList());
    }

    public SingleStoreResponse createSingleStoreResponse(StoreDetail storeDetail) {

        List<SingleImageUrlResponse> sortedImageUrls = storeDetail.getSortedStoreImages().stream()
                .map(SingleImageUrlResponse::createImageUrlResponse)
                .collect(Collectors.toList());

        String thumbnail = storeImageService.createThumbnail(sortedImageUrls);
        Set<BusinessHour> businessHourSet = storeDetail.getStoreMeta().getBusinessHours();
        List<BusinessHour> businessHourList = new ArrayList<>(businessHourSet);
        List<SingleBusinessHourResponse> businessHours = businessHourService.createBusinessHourResponse(businessHourList);
        return SingleStoreResponse.ofCreateSingleStoreResponse(storeDetail, thumbnail, sortedImageUrls, businessHours);
    }

    public AllStoreIntroductionResponse findAllStoreIntroductions() {

        List<StoreDetail> storeDetails = storeDetailRepository.findAllStores();

        Map<Long, List<StoreDetail>> collected = storeDetails.stream()
                .collect(Collectors.groupingBy(storeDetail -> storeDetail.getStoreMeta().getSubClassification().getId()));

        //같은 ID끼리 리스트로 모은 것을 StoreIntroductionsResponseByClassification으로 변환
        List<StoreIntroductionsResponseByClassification> storeDetailsByClassification = collected.entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry::getKey))
                .map(entry -> StoreIntroductionsResponseByClassification.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return AllStoreIntroductionResponse.of(storeDetailsByClassification);
    }

    public void saveStoreDetail(StoreDetail storeDetail) {

        storeDetailRepository.save(storeDetail);
    }
}
