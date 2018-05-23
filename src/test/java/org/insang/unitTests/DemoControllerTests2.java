package org.insang.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.insang.domain.Member;
import org.insang.repository.MemberRepository;
import org.insang.service.Memberservice;
import org.insang.web.DemoController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoControllerTests2 {

	// @MockBean
	// private Memberservice memberService;

	// 서버를 구동시켜야 한다.
	@Test
	public void createNewMember() {
		TestRestTemplate rt = new TestRestTemplate();
		Member m = new Member();
		m.setName("insang");
		m.setEmail("insang@hansung.ac.kr");
		m.setScore(30);
		ResponseEntity<Member> mem = rt.postForEntity("http://localhost:8080/member", m, Member.class);
		assertEquals("insang", mem.getBody().getName());
		assertEquals("insang@hansung.ac.kr", mem.getBody().getEmail());
		assertEquals(30, mem.getBody().getScore());
	}

	@Test
	public void deleteMember() {

		// 다른 웹(ex- naver)의 api를 사용하려면 이렇게!
		TestRestTemplate rt = new TestRestTemplate();
		Member m = new Member();
		m.setName("insang");
		m.setEmail("insang@hansung.ac.kr");
		m.setScore(30);
		ResponseEntity<Member> mem = rt.postForEntity("http://localhost:8080/member", m, Member.class);
		//

		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<Void> re = rt.exchange(mem.getHeaders().getLocation(), HttpMethod.DELETE, entity, Void.class);

		assertEquals(HttpStatus.OK, re.getStatusCode());
	}

	@Test
	public void testGetMembers() {
		// 052302 40
		TestRestTemplate rt = new TestRestTemplate();

		Member m1 = new Member();
		m1.setName("member1");
		m1.setEmail("member1@gmail.com");
		m1.setScore(10);
		ResponseEntity<Member> mem1 = rt.postForEntity("http://localhost:8080/member", m1, Member.class);

		Member m2 = new Member();
		m2.setName("member2");
		m2.setEmail("member2@gmail.com");
		m2.setScore(20);
		ResponseEntity<Member> mem2 = rt.postForEntity("http://localhost:8080/member", m2, Member.class);

		Member m3 = new Member();
		m3.setName("member3");
		m3.setEmail("member3@gmail.com");
		m3.setScore(30);
		ResponseEntity<Member> mem3 = rt.postForEntity("http://localhost:8080/member", m3, Member.class);

		// HttpHeaders headers = new HttpHeaders();
		// HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<Member[]> re = rt.exchange("http://localhost:8080/members", HttpMethod.GET, null,
				Member[].class);
		// ResponseEntity<? extends ArrayList<Member>> re =
		// rt.getForEntity("http://localhost:8080/members", (Class<? extends
		// ArrayList<Member>>)ArrayList.class);

		Member[] mArray = re.getBody();
		assertEquals(2, mArray.length);
		assertEquals("insang1", mArray[0].getName());
		assertEquals("insang2", mArray[1].getName());
		assertEquals(20, mArray[0].getScore());
		assertEquals(30, mArray[1].getScore());
		assertEquals(HttpStatus.OK, re.getStatusCode());

	}
}