package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.requestDto.BlogPostRequest;
import com.example.cms.responseDto.BlogPostResponse;
import com.example.cms.utility.ResponseStructure;

public interface BlogPostService {

	ResponseEntity<ResponseStructure<BlogPostResponse>> saveDraft(int blogId, BlogPostRequest blogPostRequest);

	ResponseEntity<ResponseStructure<BlogPostResponse>> updateDraft(int blogPostId, BlogPostRequest blogPostRequest);

	ResponseEntity<ResponseStructure<BlogPostResponse>> deleteBlogPost(int postId);

	ResponseEntity<ResponseStructure<BlogPostResponse>> findBlogPost(int postId);

}
