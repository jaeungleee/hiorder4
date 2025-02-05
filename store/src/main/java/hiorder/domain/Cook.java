package hiorder.domain;

import hiorder.StoreApplication;
import hiorder.domain.Rejected;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Cook_table")
@Data
//<<< DDD / Aggregate Root
public class Cook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long tableId;

    private Long menuId;

    private Integer quantity;

    private String status;

    @PostPersist
    public void onPostPersist() {
        Rejected rejected = new Rejected(this);
        rejected.publishAfterCommit();
    }

    public static CookRepository repository() {
        CookRepository cookRepository = StoreApplication.applicationContext.getBean(
            CookRepository.class
        );
        return cookRepository;
    }

    //<<< Clean Arch / Port Method
    public void isAccept(IsAcceptCommand isAcceptCommand) {
        // Implement business logic here:

        Accepted accepted = new Accepted(this);
        accepted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void start() {
        // Implement business logic here:

        CookStarted cookStarted = new CookStarted(this);
        cookStarted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void finish() {
        // Implement business logic here:

        CookFinished cookFinished = new CookFinished(this);
        cookFinished.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void createCookInfo(OrderCreated orderCreated) {
        // 새로운 Cook 객체 생성 및 OrderCreated 이벤트로부터 데이터 설정
        Cook cook = new Cook();
        cook.setTableId(orderCreated.getTableId());
        cook.setMenuId(orderCreated.getMenuId().longValue());  // 필요 시 형 변환
        cook.setQuantity(orderCreated.getQuantity());
        cook.setStatus("CREATED"); // 초기 상태 설정

        // Cook 객체 저장
        repository().save(cook);
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root

