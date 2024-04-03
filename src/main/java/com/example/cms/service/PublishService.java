package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.requestDto.PublishRequest;
import com.example.cms.responseDto.PublishResponse;
import com.example.cms.utility.ResponseStructure;

public interface PublishService {

	ResponseEntity<ResponseStructure<PublishResponse>> publishBlogPost(int postId, PublishRequest publishRequest);

	ResponseEntity<ResponseStructure<PublishResponse>> unpublishBlogPost(int postId);


}
