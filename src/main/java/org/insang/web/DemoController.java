package org.insang.web;

import java.util.List;

import org.insang.domain.Member;
import org.insang.service.Memberservice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class DemoController {
	
	@Autowired
	private Memberservice memberService;
	
	@RequestMapping(value="/members", method=RequestMethod.GET)
	public ResponseEntity<List<Member>> list() {
		final List<Member> allMembers = memberService.findAll();
		if (allMembers.isEmpty())
			return new ResponseEntity<List<Member>>(HttpStatus.NO_CONTENT);
		final HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<List<Member>>(allMembers, HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteall", method=RequestMethod.POST)
	public ResponseEntity<Void> deleteAll() {
		final List<Member> allMembers = memberService.findAll();
		if (allMembers.isEmpty())
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		memberService.deleteAll();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value="/member", method=RequestMethod.POST)
	public ResponseEntity<Member> create(@RequestBody final Member member, final UriComponentsBuilder uriBuilder) {
		Member created = memberService.create(member);	
		final HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriBuilder.path("/member/{id}").buildAndExpand(created.getId()).toUri());
		return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value={"/member/{id}"}, method=RequestMethod.PUT)
	public ResponseEntity<Member> update(@PathVariable Long id, @RequestBody final Member member) {
		Member updated = memberService.findById(id);
		if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		member.setId(updated.getId());
		updated = memberService.create(member);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	@RequestMapping(value="/member/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		Member deleted = memberService.findById(id);
		if (deleted == null) return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		memberService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}