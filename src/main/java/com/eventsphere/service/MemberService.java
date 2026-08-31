package com.eventsphere.service;

import com.eventsphere.dto.Request.MemberReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.MemberResDto;
import java.util.List;

public interface MemberService {

    List<MemberResDto> getAllMembers();

    ApiResponse addMember(MemberReqDto memberReqDto);
    
    MemberResDto getMemberById(Long memberId);

    ApiResponse updateMemberById(Long memberId, MemberReqDto memberReqDto);

    ApiResponse updateMemberWithRoleById(Long memberId, Long roleId);
}
