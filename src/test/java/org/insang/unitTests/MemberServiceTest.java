package org.insang.unitTests;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.insang.domain.Member;
import org.insang.repository.MemberRepository;
import org.insang.service.Memberservice;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


public class MemberServiceTest {
	
	@Mock
	MemberRepository memberRepository;
	
	@InjectMocks
	Memberservice memberService;

	@Test
	public void testFindAllInService() {
		MockitoAnnotations.initMocks(this);
		List<Member> mList = new ArrayList<>();
		Member m = new Member();
		m.setName("insang");
		m.setId(1234L);
		m.setEmail("insang@hansung.ac.kr");
		m.setScore(30);
		mList.add(m);
		when(memberRepository.findAll()).thenReturn(mList);
		
		
		List<Member> res = memberService.findAll();
		

		assertEquals(1, res.size());
		assertEquals("insang", res.get(0).getName());
		assertEquals("insang@hansung.ac.kr", res.get(0).getEmail());
		assertEquals(30, res.get(0).getScore());
		
		
	}

}
