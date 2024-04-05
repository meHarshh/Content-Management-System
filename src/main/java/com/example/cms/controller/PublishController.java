package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.requestDto.PublishRequest;
import com.example.cms.responseDto.PublishResponse;
import com.example.cms.service.PublishService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class PublishController {

	private PublishService publishService;
	
	@PostMapping(value = "/blog-posts/{postId}/publishes")
	public ResponseEntity<ResponseStructure<PublishResponse>> publishBlogPost(@PathVariable int postId, @RequestBody PublishRequest publishRequest){
		return publishService.publishBlogPost(postId,publishRequest);	
	}
	
	@PutMapping(value = "/blog-posts/{postId}/unpublish")
	public ResponseEntity<ResponseStructure<PublishResponse>> unpublishBlogPost(@PathVariable int postId){
		return publishService.unpublishBlogPost(postId);
	}
	
	
}
