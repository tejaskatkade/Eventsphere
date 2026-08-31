package com.eventsphere.controller;

import com.eventsphere.dto.Request.MemberReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.MemberResDto;
import com.eventsphere.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    ResponseEntity<?> fetchAllMembers() {
        List<MemberResDto> members = memberService.getAllMembers();
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    @PostMapping
    ResponseEntity<ApiResponse> addMember(@RequestBody @Valid MemberReqDto memberReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(memberReqDto));
    }

    @GetMapping("/{memberId}")
    ResponseEntity<?> fetchMemberById(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberById(memberId));
    }

    @PutMapping("/{memberId}")
    ResponseEntity<?> updateMemberById(@PathVariable Long memberId, @RequestBody MemberReqDto memberReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMemberById(memberId, memberReqDto));
    }

    @PutMapping("/{memberId}/role/{roleId}")
    ResponseEntity<?> assignRoleToMember(@PathVariable Long memberId, @PathVariable Long roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMemberWithRoleById(memberId, roleId));
    }
}
