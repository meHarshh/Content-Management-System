package com.example.cms.requestDto;


import com.example.cms.responseDto.ScheduleRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublishRequest {
	private String seoTitle;
	private String seoDescription;
	private String seoTags;
	private ScheduleRequest schedule;
}
