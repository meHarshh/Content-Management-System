package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.requestDto.BlogPostRequest;
import com.example.cms.responseDto.BlogPostResponse;
import com.example.cms.service.BlogPostService;
import com.example.cms.utility.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@AllArgsConstructor
public class BlogPostController {

	private BlogPostService blogPostService; 
	
	@PostMapping(value = "/blogs/{blogId}/blog-posts")
	public ResponseEntity<ResponseStructure<BlogPostResponse>> saveDraft(@Valid @PathVariable int blogId,@RequestBody BlogPostRequest blogPostRequest){
		return blogPostService.saveDraft(blogId,blogPostRequest);
	}
	
	@PutMapping(value = "/blog-posts/{postId}")
	public ResponseEntity<ResponseStructure<BlogPostResponse>> updateDraft(@Valid @PathVariable int postId, @RequestBody BlogPostRequest blogPostRequest) {
		return blogPostService.updateDraft(postId,blogPostRequest);
	}
	
	@DeleteMapping(value = "/blog-posts/{postId}")
	public ResponseEntity<ResponseStructure<BlogPostResponse>> deleteBlogPost(@PathVariable int postId){
		return blogPostService.deleteBlogPost(postId);
	}
	
	@GetMapping(value = "/blog-posts/{postId}")
	public ResponseEntity<ResponseStructure<BlogPostResponse>> findBlogPost(@PathVariable int postId){
		return blogPostService.findBlogPost(postId);
	}
	
}
