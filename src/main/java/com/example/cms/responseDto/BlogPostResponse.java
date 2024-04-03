package com.example.cms.responseDto;

import java.time.LocalDateTime;
import com.example.cms.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BlogPostResponse {

	private int postId;
	private String title;
	private String subTitle;
	private String summary;
	private PostType postType;
	private PublishResponse publishResponse;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime lastModifiedAt;
	private String lastModifiedBy;

}
