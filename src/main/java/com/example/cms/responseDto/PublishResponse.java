package com.example.cms.responseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class PublishResponse {

	
	private int publishId;
	private String seoTitle;
	private String seoDescription;
	private String seoTags;
}
