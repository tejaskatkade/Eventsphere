package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.MemberReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.MemberResDto;
import com.eventsphere.dto.Response.RoleResDto;
import com.eventsphere.entity.Member;
import com.eventsphere.entity.Role;
import com.eventsphere.exception.ApiException;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.MemberRepository;
import com.eventsphere.repository.RoleRepository;
import com.eventsphere.service.MemberService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    private final RoleRepository roleRepository;

    public MemberServiceImpl(MemberRepository memberRepository, ModelMapper modelMapper, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<MemberResDto> getAllMembers() {
        return memberRepository.findAll().stream().map(member -> {
            MemberResDto memberResDto = modelMapper.map(member, MemberResDto.class);

            memberResDto.setFirstName(member.getFirstName());
            memberResDto.setLastName(member.getLastName());
            memberResDto.setEmail(member.getEmail());
            memberResDto.setIsActive(member.getIsActive());
            memberResDto.setPhoneNumber(member.getPhoneNumber());
            memberResDto.setRole(
                    member.getRoles().stream().map(role -> {

                        RoleResDto roleResDto = modelMapper.map(role, RoleResDto.class);

                        roleResDto.setName(role.getName());
                        roleResDto.setDescription(role.getDescription());

                        return roleResDto;
                    }).toList()
            );

            return memberResDto;
        }).toList();
    }

    @Override
    public ApiResponse addMember(MemberReqDto memberReqDto) {

        Member member = modelMapper.map(memberReqDto, Member.class);

        if (memberRepository.existsByEmail(member.getEmail())) {
           throw new ApiException("Member already exist by Email : " + member.getEmail());
        }

        Member savedMember = memberRepository.save(member);
        return new ApiResponse("Member is added with ID : "+ savedMember.getId());
    }

    @Override
    public MemberResDto getMemberById(Long memberId) {

        return memberRepository.findById(memberId).map( (member) ->
                modelMapper.map(member, MemberResDto.class)
        ).orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }

    @Override
    public ApiResponse updateMemberById(Long memberId, MemberReqDto memberReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
        member.setFirstName(memberReqDto.getFirstName());
        member.setLastName(memberReqDto.getLastName());

        if ((!member.getEmail().equals(memberReqDto.getEmail())) && (memberRepository.existsByEmail(memberReqDto.getEmail()))) {
            throw new ApiException("Member already exist with an email : " + memberReqDto.getEmail());
        }
        member.setEmail(memberReqDto.getEmail());
        member.setPhoneNumber(memberReqDto.getPhoneNumber());
        memberRepository.save(member);
        return new ApiResponse("Member updated successfully");
    }

    @Override
    public ApiResponse updateMemberWithRoleById(Long memberId, Long roleId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
        if (member.getRoles().add(role))
            return new ApiResponse("Role : "+ role.getName() + " added successfully");
        throw new ApiException("Failed to add role");
    }
}
