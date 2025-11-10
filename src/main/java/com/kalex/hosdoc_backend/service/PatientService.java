package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.MemberCardDTO;
import com.kalex.hosdoc_backend.dto.MemberRequestDTO;

import java.util.List;

public interface PatientService {
	List<MemberCardDTO> getMembersByUserId(Integer userId);
	MemberCardDTO createMember(Integer userId, MemberRequestDTO request);
	MemberCardDTO updateMember(Integer userId, Long memberId, MemberRequestDTO request);
	void deleteMember(Integer userId, Long memberId);
	MemberCardDTO getMemberById(Integer userId, Long memberId);
}

