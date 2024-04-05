package com.example.cms.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.cms.enums.PostType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class BlogPost {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	private String title;
	private String subTitle;
	@Column(length = 4000)
	private String summary;
	private PostType postType;
	
	@CreatedDate
	@Column(nullable = false , updatable = false)
	private LocalDateTime createdAt;
	
	
	@Column(updatable = false)
	private String createdBy;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;
	
	
	private String lastModifiedBy;
	
	@ManyToOne
	private Blog blog;
	
	@OneToOne
	private Publish publish;
	
	
}
