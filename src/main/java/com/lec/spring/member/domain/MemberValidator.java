package com.lec.spring.member.domain;

import com.lec.spring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MemberValidator implements Validator {
    MemberService memberService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")");
        // ↓ 검증할 객체의 클래스 타입인지 확인
        boolean result = Member.class.isAssignableFrom(clazz);
        System.out.println(result);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

        // username 검증 (8~20자)
        String username = member.getUsername();
        if(username == null || username.trim().isEmpty()){
            errors.rejectValue("username", "username은 필수입니다");
        } else if(username.length() < 8 || username.length() > 20) {
            errors.rejectValue("username", "username은 8~20자 여야 합니다");
        } else if(memberService.isExist(username)){
            errors.rejectValue("username", "이미 존재하는 아이디(username) 입니다");
        }

        // password 검증 (8~16자)
        String password = member.getPassword();
        if(password == null || password.trim().isEmpty()){
            errors.rejectValue("password", "password는 필수입니다");
        } else if(password.length() < 8 || password.length() > 16) {
            errors.rejectValue("password", "password는 8~16자 여야 합니다");
        }

        // nickname 검증
        String nickname = member.getNickname();
        if(nickname == null || nickname.trim().isEmpty()){
            errors.rejectValue("nickname", "nickname은 필수입니다");
        } else if(memberService.isExistNickname(nickname)){
            errors.rejectValue("nickname", "이미 존재하는 닉네임입니다");
        }

        // email 검증
        String email = member.getEmail();
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if(email == null || email.trim().isEmpty()){
            errors.rejectValue("email", "email은 필수입니다");
        } else if(!email.matches(emailPattern)){
            errors.rejectValue("email", "유효하지 않은 이메일 형식입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name은 필수입니다");

        // 비밀번호 확인 검증
        if(!member.getPassword().equals(member.getRe_password())){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다");
        }
    }

}
