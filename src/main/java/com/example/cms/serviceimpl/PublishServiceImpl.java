package com.example.cms.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.entity.BlogPost;
import com.example.cms.entity.Publish;
import com.example.cms.enums.PostType;
import com.example.cms.exception.IllegalAccessRequestException;
import com.example.cms.repository.BlogPostRepository;
import com.example.cms.repository.PublishRepository;
import com.example.cms.requestDto.PublishRequest;
import com.example.cms.responseDto.PublishResponse;
import com.example.cms.service.PublishService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PublishServiceImpl implements PublishService {
	private PublishRepository publishRepository;
	private BlogPostRepository blogPostRepository;
	private ResponseStructure<PublishResponse> responseStructure;

	@Override
	public ResponseEntity<ResponseStructure<PublishResponse>> publishBlogPost(int postId,
			PublishRequest publishRequest) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return blogPostRepository.findById(postId).map(blogPost -> {

			if (!blogPost.getBlog().getUser().getUserEmail().equals(email))
				throw new IllegalAccessRequestException("Failed to publish");

			Publish publish = mapToPublish(publishRequest);
			blogPost.setPostType(PostType.PUBLISHED);
			blogPostRepository.save(blogPost);
			publish.setBlogPost(blogPost);

			Publish savedPublish = publishRepository.save(publish);
			blogPostRepository.save(blogPost);
			return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Blog Published")
					.setData(mapToPublishResponse(savedPublish)));
		}).orElseThrow(() -> new RuntimeException());
	}

	private PublishResponse mapToPublishResponse(Publish publish) {
		return PublishResponse.builder().publishId(publish.getPublishId()).seoTitle(publish.getSeoTitle())
				.seoTags(publish.getSeoTags()).seoDescription(publish.getSeoDescription()).build();
	}

	private Publish mapToPublish(PublishRequest publishRequest) {
		Publish publish = new Publish();
		publish.setSeoTitle(publishRequest.getSeoTitle());
		publish.setSeoDescription(publishRequest.getSeoDescription());
		publish.setSeoTags(publishRequest.getSeoTags());
		return publish;
	}

	@Override
	public ResponseEntity<ResponseStructure<PublishResponse>> unpublishBlogPost(int postId) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return blogPostRepository.findById(postId).map(blogPost -> {

			if (!blogPost.getBlog().getUser().getUserEmail().equals(email))
				throw new IllegalAccessRequestException("Failed to publish");

			blogPost.setPostType(PostType.DRAFT);
			BlogPost updatedBlogPost = blogPostRepository.save(blogPost);
			if (updatedBlogPost.getPublish() != null) {
				return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value())
						.setData(mapToPublishResponse(updatedBlogPost.getPublish())).setMessage("Blog unpublished"));
			} else {
				return ResponseEntity
						.ok(responseStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Blog unpublished"));
			}
		}).orElseThrow(() -> new RuntimeException("Blog post not found with ID: " + postId));
	}

}
