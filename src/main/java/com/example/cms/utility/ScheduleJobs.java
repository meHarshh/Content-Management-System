package com.example.cms.utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;

import com.example.cms.entity.BlogPost;
import com.example.cms.enums.PostType;
import com.example.cms.repository.BlogPostRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduleJobs {
	
	private BlogPostRepository blogPostRepository;
		
	@Scheduled(fixedDelay = 1000)
	public void logDateTime() {
		System.out.println(LocalDateTime.now());
	}
	
	@Scheduled(fixedDelay = 60 * 1000l)
	public void publishScheduledBlogPosts() {
		List<BlogPost> blogPosts = blogPostRepository
				.findAllByPublishScheduleDateTimeLessThanEqualAndPostType(LocalDateTime.now(), PostType.SCHEDULED)
				.stream().map(post -> {
					post.setPostType(PostType.PUBLISHED);
					return post;
				}).collect(Collectors.toList());
		blogPostRepository.saveAll(blogPosts);
	}


}
