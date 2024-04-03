package com.example.cms.serviceimpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.entity.BlogPost;
import com.example.cms.entity.Publish;
import com.example.cms.entity.User;
import com.example.cms.enums.PostType;
import com.example.cms.exception.BlogNotFoundByIdException;
import com.example.cms.exception.BlogPostNotFoundByIdException;
import com.example.cms.exception.IllegalAccessRequestException;
import com.example.cms.exception.UserAlreadyExistByEmailException;
import com.example.cms.exception.UserNotFoundException;
import com.example.cms.repository.BlogPostRepository;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.ContributionPanelRepository;
import com.example.cms.repository.UserRepository;
import com.example.cms.requestDto.BlogPostRequest;
import com.example.cms.responseDto.BlogPostResponse;
import com.example.cms.responseDto.PublishResponse;
import com.example.cms.service.BlogPostService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

	private ResponseStructure<BlogPostResponse> responseStructure;
	private BlogPostRepository blogPostRepository;
	private BlogRepository blogRepository;
	private ContributionPanelRepository contributionPanelRepository;
	private UserRepository userRepository;

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> saveDraft(int blogId, BlogPostRequest blogPostRequest) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByUserEmail(email).map(user -> {
			return blogRepository.findById(blogId).map(blog -> {
				if (!blog.getUser().getUserEmail().equals(email) && contributionPanelRepository
						.existsByPanelIdAndUsers(blog.getContributionPanel().getPanelId(), user))
					throw new UserNotFoundException("User not found");
				return blogRepository.findById(blogId).map(newBlog -> {
					BlogPost post = mapToBlogPost(blogPostRequest);
					post.setPostType(PostType.DRAFT);
					post.setBlog(newBlog);
					return ResponseEntity
							.ok(responseStructure.setData(mapToBlogPostResponse(blogPostRepository.save(post)))
									.setMessage("BlogPost saved as draft").setStatusCode(HttpStatus.OK.value()));
				}).orElseThrow(() -> new BlogNotFoundByIdException("Invalid blogID"));
			}).orElseThrow(() -> new UserAlreadyExistByEmailException("Invalid Email"));
		}).orElseThrow(() -> new BlogPostNotFoundByIdException("Invalid Id"));
	}

	private BlogPostResponse mapToBlogPostResponse(BlogPost blogPost) {
		BlogPostResponse blogPostResponse = BlogPostResponse.builder().postId(blogPost.getPostId())
				.title(blogPost.getTitle()).subTitle(blogPost.getSubTitle()).summary(blogPost.getSummary())
				.postType(blogPost.getPostType()).createdAt(blogPost.getCreatedAt())
				.lastModifiedAt(blogPost.getLastModifiedAt()).createdBy(blogPost.getCreatedBy())
				.lastModifiedBy(blogPost.getLastModifiedBy()).build();
		if (blogPost.getPublish() != null)
			blogPostResponse.setPublishResponse(mapToPublishResponse(blogPost.getPublish()));
		return blogPostResponse;
	}

	private PublishResponse mapToPublishResponse(Publish publish) {
		return PublishResponse.builder().publishId(publish.getPublishId()).seoTitle(publish.getSeoTitle())
				.seoTags(publish.getSeoTags()).seoDescription(publish.getSeoDescription()).build();
	}

	private BlogPost mapToBlogPost(BlogPostRequest blogPostRequest) {
		BlogPost blogPost = new BlogPost();
		blogPost.setTitle(blogPostRequest.getTitle());
		blogPost.setSubTitle(blogPostRequest.getSubTitle());
		blogPost.setSummary(blogPostRequest.getSummary());
		return blogPost;
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> updateDraft(int blogPostId,
			BlogPostRequest blogPostRequest) {
		return blogPostRepository.findById(blogPostId).map(blogPost -> {
			BlogPost post = mapToBlogPost(blogPostRequest);
			post.setPostId(blogPost.getPostId());
			BlogPost save = blogPostRepository.save(post);
			return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Updated")
					.setData(mapToBlogPostResponse(save)));
		}).orElseThrow(() -> new BlogPostNotFoundByIdException("Invalid blogId"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> deleteBlogPost(int postId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUserEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		return blogPostRepository.findById(postId).map(blogPost -> {
			if (!email.equals(blogPost.getBlog().getUser().getUserEmail()) && !contributionPanelRepository
					.existsByPanelIdAndUsers(blogPost.getBlog().getContributionPanel().getPanelId(), user))
				throw new IllegalAccessRequestException("Invalid Access");

			blogPostRepository.delete(blogPost);
			return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value()).setMessage("User Deleted")
					.setData(mapToBlogPostResponse(blogPost)));
		}).orElseThrow(() -> new BlogPostNotFoundByIdException("Invalid post ID"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> findBlogPost(int postId) {
		return blogPostRepository.findByPostIdAndPostType(postId, PostType.PUBLISHED)
				.map(blogPost -> ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value())
						.setData(mapToBlogPostResponse(blogPost)).setMessage("Fetched")))
				.orElseThrow(() -> new RuntimeException());

	}

}
