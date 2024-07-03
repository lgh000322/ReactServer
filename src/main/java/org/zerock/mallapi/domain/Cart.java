package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "owner")
@Table(
        name = "tbl_cart",
        indexes = {@Index(name = "idx_cart_email",columnList = "member_owner")}

)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_owner")
    private Member owner;

}
