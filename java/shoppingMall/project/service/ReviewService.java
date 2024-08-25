package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.domain.ReviewDto;
import shoppingMall.project.entity.*;
import shoppingMall.project.repository.ImgRepository;
import shoppingMall.project.repository.ItemRepository;
import shoppingMall.project.repository.ReviewRepository;
import shoppingMall.project.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ItemRepository itemRepository;
    private final ImgRepository imgRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ImgService imgService;

    //리뷰 VIEW단
    public ReviewDto getReview(Long itemId){
        //given 상품id,이름,사진 데이터 가져옴
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemImg img = imgRepository.findByItemIdAndImgYn(itemId,"Y");
        //when dto 객체에 정보 담음
        ReviewDto dto = new ReviewDto(item.getId(),img.getImgUrl(),item.getItemNm(),item.getPrice());
        //객체 리턴
        return dto;
    }
    //리뷰 등록
    public void addReview(ReviewDto dto, String username, MultipartFile file) throws IOException {
        System.out.println("서비스");
        //given - 필요한 엔티티객체 get

        Item item = itemRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        //when - 리뷰 객체 생성 -> 이미지 객체 생성
        Review review = Review.builder()
                .star(dto.getStar())
                .context(dto.getText())
                .item(item)
                .user(user)
                .build();
        if (file.isEmpty()){
            System.out.println("이미지 추가 안함");
            reviewRepository.save(review);
            return;
        }
        reviewRepository.save(review);
        RevImg revImg = new RevImg();
        revImg.setReview(review);
        imgService.reviewImg(revImg,file);
    }
    //리뷰 가져오기
    public Page<ReviewDto> reviewDtos(Long id, Pageable pageable){
        System.out.println("리뷰 서비스");
        //given -> 해당 상품 id로 필요한 데이터 를 포함한 리뷰dto 객체 가져오기
        List<ReviewDto> list = reviewRepository.findReview(id,pageable);
        Long count = reviewRepository.findReviewCount(id);
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println(count);
        //when -> 구현 객체로 리턴
        return new PageImpl<>(list,pageable,count);

    }
    public Long totalcount(Long id){
        return reviewRepository.findReviewCount(id);
    }
}
