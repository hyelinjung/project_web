package shoppingMall.project.repository;



import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;


import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.domain.CateDto;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.QMainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.entity.*;
import shoppingMall.project.entity.QItem;
import shoppingMall.project.entity.QItemImg;
import shoppingMall.project.entity.QLikes;
import shoppingMall.project.entity.QReview;


import java.time.LocalDateTime;
import java.util.List;

public class CustomRepositoryImpl implements CustomRepository{

    private JPAQueryFactory jpaQueryFactory ;

    public CustomRepositoryImpl(EntityManager entityManager) {
        jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    //상품검색부분 -날짜 순
    private BooleanExpression regDate(String searchDate){
        LocalDateTime localDateTime = LocalDateTime.now();
        if (StringUtils.equals("all",searchDate) || searchDate ==null){
            return  null;
        } else if (StringUtils.equals("1d",searchDate)) {
            localDateTime = localDateTime.minusDays(1);
        } else if (StringUtils.equals("1w",searchDate)) {
            localDateTime = localDateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDate)){
            localDateTime = localDateTime.minusMonths(1);
        } else if (StringUtils.equals("6m",searchDate)) {
            localDateTime = localDateTime.minusMonths(6);
        }
        return QItem.item.regDateTime.after(localDateTime);
    }


    // 검색 조건 - 상품 타입


    //검색조건- 상품명
    private BooleanExpression itemSearchBy (String searchBy) {
        System.out.println("에바적"+searchBy);
            //return StringUtils.isEmpty(searchBy)  ? null : QItem.item.itemNm.like("%"+ searchBy+"%"); null값이 들어오면 값이 있다고 판단
            return StringUtils.isEmpty(searchBy)  ?  null:QItem.item.itemNm.like("%"+ searchBy+"%")  ;
    }

    //검색조건 -상태
    private BooleanExpression sellSearch(SellStatus sellStatus){
        return sellStatus == null? null : QItem.item.sellStatus.eq(sellStatus);
    }


    private BooleanExpression largeCate (String largeItemType) {
        //return StringUtils.isEmpty(largeItemType)? null : QItem.item.largeItemType.like(largeItemType);
        return  StringUtils.isEmpty(largeItemType)? null : QItem.item.largeItemType.like(largeItemType);
    }
    private BooleanExpression smallCate (String subItemType) {
        //return StringUtils.isEmpty(subItemType)? null : QItem.item.subItemType.like(subItemType);
        return  StringUtils.isEmpty(subItemType)? null: QItem.item.subItemType.like(subItemType);
    }
    private BooleanExpression sexCate (String sex) {
        //return StringUtils.isEmpty(subItemType)? null : QItem.item.subItemType.like(subItemType);
        return  StringUtils.isEmpty(sex)? null: QItem.item.sex.like(sex);
    }
    private BooleanExpression userLike (Long id) {
        //return StringUtils.isEmpty(subItemType)? null : QItem.item.subItemType.like(subItemType);
        return  StringUtils.isEmpty(String.valueOf(id))? null: QLikes.likes.user.id.eq(id);
    }
    private BooleanExpression sale (String sale) {
        //return StringUtils.isEmpty(subItemType)? null : QItem.item.subItemType.like(subItemType);
        return  StringUtils.isEmpty(sale)? null:QItem.item.discount_yn.like("y");
    }

    //상품관리자페이지
    @Override
    public Page<Item> getAdminItemWithPage(SearchDto dto, Pageable pageable) {
        List<Item> result = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy())
                        )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = jpaQueryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus())
                        )
                .fetchOne();
        return new PageImpl<>(result,pageable,total);
    }

    //메인화면 -> 관심상품 표시한정보 포함해서
    @Override
    public Page<MainDto> getMainItemWithPage(SearchDto dto, Pageable pageable) { //검색 시 정렬 등록 순
         QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QReview review = QReview.review;

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,itemImg.imgUrl, item.price, item.itemNm,item.itemDtl,item.sex)
                ).from(item)
                .leftJoin(itemImg).on(item.id.eq(itemImg.item.id))
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy()))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(item)
                .leftJoin(itemImg).on(item.id.eq(itemImg.item.id))
                .where(itemImg.imgYn.eq("Y"))
                .where(regDate(dto.getSearchDate()),
                sellSearch(dto.getSellStatus()),
                itemSearchBy(dto.getSearchBy()))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }

    //검색 시
    @Override
    public Page<MainDto> getSearchItemWithPage(SearchDto dto, Pageable pageable) { //검색 시 정렬 가격 높은 순
        System.out.println("검색 페이징");
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
       //QLikes likes = QLikes.likes;
        OrderSpecifier orderSpecifier = createOrderSpecifier(dto);

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,item.itemNm, item.price,itemImg.imgUrl,item.itemDtl,item.sex,item.discount,item.discountedPrice,item.discount_yn)
                ).from(item)
                .leftJoin(itemImg).on(item.id.eq(itemImg.item.id))
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy())
                        )
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(item)
                .leftJoin(itemImg).on(item.id.eq(itemImg.item.id))
                .where(itemImg.imgYn.eq("Y"))
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy())
                       )
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }
    private OrderSpecifier createOrderSpecifier(SearchDto sort){
        QItem item = QItem.item;
        if (sort.getOrderBy().isEmpty() || sort.getOrderBy().equals("new") ){
            return new OrderSpecifier<>(Order.DESC,item.id);
        }else if (sort.getOrderBy().equals("low")){
            return new OrderSpecifier<>(Order.ASC,item.price);
        }else {
            return new OrderSpecifier<>(Order.DESC,item.price);
        }
    }


//카테고리
    @Override
    public Page<MainDto> getCateItemWithPage(CateDto cateDto,SearchDto dto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        OrderSpecifier orderSpecifier = createOrderSpecifier(dto);

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,item.itemNm, item.price, itemImg.imgUrl,item.itemDtl,item.sex,item.discount,item.discountedPrice,item.discount_yn)
                ).from(itemImg)
                .join(itemImg.item,item)
                .where(largeCate(cateDto.getLargeItemType()),smallCate(cateDto.getSubItemType()),sexCate(cateDto.getSex()),sale(cateDto.getSale()))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(orderSpecifier )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(largeCate(cateDto.getLargeItemType()),
                        smallCate(cateDto.getSubItemType()),sexCate(cateDto.getSex()),sale(cateDto.getSale()))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }

   /* @Override
    public Page<MainDto> getCateItemWithPage2(String largeItemType, String subItemType, String sm, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,itemImg.imgUrl, item.price, item.itemNm,item.itemDtl,item.sex)
                ).from(itemImg)
                .join(itemImg.item,item)
                .where(largeCate(largeItemType),smallCate(subItemType))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(QItem.item.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(largeCate(largeItemType),
                        smallCate(subItemType))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }*/

    @Override
    public long getCateItemCount(CateDto cateDto) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(largeCate(cateDto.getLargeItemType()),
                        smallCate(cateDto.getSubItemType()),sexCate(cateDto.getSex()),sale(cateDto.getSale()))
                .fetchOne();
        return count;
    }
    //검색 시 총 갯수
    @Override
    public long getSearchItemCount(SearchDto dto) {
        QItem item = QItem.item;
        long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(item)
                .where( itemSearchBy(dto.getSearchBy()))
                .fetchOne();
        return count;
    }

    @Override
    public List<Item> getLike(CateDto cateDto, SearchDto dto,Long id) {
        QItem item = QItem.item;
        QLikes likes = QLikes.likes;
        List<Item> list  =jpaQueryFactory
                .select(item)
                .from(likes)
                .join(likes.item, item)
                .where(largeCate(cateDto.getLargeItemType()),smallCate(cateDto.getSubItemType()),sexCate(cateDto.getSex()),itemSearchBy(dto.getSearchBy()),userLike(id))
                .orderBy(QItem.item.id.asc())
                .fetch();
        return list;
    }
}
