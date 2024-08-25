package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.domain.*;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.ItemImg;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.ImgRepository;
import shoppingMall.project.repository.ItemRepository;
import shoppingMall.project.repository.UserRepository;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    ImgService imgService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    UserRepository userRepository;

    //상품 저장
    public Long saveItem(ItemDto itemDto, List<MultipartFile> multipartFileList) throws IOException {
        System.out.println("아이템 들어감:"+itemDto);
        Item item = new Item();
        int discountedPrice = (int) (itemDto.getPrice() - itemDto.getPrice()*itemDto.getDiscount());
        System.out.println("discountedPrice"+discountedPrice);
        itemDto.setDiscountedPrice(discountedPrice);
        if(itemDto.getDiscount() != 0.0){
            itemDto.setDiscount_yn("y");
        }
        item.createItem(itemDto);
        System.out.println("item======"+item);
        itemRepository.save(item);
        //if (!multipartFileList.isEmpty()) {
            System.out.println("입장");
            System.out.println("이미지 길이"+multipartFileList.size());
            for (int i = 0; i < multipartFileList.size(); i++) {
                if (!multipartFileList.get(i).isEmpty()){
                    ItemImg itemImg = new ItemImg();
                    if (i == 0) {
                        itemImg.setImgYn("Y");
                    } else {
                        itemImg.setImgYn("N");
                    }
                    itemImg.setItem(item);
                    imgService.uploadImg(itemImg, multipartFileList.get(i));
                }
            }
        //}
        return item.getId();
    }

    //상품 수정
/*    public Long updateItem(ItemDto itemDto,List<MultipartFile> multipartFileList) throws Exception {
        System.out.println("상품 수정");
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(EntityNotFoundException::new);
        System.out.println("수정전카테:"+item.getLargeItemType());
        item.updateItem(itemDto);
        itemRepository.save(item);
        System.out.println("수정된 카테고리:"+ item.getLargeItemType());
        System.out.println("item수정"+ item);
        System.out.println("길이:"+multipartFileList.size());
        boolean rr = multipartFileList.isEmpty();
        List<Long> imgIds = itemDto.getImgId();
        for (int i =0; i< multipartFileList.size(); i++){
            if(imgIds.size() != 0){
                System.out.println("기본");
                imgService.updateImg(imgIds.get(i), multipartFileList.get(i));
            }else { //이미지db가 삭제되었을 경우 버그 해결
                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);
                if (i==0){
                    itemImg.setImgYn("Y");
                }else {
                    itemImg.setImgYn("N");
                }
                imgService.uploadImg(itemImg,multipartFileList.get(i));
            }

        }
        return item.getId();
    }*/
    public Long updateItem(ItemDto itemDto,List<MultipartFile> multipartFileList) throws Exception {
        System.out.println("상품 수정");
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(EntityNotFoundException::new);
        System.out.println("수정전카테:"+item.getLargeItemType());
        item.updateItem(itemDto);
        itemRepository.save(item);
        System.out.println("수정된 카테고리:"+ item.getLargeItemType());
        System.out.println("item수정"+ item);
        System.out.println("길이:"+multipartFileList.size());

        Long[] imgIds = itemDto.getImgId();
        Long[] temp = new Long[5];
        for (int i =0; i<imgIds.length; i++){
            temp[i]=imgIds[i];
        }
        System.out.println("배열로 함:"+ Arrays.toString(temp));

        for (int i =0; i<multipartFileList.size(); i++){ //
            if (!multipartFileList.get(i).isEmpty()) {
                if (temp[i] != null) { //이미지 다시 등록
                    imgService.updateImg(temp[i], multipartFileList.get(i));
                } else { // 새 이미지 등록
                    System.out.println("????");
                    ItemImg itemImg = new ItemImg();
                    itemImg.setImgYn("N");
                    itemImg.setItem(item);
                    imgService.uploadImg(itemImg, multipartFileList.get(i));
                }

            }
        }

        return item.getId();
    }

    //상품 디테일
    public ItemDto itemDtl(Long id){
        List<ItemImg> itemImgs = imgRepository.findByItemId(id);
        Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDtl(item.getItemDtl());
        itemDto.setItemNm(item.getItemNm());
        itemDto.setPrice(item.getPrice());
        itemDto.setStock(item.getStock());
        itemDto.setId(item.getId());
        itemDto.setSellStatus(item.getSellStatus());
        itemDto.setSex(item.getSex());
        itemDto.setDiscount(item.getDiscount());
        itemDto.setDiscountedPrice(item.getDiscountedPrice());
        itemDto.setDiscount_yn(item.getDiscount_yn());
        itemDto.setLargeItemType(item.getLargeItemType());
        List<ItemImgDto> itemImgDtoList2 = itemDto.getItemImgDtoList();
        int i =0;
        //List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg :itemImgs){
            ItemImgDto dto = new ItemImgDto(itemImg.getId(),itemImg.getImgOriginNm(),itemImg.getImgYn(),itemImg.getImgUrl());
            itemImgDtoList2.add(dto);
            i++;
        }
        //이미지 dto에 이미지 없을 시 null 넣기 -> view에 사진등록 5칸 맞추기 위해서
        for (int j = i;j<5; j++){
            itemImgDtoList2.add(j,null);
        }


        System.out.println("리턴 이미지 길이:"+itemDto.getItemImgDtoList().size());
        System.out.println("리턴 이미지 길이2:"+ Arrays.toString(itemDto.getItemImgDtoList().toArray()));
        System.out.println("리턴 이미지 길이3:"+ itemDto.getItemImgDtoList().get(0).toString());

        return itemDto;
    }

    // 검색 할 수 있는 관리자 상품 리스트, 어떤 검색어로 어떤 테이블에서 가져올건지
    @Transactional(readOnly = true)
    public Page<Item> searchWithItemPage(SearchDto dto, Pageable pageable){  //관리자 상품 검색 관리
        return itemRepository.getAdminItemWithPage(dto,pageable);
    }

   /* @Transactional(readOnly = true) //상품 검색 시 sort 1 등록순 -> 사용안함 삭제예정
    public Page<MainDto> mainPageWsearch(SearchDto dto, Pageable pageable){
        return itemRepository.getMainItemWithPage(dto,pageable);
    }*/
    @Transactional(readOnly = true) //상품 검색 시 default
    public Page<MainDto> itemSearch(SearchDto dto, Pageable pageable){
       // User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return itemRepository.getSearchItemWithPage(dto,pageable);
    }

    /*public Page<MainDto> itemCate(String largeItemType,String subItemType,Pageable pageable){
        List<MainDto> mainDtos = itemRepository.findCate(largeItemType,subItemType,pageable);
        System.out.println("service:"+mainDtos);
        int count = itemRepository.itemCount(largeItemType,subItemType);
        System.out.println("count:"+ count);
        return new PageImpl<>(mainDtos,pageable,count);
    }*/

    public Page<MainDto> itemsCate(CateDto cateDto,SearchDto dto, Pageable pageable){
        return itemRepository.getCateItemWithPage( cateDto, dto,pageable);
    }

    public int getCount(CateDto cateDto){
        int count = (int) itemRepository.getCateItemCount(cateDto);
        System.out.println("카테결과 갯수:"+ count);
        return count;
    }

    public int getSearchCount(SearchDto dto){
        int count = (int) itemRepository.getSearchItemCount(dto);
        System.out.println("검색 갯수:"+ count);
        return count;
    }

    public List<MainDto> findRelate(Long id){
        Item item = itemRepository.findById(id).orElseThrow( EntityNotFoundException::new);
        return itemRepository.findRelate(item.getSex());
    }

    public List<LikeServeDto> serveDtos(CateDto cateDto,SearchDto dto,String username){
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        List<Item> list = itemRepository.getLike(cateDto,dto,user.getId());
        List<LikeServeDto> serveDtos = new ArrayList<>();
        for (Item item: list){
            LikeServeDto likeServeDto = new LikeServeDto();
            likeServeDto.setId(item.getId());
            serveDtos.add(likeServeDto);
        }
        return serveDtos;
    }
    public List<MainDto> popularItem(){
        System.out.println("인기순");
        return itemRepository.popularItem();
    }

    public List<MainDto> mainNewItem(){
        System.out.println("메인 화면 8개 씩");
        List<MainDto> list = new ArrayList<>();
          list.addAll(itemRepository.mainNewItemW());
          list.addAll(itemRepository.mainNewItemM());
          list.addAll(itemRepository.mainNewItemK());
          return list;
    }
}
