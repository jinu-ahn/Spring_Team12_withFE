package com.example.spring_team12_withfe.domain;

import com.example.spring_team12_withfe.dto.member.MemberReqDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    @JsonIgnore
    private String password;

    public Member(MemberReqDto memberReqDto){
        this.username = memberReqDto.getUsername();
        this.password = memberReqDto.getPassword();
    }

}
