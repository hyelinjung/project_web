package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.ItemImg;
import shoppingMall.project.entity.RevImg;
import shoppingMall.project.repository.ImgRepository;
import shoppingMall.project.repository.ItemRepository;
import shoppingMall.project.repository.RevImgRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ImgService {
    @Value("${itemImgLocation}")
     String itemImgLocation;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    FileService fileService;
    @Autowired
    RevImgRepository revImgRepository;

    public void uploadImg(ItemImg itemImg, MultipartFile file) throws IOException {
        System.out.println("이미지 업로드");
        String imgOriginNm = file.getOriginalFilename();
        String imgUrl ="";
        String imgNm ="";

        assert imgOriginNm != null;
        imgNm = fileService.loadFile(itemImgLocation, file.getBytes(), imgOriginNm);
             imgUrl = "/img/item/" + imgNm;

        itemImg.updateImg(imgOriginNm,imgUrl,imgNm);
        imgRepository.save(itemImg);
    }


    public void updateImg(Long itemimgid, MultipartFile file) throws Exception {
        System.out.println("이미지수정1");
        if (itemimgid != null) { //새로 등록한 이미지 말고 기존 있던 이미지 처리
            ItemImg savedItemImg = imgRepository.findById(itemimgid).orElseThrow(EntityNotFoundException::new);
            if (!StringUtils.isEmpty(savedItemImg.getImgNm())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgNm());
            }


            String or = file.getOriginalFilename();
            String imgNm = fileService.loadFile(itemImgLocation, file.getBytes(), or);
            String imgUrl = "/img/item/" + imgNm;
            savedItemImg.updateImg(or, imgUrl, imgNm);
            imgRepository.save(savedItemImg);
    }
    }
    //리뷰 사진 저장
    public void reviewImg(RevImg revImg, MultipartFile file) throws IOException {
        System.out.println("리뷰 이미지 업로드");
        String imgOriginNm = file.getOriginalFilename();
        String imgUrl ="";
        String imgNm ="";

        assert imgOriginNm != null;
        imgNm = fileService.loadFile(itemImgLocation, file.getBytes(), imgOriginNm);
        imgUrl = "/img/item/" + imgNm;

       revImg.setImgOriNm(imgOriginNm);
       revImg.setImgUrl(imgUrl);
        revImgRepository.save(revImg);
    }
}
